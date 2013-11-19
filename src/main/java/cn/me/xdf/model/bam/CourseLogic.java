package cn.me.xdf.model.bam;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.process.SourceNote;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-19
 * Time: 下午4:01
 * To change this template use File | Settings | File Templates.
 */
public class CourseLogic {

    private BamCourse bamCourse;

    private SourceNote sourceNote;


    public CourseLogic(BamCourse bamCourse, SourceNote sourceNote) {
        this.bamCourse = bamCourse;
        this.sourceNote = sourceNote;

        initData();
    }

    private void initData() {
        toMateridThrough();

        toCatalogThrough();
        toCourseThroug();
    }


    public BamCourse getBamCourse() {
        return bamCourse;
    }

    /**
     * 设置当前素材通过
     */
    private void toMateridThrough() {
        List<CourseContent> courseContents = bamCourse.getCourseContents();
        if (courseContents == null)
            return;

        for (CourseContent content : courseContents) {
            if (content.getMaterial().getFdId().equals(sourceNote.getFdMaterialId())) {
                content.getMaterial().setThrough(true);
            }
        }
        bamCourse.setCourseContentJson(JsonUtils.writeObjectToJson(courseContents));
    }


    /**
     * 设置当前节通过
     */
    private void toCatalogThrough() {
        List<MaterialInfo> materialInfos = bamCourse.getMaterialByCatalog(sourceNote.getFdCatalogId());
        if (CollectionUtils.isEmpty(materialInfos))
            return;
        boolean isThrought = true;
        for (MaterialInfo materialInfo : materialInfos) {
            if (!materialInfo.getThrough()) {
                isThrought = false;
            }
        }
        if (isThrought) {
            List<CourseCatalog> catalogs = bamCourse.getCatalogs();
            for (CourseCatalog catalog : catalogs) {
                if (catalog.getFdId().equals(sourceNote.getFdCatalogId()))
                    catalog.setThrough(true);
            }
            bamCourse.setCatalogJson(JsonUtils.writeObjectToJson(catalogs));
        }
    }

    /**
     * 设置此课程结束
     */
    private void toCourseThroug() {
        List<CourseCatalog> catalogs = bamCourse.getCatalogs();
        boolean isThrought = true;
        for (CourseCatalog catalog : catalogs) {
            if (!catalog.getThrough())
                isThrought = false;
        }

        if (isThrought) {
            bamCourse.setEndDate(new Date());
            bamCourse.setThrough(true);
        }
    }


}
