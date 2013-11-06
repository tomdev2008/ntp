package cn.me.xdf.model.process;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.me.xdf.model.base.IdEntity;


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
	 * 所属素材记录
	 */
	private SourceNote fdSourceNode;
	
	/**
	 * 所属试题Id
	 */
	private String fdQuestionId;

	/**
	 * 答案
	 */
	private String fdAnswer;
	
	public String getFdQuestionId() {
		return fdQuestionId;
	}

	public void setFdQuestionId(String fdQuestionId) {
		this.fdQuestionId = fdQuestionId;
	}

	public String getFdAnswer() {
		return fdAnswer;
	}

	public void setFdAnswer(String fdAnswer) {
		this.fdAnswer = fdAnswer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdSourceNodeId")	
	public SourceNote getFdSourceNode() {
		return fdSourceNode;
	}

	public void setFdSourceNode(SourceNote fdSourceNode) {
		this.fdSourceNode = fdSourceNode;
	}
	
}
