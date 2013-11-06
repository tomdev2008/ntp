package cn.me.xdf.service.bam;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.source.ISourceService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-6
 * Time: 上午11:05
 * <p/>
 * 素材进程处理类
 */
@Transactional(readOnly = true)
public class BamMaterialService extends SimpleService {


    private Map<String, ISourceService> sourceMap;

    public void setSourceMap(Map<String, ISourceService> sourceMap) {
        this.sourceMap = sourceMap;
    }


    /**
     * 保存素材完成记录
     *
     * @param request
     */
    @Transactional(readOnly = false)
    public void saveSourceNode(HttpServletRequest request) {
    }


    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;
    }


}
