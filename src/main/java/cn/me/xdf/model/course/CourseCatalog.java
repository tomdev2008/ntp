package cn.me.xdf.model.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.me.xdf.model.base.IdEntity;

/**
 * 
 * 课程章节实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_COURSE_CATALOG")
@Inheritance(strategy = InheritanceType.JOINED)
public class CourseCatalog extends IdEntity {
	
	/**
	 * 所属课程
	 */
	private CourseInfo courseInfo;
	
	/**
	 * 上级章节
	 */
	private CourseCatalog hbmParent;
	
	/**
	 * 章节名称
	 */
	private String fdName;
	
	/**
	 * 章节描述
	 */
	private String fdDescription;
	
	/**
	 * 备注
	 */
	private String fdRemark;
	
	/**
	 * 总节数
	 */
	private Integer fdTotalPart;
	
	/**
	 * 总内容项
	 */
	private Integer fdTotalContent;
	
	/**
	 * 学习时长（为分钟形式）
	 */
	private Integer fdStudyTime;
	
	/**
	 * 章节序号
	 */
	private String fdNo;
	
	/**
	 * 总序号
	 */
	private String fdTotalNo;
	
	/**
	 * 过关条件（%）
	 */
	private Double fdPassCondition;
	
	/**
	 * 素材类型：
	 * 01视频
	 * 02音频
	 * 03图片
	 * 04文档
	 * 05幻灯片
	 * 06网页(链接)
	 * 07富文本
	 * 08测试
	 * 09测评
	 * 10作业
	 * 11日程
	 */
	private String fdMaterialType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdCourseId")
	public CourseInfo getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(CourseInfo courseInfo) {
		this.courseInfo = courseInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdParentId")
	public CourseCatalog getHbmParent() {
		return hbmParent;
	}

	public void setHbmParent(CourseCatalog hbmParent) {
		this.hbmParent = hbmParent;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	@Column(length = 2000)
	public String getFdDescription() {
		return fdDescription;
	}

	public void setFdDescription(String fdDescription) {
		this.fdDescription = fdDescription;
	}

	@Column(length = 2000)
	public String getFdRemark() {
		return fdRemark;
	}

	public void setFdRemark(String fdRemark) {
		this.fdRemark = fdRemark;
	}

	public Integer getFdTotalPart() {
		return fdTotalPart;
	}

	public void setFdTotalPart(Integer fdTotalPart) {
		this.fdTotalPart = fdTotalPart;
	}

	public Integer getFdTotalContent() {
		return fdTotalContent;
	}

	public void setFdTotalContent(Integer fdTotalContent) {
		this.fdTotalContent = fdTotalContent;
	}

	public Integer getFdStudyTime() {
		return fdStudyTime;
	}

	public void setFdStudyTime(Integer fdStudyTime) {
		this.fdStudyTime = fdStudyTime;
	}

	public String getFdNo() {
		return fdNo;
	}

	public void setFdNo(String fdNo) {
		this.fdNo = fdNo;
	}

	public String getFdTotalNo() {
		return fdTotalNo;
	}

	public void setFdTotalNo(String fdTotalNo) {
		this.fdTotalNo = fdTotalNo;
	}

	public Double getFdPassCondition() {
		return fdPassCondition;
	}

	public void setFdPassCondition(Double fdPassCondition) {
		this.fdPassCondition = fdPassCondition;
	}

	public String getFdMaterialType() {
		return fdMaterialType;
	}

	public void setFdMaterialType(String fdMaterialType) {
		this.fdMaterialType = fdMaterialType;
	}
	
}
