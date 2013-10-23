package cn.me.xdf.service.base;

import java.util.List;

import cn.me.xdf.common.file.FileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
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
        List<AttMain> attMains = findByCriteria(AttMain.class,
                Value.eq("fdModelId", modelId),
                Value.eq("fdModelName", modelName), Value.eq("fdKey", key));
        if (CollectionUtils.isNotEmpty(attMains)) {
            return attMains.get(0);
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

}