package cn.me.xdf.service.material.source.impl;

import cn.me.xdf.model.bam.BamCourse;
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
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
@Service("materialQuestionsService")
public class MaterialQuestionsService extends SimpleService implements ISourceService {
    @Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}