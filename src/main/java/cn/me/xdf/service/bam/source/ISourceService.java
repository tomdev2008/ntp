package cn.me.xdf.service.bam.source;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import org.springframework.web.context.request.WebRequest;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午4:54
 * 需要的方法可以在此类里添加。
 */
public interface ISourceService {

    /**
     * @param bamCourse 人员对应的课程信息
     * @param catalog   对应的节信息
     * @return
     */
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog);

    /**
     * 保存素材学习进度
     * @param request
     * @return
     */
    public Object saveSourceNode(WebRequest request);

}
