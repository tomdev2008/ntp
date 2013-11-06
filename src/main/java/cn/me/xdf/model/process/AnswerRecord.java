package cn.me.xdf.model.process;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;

/**
 * 
 * 用户答题记录实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_ANSWER_RECORD")
public class AnswerRecord extends IdEntity{
	/**
     * 所属章节
     */
    private CourseCatalog fdCatalog;
    
    /**
	 * 所属素材
	 */
	private MaterialInfo fdMaterial;
	
	/**
	 * 所属试题
	 */
	private ExamQuestion fdQuestion;

	/**
	 * 用户
	 */
	private SysOrgPerson fdUser;
	
	/**
	 * 答案
	 */
	private String fdAnswer;

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
	@JoinColumn(name = "fdQuestionId")
	public ExamQuestion getFdQuestion() {
		return fdQuestion;
	}

	public void setFdQuestion(ExamQuestion fdQuestion) {
		this.fdQuestion = fdQuestion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdUserId")
	public SysOrgPerson getFdUser() {
		return fdUser;
	}

	public void setFdUser(SysOrgPerson fdUser) {
		this.fdUser = fdUser;
	}

	public String getFdAnswer() {
		return fdAnswer;
	}

	public void setFdAnswer(String fdAnswer) {
		this.fdAnswer = fdAnswer;
	}
	
}
