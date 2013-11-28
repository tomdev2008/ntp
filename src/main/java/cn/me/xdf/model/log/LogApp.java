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

/**
 * 
 * 操作日志
 * 
 * @author zhaoq
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_LOGAPP")
public class LogApp extends IdEntity{

	/**
	 * 操作人
	 */
	private SysOrgPerson person;
	
	/**
	 * 操作时间
	 */
	private Date time;
	
	/**
	 * ip地址
	 */
	private String ip;
	
	
	/**
	 * 操作类型
	 */
	private String method;
	
	/**
	 * SQL语句
	 */
	private String sql;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdPersonId")
	public SysOrgPerson getPerson() {
		return person;
	}

	public void setPerson(SysOrgPerson person) {
		this.person = person;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	
	
}
