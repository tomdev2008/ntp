package cn.me.xdf.model.process;

import java.util.Date;

import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
     * 所属章节
     */
    private CourseCatalog fdCatalog;
    
    /**
	 * 所属素材
	 */
	private MaterialInfo fdMaterial;
	
	/**
	 * 用户
	 */
	private SysOrgPerson fdUser;

	/**
	 * 学习素材状态
	 */
	private Boolean isStudy;
	
	/**
	 * 操作时间
	 */
	private Date fdOperationDate;
	
	/**
	 * 作业审核结果
	 */
	private String fdAudit;
	
	/**
	 * 作业上传状态
	 */
	private Integer fdUploadStatus;
	
	/**
	 * 分数
	 */
	private Double fdScore;
	
	/**
	 * 考试时长
	 */
	private Integer fdExamTime;
	
	/**
	 * 评分人
	 */
	private SysOrgPerson fdAppraiser;
	
	/**
	 * 评语
	 */
	private String fdComment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdCatalogId")
	public CourseCatalog getFdCatalog() {
		return fdCatalog;
	}

	public void setFdCatalog(CourseCatalog fdCatalog) {
		this.fdCatalog = fdCatalog;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdMaterialId")
	public MaterialInfo getFdMaterial() {
		return fdMaterial;
	}

	public void setFdMaterial(MaterialInfo fdMaterial) {
		this.fdMaterial = fdMaterial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdUserId")
	public SysOrgPerson getFdUser() {
		return fdUser;
	}

	public void setFdUser(SysOrgPerson fdUser) {
		this.fdUser = fdUser;
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

	public String getFdAudit() {
		return fdAudit;
	}

	public void setFdAudit(String fdAudit) {
		this.fdAudit = fdAudit;
	}

	public Integer getFdUploadStatus() {
		return fdUploadStatus;
	}

	public void setFdUploadStatus(Integer fdUploadStatus) {
		this.fdUploadStatus = fdUploadStatus;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdAppraiserId")
	public SysOrgPerson getFdAppraiser() {
		return fdAppraiser;
	}

	public void setFdAppraiser(SysOrgPerson fdAppraiser) {
		this.fdAppraiser = fdAppraiser;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	public String getFdComment() {
		return fdComment;
	}

	public void setFdComment(String fdComment) {
		this.fdComment = fdComment;
	}
	
}
