package cn.me.xdf.model.process;

import java.util.Date;

import cn.me.xdf.model.base.IdEntity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-28
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 * 素材记录
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IXDF_NTP_SOURCE_NOTE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SourceNote extends IdEntity{
	/**
     * 所属课程Id
     */
    private String fdCourseId;
	
	/**
     * 所属节Id
     */
    private String fdCatalogId;
    
    /**
	 * 所属素材Id
	 */
	private String fdMaterialId;
	
	/**
	 * 用户Id
	 */
	private String fdUserId;

	/**
	 * 学习素材状态
	 */
	private Boolean isStudy;
	
	/**
	 * 操作时间
	 */
	private Date fdOperationDate;
	
	/**
	 * 分数
	 */
	private Double fdScore;
	
	/**
	 * 考试时长
	 */
	private Integer fdExamTime;
	
	/**
	 * 评分人Id
	 */
	private String fdAppraiserId;
	
	/**
	 * 评语
	 */
	private String fdComment;

	public String getFdCourseId() {
		return fdCourseId;
	}

	public void setFdCourseId(String fdCourseId) {
		this.fdCourseId = fdCourseId;
	}

	public Boolean getIsStudy() {
		return isStudy;
	}

	public void setIsStudy(Boolean isStudy) {
		this.isStudy = isStudy;
	}

	public Date getFdOperationDate() {
		return fdOperationDate;
	}

	public void setFdOperationDate(Date fdOperationDate) {
		this.fdOperationDate = fdOperationDate;
	}

	public Double getFdScore() {
		return fdScore;
	}

	public void setFdScore(Double fdScore) {
		this.fdScore = fdScore;
	}

	public Integer getFdExamTime() {
		return fdExamTime;
	}

	public void setFdExamTime(Integer fdExamTime) {
		this.fdExamTime = fdExamTime;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public String getFdComment() {
		return fdComment;
	}

	public void setFdComment(String fdComment) {
		this.fdComment = fdComment;
	}

	public String getFdCatalogId() {
		return fdCatalogId;
	}

	public void setFdCatalogId(String fdCatalogId) {
		this.fdCatalogId = fdCatalogId;
	}

	public String getFdMaterialId() {
		return fdMaterialId;
	}

	public void setFdMaterialId(String fdMaterialId) {
		this.fdMaterialId = fdMaterialId;
	}

	public String getFdUserId() {
		return fdUserId;
	}

	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}

	public String getFdAppraiserId() {
		return fdAppraiserId;
	}

	public void setFdAppraiserId(String fdAppraiserId) {
		this.fdAppraiserId = fdAppraiserId;
	}
	
}
