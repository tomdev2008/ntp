package cn.me.xdf.service.material.source;

import cn.me.xdf.model.material.MaterialInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午4:54
 * To change this template use File | Settings | File Templates.
 */
public interface ISourceService {

    public List<?> findSourceByMaterial(MaterialInfo materialInfo);

}
