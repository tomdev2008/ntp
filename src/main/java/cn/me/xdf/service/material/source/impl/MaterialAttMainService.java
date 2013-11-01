package cn.me.xdf.service.material.source.impl;

import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.material.source.ISourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
@Service("materialAttmainService")
public class MaterialAttMainService extends SimpleService implements ISourceService {


    @Override
    public Object findSourceByMateral(MaterialInfo materialInfo) {
        return null;
    }

    @Override
    public Object findSourceByCourseCatalog(CourseCatalog catalog) {
        return null;
    }

    @Override
    public Object findSourceByMaterials(List<MaterialInfo> materialInfos) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
