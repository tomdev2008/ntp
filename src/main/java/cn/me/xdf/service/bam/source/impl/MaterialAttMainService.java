package cn.me.xdf.service.bam.source.impl;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.source.ISourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;


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
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}