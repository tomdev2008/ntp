package cn.me.xdf.model.bam;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.model.base.BamProcess;
import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static cn.me.xdf.common.json.JsonUtils.*;

/**
 *
 *
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 下午3:29
 * 点击参加课程的时候，存储此信息
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IXDF_NTP_BAM_SCORE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BamCourse extends IdEntity implements BamProcess {


    public BamCourse() {
    }



    public BamCourse(String preTeachId, String guideTeachId, String courseId, String courseJson,
                     String catalogJson, String courseContentJson) {
        this.preTeachId = preTeachId;
        this.guideTeachId = guideTeachId;
        this.courseId = courseId;
        this.initCourseJson = courseJson;
        this.initCourseContentJson = courseContentJson;
        this.initCatalogJson = catalogJson;
        this.courseJson = courseJson;
        this.catalogJson = catalogJson;
        this.courseContentJson = courseContentJson;
        this.through = false;
        this.isOpen = false;
    }


    public BamCourse(String preTeachId, String guideTeachId, String courseId,
                     String courseJson, String catalogJson, String courseContentJson,
                     boolean through, boolean isOpen) {
        this.preTeachId = preTeachId;
        this.guideTeachId = guideTeachId;
        this.courseId = courseId;
        this.initCourseJson = courseJson;
        this.initCourseContentJson = courseContentJson;
        this.initCatalogJson = catalogJson;
        this.courseJson = courseJson;
        this.catalogJson = catalogJson;
        this.courseContentJson = courseContentJson;
        this.through = through;
        this.isOpen = isOpen;

    }

    /**
     * 备课老师
     */
    private String preTeachId;

    /**
     * 指导老师
     */
    private String guideTeachId;

    /**
     * 对应课程的ID
     */
    private String courseId;


    /**
     * 对应课程的信息(初始化)
     */
    private String initCourseJson;

    /**
     * 对应章节的JSON（初始化）
     */
    private String initCatalogJson;

    /**
     * 对应章节和素材的JSON（初始化）
     */
    private String initCourseContentJson;

    /**
     * 对应课程的信息
     */
    private String courseJson;

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
     * 是否公开
     */
    private boolean isOpen;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getInitCourseJson() {
        return initCourseJson;
    }

    public void setInitCourseJson(String initCourseJson) {
        this.initCourseJson = initCourseJson;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getInitCatalogJson() {
        return initCatalogJson;
    }

    public void setInitCatalogJson(String initCatalogJson) {
        this.initCatalogJson = initCatalogJson;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getInitCourseContentJson() {
        return initCourseContentJson;
    }

    public void setInitCourseContentJson(String initCourseContentJson) {
        this.initCourseContentJson = initCourseContentJson;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getCourseJson() {
        return courseJson;
    }

    public void setCourseJson(String courseJson) {
        this.courseJson = courseJson;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getCatalogJson() {
        return catalogJson;
    }

    public void setCatalogJson(String catalogJson) {
        this.catalogJson = catalogJson;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getCourseContentJson() {
        return courseContentJson;
    }

    public void setCourseContentJson(String courseContentJson) {
        this.courseContentJson = courseContentJson;
    }

    public String getPreTeachId() {
        return preTeachId;
    }

    public void setPreTeachId(String preTeachId) {
        this.preTeachId = preTeachId;
    }

    public String getGuideTeachId() {
        return guideTeachId;
    }

    public void setGuideTeachId(String guideTeachId) {
        this.guideTeachId = guideTeachId;
    }

    @Override
    public boolean getThrough() {
        return through;
    }

    @Override
    public void setThrough(boolean through) {
        this.through = through;
    }

    /**
     * 是否公开
     *
     * @return
     */
    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
        isOpen = open;
    }

    /**
     * ************************************************************************************************
     * 各种行为 (充血)
     * **************************************************************************************************
     */


    /**
     * 备课老师
     */
    private SysOrgPerson preTeach;

    /**
     * 指导老师
     */
    private SysOrgPerson guideTeach;

    /**
     * 备课老师
     *
     * @return
     */
    public SysOrgPerson getPreTeach() {
        return preTeach;
    }

    /**
     * 备课老师
     *
     * @param preTeach
     */
    @Transient
    public void setPreTeach(SysOrgPerson preTeach) {
        this.preTeach = preTeach;
    }

    public SysOrgPerson getGuideTeach() {
        return guideTeach;
    }

    /**
     * 指导老师
     *
     * @param guideTeach
     */
    @Transient
    public void setGuideTeach(SysOrgPerson guideTeach) {
        this.guideTeach = guideTeach;
    }

    @Transient
    public CourseInfo getCourseInfo() {
        if (StringUtils.isNotBlank(courseJson)) {
            return readObjectByJson(courseJson, CourseInfo.class);
        }
        return null;
    }

    /**
     * 获取课程流程里所有章节
     *
     * @return
     */
    @Transient
    public List<CourseCatalog> getCatalogs() {
        if (StringUtils.isNotBlank(catalogJson)) {
             return readBeanByJson(catalogJson, List.class, CourseCatalog.class);
        }
        return null;
    }



    /**
     * 获取章节和素材的关系数据
     *
     * @return
     */
    @Transient
    public List<CourseContent> getCourseContents() {
        if (StringUtils.isNotBlank(courseContentJson)) {
            return readBeanByJson(courseContentJson, List.class, CourseContent.class);
        }
        return null;
    }


    /**
     * 获取章节下的所有素材
     *
     * @param catalog
     * @return
     */
    @Transient
    public List<MaterialInfo> getMaterialByCatalog(CourseCatalog catalog) {
        List<CourseContent> courseContents = getCourseContents();
        if (courseContents == null)
            return null;
        List<MaterialInfo> informs = new LinkedList<MaterialInfo>();
        ArrayUtils.sortListByProperty(courseContents, "fdMaterialNo", SortType.HIGHT);
        for (CourseContent content : courseContents) {
            if (content.getCatalog().getFdId().equals(catalog.getFdId())) {
                informs.add(content.getMaterial());
            }
        }
        return informs;
    }


    /**
     * 设置当前素材通过
     *
     * @param id
     */
    @Transient
    public void toMateridThroughById(String id) {
        List<CourseContent> courseContents = getCourseContents();
        if (courseContents == null)
            return;

        for (CourseContent content : courseContents) {
            if (content.getMaterial().getFdId().equals(id)) {
                content.getMaterial().setThrough(true);
            }
        }
        this.courseContentJson = JsonUtils.writeObjectToJson(courseContents);
    }


}
