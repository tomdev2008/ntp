package cn.me.xdf.model.log;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.me.xdf.model.base.IdEntity;
import cn.me.xdf.model.organization.SysOrgPerson;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_LOGONLINE")
public class LogOnline extends IdEntity{

	private SysOrgPerson person;
	
	private Date loginTime;
	
	private String ip;
	
	private Integer loginNum;
	
	private Boolean isOnline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdPersonId")
	public SysOrgPerson getPerson() {
		return person;
	}

	public void setPerson(SysOrgPerson person) {
		this.person = person;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}

	public Boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	
	
	
	
}
