package cn.me.xdf.filter.hibernate;

import cn.me.xdf.annotaion.AttMainMachine;
import cn.me.xdf.common.utils.MyBeanUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.IAttMain;
import cn.me.xdf.service.base.AttMainService;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-22
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class MachineListener extends DefaultLoadEventListener
        implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {

    @Autowired
    private AttMainService attMainService;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {

    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        Object o = event.getEntity();
        if (o instanceof IAttMain) {
            AttMainMachine attMainMachine = event.getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            attMainService.getAttsByModelId(modelId);
        }
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object o = event.getEntity();
        if (o instanceof IAttMain) {
            IAttMain attMain = (IAttMain) o;
            AttMainMachine attMainMachine = event.getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            String modelName = attMainMachine.modelName();
            String key = attMainMachine.key();
            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            List<String> attIds = attMain.getAttId();
            for (String attId : attIds) {
                AttMain att = (AttMain) event.getSession().get(AttMain.class, attId);
                if (att != null) {
                    att.setFdKey(key);
                    att.setFdModelId(modelIdValue);
                    att.setFdModelName(modelName);
                    event.getSession().update(attMain);
                }
            }

        }
    }
}
