package cn.me.xdf.model.course;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.organization.SysOrgPerson;

/**
 * 
 * 课程实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_COURSE")
@Inheritance(strategy = InheritanceType.JOINED)
public class CourseInfo extends IdEntity{
	
	/**
	 * 课程标题
	 */
	private String fdTitle;
	
	/**
	 * 课程副标题
	 */
	private String fdSubTitle;
	
	/**
	 * 课程分类
	 */
	private CourseCategory fdCategory;
	
	/**
	 * 课程摘要
	 */
	private String fdSummary;
	
	/**
	 * 学习目标
	 */
	private String fdLearnAim;
	
	/**
	 * 建议群体
	 */
	private String fdProposalsGroup;
	
	/**
	 * 课程要求
	 */
	private String fdDemand;
	
	/**
	 * 课程封面
	 */
	private AttMain attMain;
	
	/**
	 * 课程皮肤
	 */
	private String fdSkin;
	
	/**
	 * 是否公开
	 */
	private Boolean isPublish;
	
	/**
	 * 课程密码
	 */
	private String fdPassword;
	
	/**
	 * 是否必修课
	 */
	private Boolean isCompulsoryCourse;
	
	/**
	 * 创建时间
	 */
	private Date fdCreateTime;
	
	/**
	 * 创建者
	 */
	private SysOrgPerson creator;
	
	/**
	 * 作者
	 */
	private String fdAuthor;
	
	/**
	 * 作者描述
	 */
	private String fdAuthorDescription;
	
	/**
	 * 总节数
	 */
	private Integer fdTotalPart;
	
	/**
	 * 学习顺序
	 */
	private String fdOrder;
	
	/**
	 * 是否有效
	 */
	private Boolean isAvailable;
	
	/**
	 * 状态：00草稿，01发布
	 */
	private String fdStatus;

	public String getFdTitle() {
		return fdTitle;
	}

	public void setFdTitle(String fdTitle) {
		this.fdTitle = fdTitle;
	}

	public String getFdSubTitle() {
		return fdSubTitle;
	}

	public void setFdSubTitle(String fdSubTitle) {
		this.fdSubTitle = fdSubTitle;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdCategoryId")
	public CourseCategory getFdCategory() {
		return fdCategory;
	}

	public void setFdCategory(CourseCategory fdCategory) {
		this.fdCategory = fdCategory;
	}

	@Column(length = 2000)
	public String getFdSummary() {
		return fdSummary;
	}

	public void setFdSummary(String fdSummary) {
		this.fdSummary = fdSummary;
	}

	@Column(length = 2000)
	public String getFdLearnAim() {
		return fdLearnAim;
	}

	public void setFdLearnAim(String fdLearnAim) {
		this.fdLearnAim = fdLearnAim;
	}

	public String getFdProposalsGroup() {
		return fdProposalsGroup;
	}

	public void setFdProposalsGroup(String fdProposalsGroup) {
		this.fdProposalsGroup = fdProposalsGroup;
	}

	@Column(length = 1000)
	public String getFdDemand() {
		return fdDemand;
	}

	public void setFdDemand(String fdDemand) {
		this.fdDemand = fdDemand;
	}

	@Transient
	public AttMain getAttMain() {
		return attMain;
	}

	public void setAttMain(AttMain attMain) {
		this.attMain = attMain;
	}

	public String getFdSkin() {
		return fdSkin;
	}

	public void setFdSkin(String fdSkin) {
		this.fdSkin = fdSkin;
	}

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	public String getFdPassword() {
		return fdPassword;
	}

	public void setFdPassword(String fdPassword) {
		this.fdPassword = fdPassword;
	}

	public Boolean getIsCompulsoryCourse() {
		return isCompulsoryCourse;
	}

	public void setIsCompulsoryCourse(Boolean isCompulsoryCourse) {
		this.isCompulsoryCourse = isCompulsoryCourse;
	}

	public Date getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(Date fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdCreatorId")
	public SysOrgPerson getCreator() {
		return creator;
	}

	public void setCreator(SysOrgPerson creator) {
		this.creator = creator;
	}

	public String getFdAuthor() {
		return fdAuthor;
	}

	public void setFdAuthor(String fdAuthor) {
		this.fdAuthor = fdAuthor;
	}

	@Column(length = 2000)
	public String getFdAuthorDescription() {
		return fdAuthorDescription;
	}

	public void setFdAuthorDescription(String fdAuthorDescription) {
		this.fdAuthorDescription = fdAuthorDescription;
	}

	public Integer getFdTotalPart() {
		return fdTotalPart;
	}

	public void setFdTotalPart(Integer fdTotalPart) {
		this.fdTotalPart = fdTotalPart;
	}

	public String getFdOrder() {
		return fdOrder;
	}

	public void setFdOrder(String fdOrder) {
		this.fdOrder = fdOrder;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}
	
}
