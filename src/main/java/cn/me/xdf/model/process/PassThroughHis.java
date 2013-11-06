package cn.me.xdf.model.process;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.organization.SysOrgPerson;

/**
 * 
 * 课程章节学习历史记录
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_PASS_THROUGH_HIS")
public class PassThroughHis extends IdEntity{
	
	/**
     * 所属课程
     */
    private CourseInfo courseInfo;
    
	/**
     * 所属章
     */
    private CourseCatalog fdChapter;
    
	/**
     * 所属节
     */
    private CourseCatalog fdLecture;
    
    /**
	 * 用户
	 */
	private SysOrgPerson fdUser;
	
	/**
	 * 开始时间
	 */
	private Date fdStartDate;
	
	/**
	 * 结束时间
	 */
	private Date fdEndDate;
	
	/**
	 * 所有节数
	 */
	private Integer fdTotalLectureNum;
	
	/**
	 * 已通过节数
	 */
	private Integer fdPassLectureNum;
	
	/**
	 * 所有素材数
	 */
	private Integer fdTotalMaterialNum;
	
	/**
	 * 已通过素材数
	 */
	private Integer fdPassMaterialNum;
	
	/**
	 * 通关状态
	 */
	private Boolean isPass;
	
	/**
	 * 类型（0：课程；1：章；2：节）
	 */
	private Integer fdType;

	public Integer getFdType() {
		return fdType;
	}

	public void setFdType(Integer fdType) {
		this.fdType = fdType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdCourseId")
	public CourseInfo getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(CourseInfo courseInfo) {
		this.courseInfo = courseInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdChapterId")
	public CourseCatalog getFdChapter() {
		return fdChapter;
	}

	public void setFdChapter(CourseCatalog fdChapter) {
		this.fdChapter = fdChapter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdLectureId")
	public CourseCatalog getFdLecture() {
		return fdLecture;
	}

	public void setFdLecture(CourseCatalog fdLecture) {
		this.fdLecture = fdLecture;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdUserId")
	public SysOrgPerson getFdUser() {
		return fdUser;
	}

	public void setFdUser(SysOrgPerson fdUser) {
		this.fdUser = fdUser;
	}

	public Date getFdStartDate() {
		return fdStartDate;
	}

	public void setFdStartDate(Date fdStartDate) {
		this.fdStartDate = fdStartDate;
	}

	public Date getFdEndDate() {
		return fdEndDate;
	}

	public void setFdEndDate(Date fdEndDate) {
		this.fdEndDate = fdEndDate;
	}

	public Integer getFdTotalLectureNum() {
		return fdTotalLectureNum;
	}

	public void setFdTotalLectureNum(Integer fdTotalLectureNum) {
		this.fdTotalLectureNum = fdTotalLectureNum;
	}

	public Integer getFdPassLectureNum() {
		return fdPassLectureNum;
	}

	public void setFdPassLectureNum(Integer fdPassLectureNum) {
		this.fdPassLectureNum = fdPassLectureNum;
	}

	public Integer getFdTotalMaterialNum() {
		return fdTotalMaterialNum;
	}

	public void setFdTotalMaterialNum(Integer fdTotalMaterialNum) {
		this.fdTotalMaterialNum = fdTotalMaterialNum;
	}

	public Integer getFdPassMaterialNum() {
		return fdPassMaterialNum;
	}

	public void setFdPassMaterialNum(Integer fdPassMaterialNum) {
		this.fdPassMaterialNum = fdPassMaterialNum;
	}

	public Boolean getIsPass() {
		return isPass;
	}

	public void setIsPass(Boolean isPass) {
		this.isPass = isPass;
	}
	
}
