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
import cn.me.xdf.model.organization.SysOrgPerson;

/**
 * 
 * 素材权限实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_MATERIAL_AUTH")
@Inheritance(strategy = InheritanceType.JOINED)
public class MaterialAuth extends IdEntity{
	
	/**
	 * 素材
	 */
	private MaterialInfo material;
	
	/**
	 * 用户
	 */
	private SysOrgPerson user;
	
	/**
	 * 是否可使用
	 */
	private Boolean isReader;
	
	/**
	 * 是否可编辑
	 */
	private Boolean isEditer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdMaterialId")
	public MaterialInfo getMaterial() {
		return material;
	}

	public void setMaterial(MaterialInfo material) {
		this.material = material;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdUserId")
	public SysOrgPerson getUser() {
		return user;
	}

	public void setUser(SysOrgPerson user) {
		this.user = user;
	}

	public Boolean getIsReader() {
		return isReader;
	}

	public void setIsReader(Boolean isReader) {
		this.isReader = isReader;
	}

	public Boolean getIsEditer() {
		return isEditer;
	}

	public void setIsEditer(Boolean isEditer) {
		this.isEditer = isEditer;
	}
	
}
