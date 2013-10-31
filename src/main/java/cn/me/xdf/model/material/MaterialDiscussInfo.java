package cn.me.xdf.model.material;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 素材详述信息实体的定义（下载、播放、攒）
 * 
 * @author zhaoq
 * 
 */
import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.organization.SysOrgPerson;
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_MATERIALDISCUSSINFO")
@Inheritance(strategy = InheritanceType.JOINED)
public class MaterialDiscussInfo extends IdEntity{

	/**
	 * 操作人
	 */
	private SysOrgPerson orgPerson;
	
	/**
	 * 操作时间
	 */
	private Date creatTime;
	
	/**
	 * 操作类型(下载:01、播放:02、攒:03）
	 */
	private String fdType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdPersonId")
	public SysOrgPerson getOrgPerson() {
		return orgPerson;
	}

	public void setOrgPerson(SysOrgPerson orgPerson) {
		this.orgPerson = orgPerson;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getFdType() {
		return fdType;
	}

	public void setFdType(String fdType) {
		this.fdType = fdType;
	}
	
	
}
