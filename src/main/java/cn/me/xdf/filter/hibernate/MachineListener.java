package cn.me.xdf.filter.hibernate;


import java.util.Date;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.log.LogApp;
import cn.me.xdf.model.log.LogLogin;
import cn.me.xdf.model.log.LogLogout;
import cn.me.xdf.model.log.LogOnline;
import cn.me.xdf.service.log.LogAppService;
import cn.me.xdf.utils.ShiroUtils;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-22
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class MachineListener
        implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {

    private static final Logger log = LoggerFactory.getLogger(MachineListener.class);

    @Autowired
    private LogAppService logAppService;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (ShiroUtils.getUser() == null)
            return;
        try {
            if (event.getEntity() instanceof IdEntity
                    && !(event.getEntity() instanceof LogApp)
                    && !(event.getEntity() instanceof LogLogin)
                    && !(event.getEntity() instanceof LogLogout)
                    && !(event.getEntity() instanceof LogOnline)
                    ) {
                String modelName = event.getEntity().getClass().getName();
                String modelId = ((IdEntity) event.getEntity()).getFdId();
                Object[] oldState = event.getOldState();
                Object[] newState = event.getState();
                String[] fields = event.getPersister().getPropertyNames();
                String con = "";
                if (oldState != null && newState != null && fields != null
                        && oldState.length == newState.length && oldState.length == fields.length) {
                    for (int i = 0; i < fields.length; i++) {
                        if ((newState[i] == null && oldState[i] != null)
                                || (newState[i] != null && !newState[i].equals(oldState[i]))) {
                            con = addStr(oldState, newState, fields,
                                    con, i);
                        }
                    }
                }
                String content = "[" + con + "]";
                String personId = ShiroUtils.getUser().getId();
                LogApp logApp = new LogApp();
                logApp.setPersonId(personId);
                logApp.setTime(new Date());
                logApp.setMethod(Constant.DB_UPDATE);
                logApp.setContent(content);
                logApp.setModelId(modelId);
                logApp.setModelName(modelName);
                logAppService.save(logApp);
            }
        } catch (Exception e) {
            log.error("log update error!", e);
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        if (ShiroUtils.getUser() == null)
            return;
        try {
            if (event.getEntity() instanceof IdEntity
                    && !(event.getEntity() instanceof LogApp)
                    && !(event.getEntity() instanceof LogLogin)
                    && !(event.getEntity() instanceof LogLogout)
                    && !(event.getEntity() instanceof LogOnline)
                    ) {
                // 保存 删除日志
                String modelName = event.getEntity().getClass().getName();
                String modelId = ((IdEntity) event.getEntity()).getFdId();
                Object[] state = event.getDeletedState();
                String[] fields = event.getPersister().getPropertyNames();
                String con = "";
                if (state != null && fields != null
                        && state.length == fields.length) {
                    for (int i = 0; i < fields.length; i++) {
                        con = addStr(null, state, fields,
                                con, i);
                    }
                }
                String content = "[" + con + "]";
                String personId = ShiroUtils.getUser().getId();
                LogApp logApp = new LogApp();
                logApp.setPersonId(personId);
                logApp.setTime(new Date());
                logApp.setMethod(Constant.DB_DELETE);
                logApp.setContent(content);
                logApp.setModelId(modelId);
                logApp.setModelName(modelName);
                logAppService.save(logApp);
            }
        } catch (Exception e) {
            log.error("log update error!", e);
        }
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (ShiroUtils.getUser() == null)
            return;
        try {
            if (event.getEntity() instanceof IdEntity
                    && !(event.getEntity() instanceof LogApp)
                    && !(event.getEntity() instanceof LogLogin)
                    && !(event.getEntity() instanceof LogLogout)
                    && !(event.getEntity() instanceof LogOnline)
                    ) {
                String modelName = event.getEntity().getClass().getName();
                String modelId = ((IdEntity) event.getEntity()).getFdId();
                Object[] state = event.getState();
                String[] fields = event.getPersister().getPropertyNames();
                String con = "";
                if (state != null && fields != null
                        && state.length == fields.length) {
                    for (int i = 0; i < fields.length; i++) {
                        con = addStr(null, state, fields,
                                con, i);
                    }
                }
                String content = "[" + con + "]";
                String personId = ShiroUtils.getUser().getId();
                LogApp logApp = new LogApp();
                logApp.setPersonId(personId);
                logApp.setTime(new Date());
                logApp.setMethod(Constant.DB_INSERT);
                logApp.setContent(content);
                logApp.setModelId(modelId);
                logApp.setModelName(modelName);
                logAppService.save(logApp);
            }
        } catch (Exception e) {
            log.error("log update error!", e);
        }
    }

    /**
     * 向content追加一个修改项
     *
     * @param oldState
     * @param newState
     * @param fields
     * @param content
     * @param i
     * @return String
     * @throws
     * @Title: addStr
     */
    private String addStr(Object[] oldState, Object[] newState,
                          String[] fields, String content, int i) {
        if (content.length() > 0) {
            content += ",";
        }
        content += "{columnName:\"" + fields[i] +
                "\",oldValue:\"" + (oldState == null ? "" : String.valueOf(oldState[i])) +
                "\",newValue:\"" + String.valueOf(newState[i]) + "\"}";
        return content;
    }
}
