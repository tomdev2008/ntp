package cn.me.xdf.model.demo;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.BamProcess;
import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

import static cn.me.xdf.common.json.JsonUtils.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */
public class BamScore extends IdEntity implements BamProcess {

    /**
     * 对应课程的信息
     */
    private CourseInfo courseInfo;

    /**
     * 章节的json串
     */
    private String catalogJson;

    /**
     * 章节与素材的关系JSON串
     */
    private String courseContentJson;

    /**
     * 是否通过
     */
    private boolean through;


    /**
     * @return
     */
    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public String getCatalogJson() {
        return catalogJson;
    }

    public void setCatalogJson(String catalogJson) {
        this.catalogJson = catalogJson;
    }

    public String getCourseContentJson() {
        return courseContentJson;
    }

    public void setCourseContentJson(String courseContentJson) {
        this.courseContentJson = courseContentJson;
    }

    @Override
    public boolean getThrough() {
        return through;
    }

    @Override
    public void setThrough(boolean through) {
        this.through = through;
    }

    /***************************************************************************************************
     * 各种行为
     ****************************************************************************************************/

    /**
     * 获取课程流程里所有章节
     *
     * @return
     */
    @JsonIgnore
    public List<CourseCatalog> getCatalogs() {
        if (StringUtils.isNotBlank(catalogJson)) {
            return readObjectByJson(catalogJson, List.class);
        }
        return null;
    }

    /**
     *
     * @return
     */
    @JsonIgnore
    public List<CourseContent> getCourseContents() {
        if (StringUtils.isNotBlank(courseContentJson)) {
            return readObjectByJson(courseContentJson, List.class);
        }
        return null;
    }


}
