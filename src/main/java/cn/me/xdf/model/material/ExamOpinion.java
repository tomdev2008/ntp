package cn.me.xdf.model.material;

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
 * 试题选项实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_EXAM_OPINION")
@Inheritance(strategy = InheritanceType.JOINED)
public class ExamOpinion extends IdEntity{
	
	/**
	 * 所属试题
	 */
	private ExamQuestion question;
	
	/**
	 * 选项
	 */
	private String opinion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdQuestionId")
	public ExamQuestion getQuestion() {
		return question;
	}

	public void setQuestion(ExamQuestion question) {
		this.question = question;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
}
