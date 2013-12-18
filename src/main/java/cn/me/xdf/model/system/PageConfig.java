package cn.me.xdf.model.system;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.me.xdf.model.base.IdEntity;
/**
 * 首页平台寄语与学校联盟的配置
 * User: zuoyi
 * Date: 13-12-18
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "SYS_APP_CONFIG")
public class PageConfig extends IdEntity{
	
	/**
	 * 排序号
	 */
	private Integer fdOrder;
	
	/**
	 * 配置类型：01平台寄语，02学校联盟
	 */
	private String fdType;
	
	/**
	 * 内容
	 */
	private String fdContent;
	
	/**
	 * 机构或人员的ID
	 */
	private String fdElementId;

	public Integer getFdOrder() {
		return fdOrder;
	}

	public void setFdOrder(Integer fdOrder) {
		this.fdOrder = fdOrder;
	}

	public String getFdType() {
		return fdType;
	}

	public void setFdType(String fdType) {
		this.fdType = fdType;
	}

	public String getFdContent() {
		return fdContent;
	}

	public void setFdContent(String fdContent) {
		this.fdContent = fdContent;
	}

	public String getFdElementId() {
		return fdElementId;
	}

	public void setFdElementId(String fdElementId) {
		this.fdElementId = fdElementId;
	}
	
}
