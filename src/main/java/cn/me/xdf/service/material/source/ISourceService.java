package cn.me.xdf.service.material.source;

import cn.me.xdf.model.material.MaterialInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午4:54
 * 需要的方法可以在此类里添加。
 */
public interface ISourceService {

    /**
     * 根据素材实体查询具体的素材数据
     *
     * @param materialInfo
     * @return
     */
    public List<?> findSourceByMaterial(MaterialInfo materialInfo);

}
