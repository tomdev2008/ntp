package cn.me.xdf.filter.hibernate;

import cn.me.xdf.annotaion.AttMainMachine;
import cn.me.xdf.annotaion.AttValues;
import cn.me.xdf.common.utils.MyBeanUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.IAttMain;
import cn.me.xdf.service.base.AttMainService;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-22
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class MachineListener extends DefaultLoadEventListener
        implements PostUpdateEventListener, PreInsertEventListener, PostDeleteEventListener  {

    @Autowired
    private AttMainService attMainService;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        Object o = event.getEntity();
        if (o instanceof IAttMain) {
            IAttMain attMain = (IAttMain) o;
            AttMainMachine attMainMachine = event.getEntity().getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            String modelName = attMainMachine.modelName();
            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            AttValues[] attValues = attMainMachine.value();
            String fdId = modelIdValue;
            List<String> allAttId = new ArrayList<String>();
            for (AttValues v : attValues) {

                String key = v.key();
                //存储附件的主键属性
                String field = v.fild();

                Object fieldValue = MyBeanUtils.getFieldValue(o, field);
                if (fieldValue != null) {
                    List<String> attIds = (List<String>) fieldValue;
                    allAttId.addAll(attIds);
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

            List<String> dbAttId = attMainService.getFdIdsAttsByModelId(fdId);
            dbAttId.removeAll(allAttId);
            for (String id : dbAttId) {
                attMainService.deleteAttMain(id);
            }

        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        Object o = event.getEntity();
        if (o instanceof IAttMain) {
            AttMainMachine attMainMachine = event.getEntity().getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();

            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            attMainService.getAttsByModelId(modelIdValue);
        }
    }


    public void onPostInsert(PostInsertEvent event) {
        System.out.println("-----------start Insert----------------------");
        Object o = event.getEntity();
        if (o instanceof IAttMain) {
            System.out.println("-----------IAttMain----------------------");
            IAttMain attMain = (IAttMain) o;
            AttMainMachine attMainMachine = event.getEntity().getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            String modelName = attMainMachine.modelName();
            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            AttValues[] attValues = attMainMachine.value();
            for (AttValues v : attValues) {

                String key = v.key();
                //存储附件的主键属性
                String field = v.fild();
                System.out.println("-------1--------field="+field);
                Object fieldValue = MyBeanUtils.getFieldValue(o, field);
                System.out.println("fieldValue=="+fieldValue);
                if (fieldValue != null) {
                    List<String> attIds = (List<String>) fieldValue;
                    for (String attId : attIds) {
                        System.out.println("-------2---------"+attId);
                        AttMain att = (AttMain) event.getSession().get(AttMain.class, attId);
                        if (att != null) {
                            System.out.println("-------3---------");
                            att.setFdKey(key);
                            att.setFdModelId(modelIdValue);
                            att.setFdModelName(modelName);
                            event.getSession().update(attMain);
                        }
                    }
                }

            }
        }
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        System.out.println("-----------start Insert----------------------");
        Object o = event.getEntity();
        if (o instanceof IAttMain) {
            System.out.println("-----------IAttMain----------------------");
            IAttMain attMain = (IAttMain) o;
            AttMainMachine attMainMachine = event.getEntity().getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            String modelName = attMainMachine.modelName();
            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            AttValues[] attValues = attMainMachine.value();
            for (AttValues v : attValues) {

                String key = v.key();
                //存储附件的主键属性
                String field = v.fild();
                System.out.println("-------1--------field="+field);
                Object fieldValue = MyBeanUtils.getFieldValue(o, field);
                System.out.println("fieldValue=="+fieldValue);
                if (fieldValue != null) {
                    List<String> attIds = (List<String>) fieldValue;
                    for (String attId : attIds) {
                        System.out.println("-------2---------"+attId);
                        AttMain att = (AttMain) event.getSession().get(AttMain.class, attId);
                        if (att != null) {
                            System.out.println("-------3---------");
                            att.setFdKey(key);
                            att.setFdModelId(modelIdValue);
                            att.setFdModelName(modelName);
                            event.getSession().update(attMain);
                        }
                    }
                }

            }
        }
        return true;
    }
}
