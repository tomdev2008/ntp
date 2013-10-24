package cn.me.xdf.service.base;

import java.util.ArrayList;
import java.util.List;

import cn.me.xdf.annotaion.AttMainMachine;
import cn.me.xdf.annotaion.AttValues;
import cn.me.xdf.common.file.FileUtil;
import cn.me.xdf.common.utils.MyBeanUtils;
import cn.me.xdf.model.base.IAttMain;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.SimpleService;

/**
 *
 */
@Service
@Transactional(readOnly = true)
public class AttMainService extends SimpleService {

    public AttMain get(String id) {
        return get(AttMain.class, id);
    }

    @Transactional(readOnly = false)
    public AttMain save(AttMain attMain) {
        return super.save(attMain);
    }

    /**
     * 根据模型ID和模型名称和关键字查询上传附件信息
     *
     * @param modelId
     * @param modelName
     * @param key
     * @return
     */
    public AttMain getByModelIdAndModelNameAndKey(String modelId,
                                                  String modelName, String key) {
        List<AttMain> attMains = getByModeslIdAndModelNameAndKey(modelId, modelName, key);
        if (CollectionUtils.isNotEmpty(attMains)) {
            return attMains.get(0);
        }
        return null;
    }


    public List<AttMain> getByModeslIdAndModelNameAndKey(String modelId,
                                                         String modelName, String key) {
        List<AttMain> attMains = findByCriteria(AttMain.class,
                Value.eq("fdModelId", modelId),
                Value.eq("fdModelName", modelName), Value.eq("fdKey", key));
        if (CollectionUtils.isNotEmpty(attMains)) {
            return attMains;
        }
        return null;
    }


    @Transactional(readOnly = false)
    public boolean deleteAttMain(String id) {
        AttMain attMain = get(id);
        String file = attMain.getFdFilePath();
        delete(AttMain.class, id);
        return FileUtil.delete(file);
    }

    @Transactional(readOnly = false)
    public void deleteAttMainByModelId(String modelId) {
        List<AttMain> attMains = getAttsByModelId(modelId);
        for (AttMain attMain : attMains) {
            deleteAttMain(attMain.getFdId());
        }
    }

    /**
     * 根据模型ID查询上传附件的信息
     *
     * @param modelId
     * @return
     */
    public AttMain getByModelId(String modelId) {
        List<AttMain> attMains = findByCriteria(AttMain.class,
                Value.eq("fdModelId", modelId));
        if (CollectionUtils.isNotEmpty(attMains)) {
            return attMains.get(0);
        }
        return null;
    }


    /**
     * 根据模型ID查询上传附件的信息
     *
     * @param modelId
     * @return
     */
    public List<AttMain> getAttsByModelId(String modelId) {
        return findByCriteria(AttMain.class,
                Value.eq("fdModelId", modelId));
    }


    public List<String> getFdIdsAttsByModelId(String modelId) {
        List<AttMain> attMains = getAttsByModelId(modelId);
        List<String> lists = new ArrayList<String>();
        for (AttMain att : attMains) {
            lists.add(att.getFdId());
        }
        return lists;
    }

    /**
     * 根据模型ID和模型名称查询上传附件的信息
     *
     * @param modelId
     * @param modelName
     * @return
     */
    public AttMain getByModelIdAndModelName(String modelId, String modelName) {
        List<AttMain> attMains = findByCriteria(AttMain.class,
                Value.eq("fdModelId", modelId),
                Value.eq("fdModelName", modelName));
        if (CollectionUtils.isNotEmpty(attMains)) {
            return attMains.get(0);
        }
        return null;
    }

    public void convertModelAttMain(Object o) {
        if (o instanceof IAttMain) {
            AttMainMachine attMainMachine = o.getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            String modelName = attMainMachine.modelName();
            AttValues[] attValues = attMainMachine.value();
            for (AttValues v : attValues) {
                String key = v.key();
                //存储附件的主键属性
                String field = v.fild();
                List<AttMain> list = getByModeslIdAndModelNameAndKey(modelIdValue, modelName, key);
                MyBeanUtils.setFieldValue(o, field, list);
            }
        }
    }

    @Transactional(readOnly = false)
    public void updateAttMainMachine(Object o) {
        if (o instanceof IAttMain) {
            AttMainMachine attMainMachine = o.getClass().getAnnotation(AttMainMachine.class);
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
                    List<AttMain> attIds = (List<AttMain>) fieldValue;

                    //allAttId.addAll(attIds);
                    for (AttMain attId : attIds) {
                        AttMain att = get(AttMain.class, attId.getFdId());
                        allAttId.add(attId.getFdId());
                        if (att != null) {
                            att.setFdKey(key);
                            att.setFdModelId(modelIdValue);
                            att.setFdModelName(modelName);
                            super.update(att);
                        }
                    }
                }
            }

            List<String> dbAttId = getFdIdsAttsByModelId(fdId);
            dbAttId.removeAll(allAttId);
            for (String id : dbAttId) {
                deleteAttMain(id);
            }

        }
    }

    @Transactional(readOnly = false)
    public void saveAttMainMachine(Object o) {
        if (o instanceof IAttMain) {
            AttMainMachine attMainMachine = o.getClass().getAnnotation(AttMainMachine.class);
            String modelId = attMainMachine.modelId();
            String modelName = attMainMachine.modelName();
            String modelIdValue = ObjectUtils.toString(MyBeanUtils.getFieldValue(o, modelId));
            AttValues[] attValues = attMainMachine.value();
            for (AttValues v : attValues) {

                String key = v.key();
                //存储附件的主键属性
                String field = v.fild();
                Object fieldValue = MyBeanUtils.getFieldValue(o, field);
                if (fieldValue != null) {
                    List<AttMain> attIds = (List<AttMain>) fieldValue;
                    for (AttMain attId : attIds) {
                        AttMain att = get(AttMain.class, attId.getFdId());
                        if (att != null) {
                            att.setFdKey(key);
                            att.setFdModelId(modelIdValue);
                            att.setFdModelName(modelName);
                            super.update(att);
                        }
                    }
                }

            }
        }
    }

}
