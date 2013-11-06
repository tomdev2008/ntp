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
 * 作业记录
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_TASK_RECORD")
public class TaskRecord extends IdEntity{
	
	/**
	 * 所属素材记录
	 */
	private SourceNote fdSourceNode;
	
	/**
	 * 所属作业题Id
	 */
	private String fdTaskId;
	
	/**
	 * 分数
	 */
	private Double fdScore;
	
	/**
	 * 评分人Id
	 */
	private String fdAppraiserId;
	
	/**
	 * 评语
	 */
	private String fdComment;
	
	/**
	 * 作业状态
	 */
	private Integer fdTaskStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdSourceNodeId")	
	public SourceNote getFdSourceNode() {
		return fdSourceNode;
	}

	public void setFdSourceNode(SourceNote fdSourceNode) {
		this.fdSourceNode = fdSourceNode;
	}

	public String getFdTaskId() {
		return fdTaskId;
	}

	public void setFdTaskId(String fdTaskId) {
		this.fdTaskId = fdTaskId;
	}

	public Double getFdScore() {
		return fdScore;
	}

	public void setFdScore(Double fdScore) {
		this.fdScore = fdScore;
	}

	public String getFdAppraiserId() {
		return fdAppraiserId;
	}

	public void setFdAppraiserId(String fdAppraiserId) {
		this.fdAppraiserId = fdAppraiserId;
	}

	public String getFdComment() {
		return fdComment;
	}

	public void setFdComment(String fdComment) {
		this.fdComment = fdComment;
	}

	public Integer getFdTaskStatus() {
		return fdTaskStatus;
	}

	public void setFdTaskStatus(Integer fdTaskStatus) {
		this.fdTaskStatus = fdTaskStatus;
	}
	
	
}
