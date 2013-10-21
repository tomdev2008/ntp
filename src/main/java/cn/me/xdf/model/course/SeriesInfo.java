package cn.me.xdf.model.course;



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

/**
 * 
 * 系列实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_SERIES")
@Inheritance(strategy = InheritanceType.JOINED)
public class SeriesInfo extends IdEntity{

	/**
	 * 上级系列
	 */
	private SeriesCourses hbmParent;
	
	/**
	 * 系列名称
	 */
	private String fdName;
	
	/**
	 * 系列描述
	 */
	private String fdDescription;
	
	/**
	 * 是否有效
	 */
	private Boolean isAvailable;
	
	/**
	 * 对应封面存储的附件
	 */
	private AttMain attMain;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdParentId")
	public SeriesCourses getHbmParent() {
		return hbmParent;
	}

	public void setHbmParent(SeriesCourses hbmParent) {
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

	@Transient
	public AttMain getAttMain() {
		return attMain;
	}

	public void setAttMain(AttMain attMain) {
		this.attMain = attMain;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
}
