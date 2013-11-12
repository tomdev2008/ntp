package cn.me.xdf.service.bam.source.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.source.ISourceService;

@Service("materialTaskService")
public class MaterialTaskService extends SimpleService implements ISourceService  {
	@Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
