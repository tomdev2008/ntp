package cn.me.xdf.service.base;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.me.xdf.common.file.FileUtil;
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

    /**
     * 保存附件
     *
     * @param attMain
     * @return
     */
    @Transactional(readOnly = false)
    public AttMain save(AttMain attMain) {
    	return super.save(attMain);
    	//return get("142c0f7aa5b3faa6efca6da479cb0c54");//图片
        //return get("142bce9c6e47bc8a2ead7054ef49a26c");//文档
       //return get("142bbbca857f4b136ef702041b59781a");//视频
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

    @Transactional(readOnly = false)
    public void deleteAttMainByIds(String[] ids) {
        for (String id : ids) {
            deleteAttMain(id);
        }
    }

    @Transactional(readOnly = false)
    public List<AttMain> getAttMainsByModelIdAndModelName(String modelId, String modelName) {
        return findByCriteria(AttMain.class, Value.eq("fdModelId", modelId), Value.eq("fdModelName", modelName));
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

    public List<AttMain> getAttsByIds(String[] ids) {
        return findByCriteria(AttMain.class, Value.in("fdId", ids));
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
