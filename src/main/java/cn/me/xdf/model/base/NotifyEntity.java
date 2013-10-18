package cn.me.xdf.model.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class NotifyEntity implements Serializable {

	public NotifyEntity() {
	}

	/**
	 * 
	 * @param fdMobileNo
	 * @param fdEmail
	 * @param realName
	 */
	public NotifyEntity(String fdMobileNo, String fdEmail, String realName) {
		this.fdEmail = fdEmail;
		this.fdMobileNo = fdMobileNo;
		this.realName = realName;
	}

	/**
	 * 手机号
	 */
	private String fdMobileNo;
	/**
	 * 对应邮箱
	 */
	private String fdEmail;

	/**
	 * 用户姓名
	 */
	private String realName;

	/**
	 * 姓名
	 * 
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 姓名
	 * 
	 * @param realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 邮件地址
	 * 
	 * @return
	 */
	@Column(name = "FD_EMAIL")
	public String getFdEmail() {
		return fdEmail;
	}

	public void setFdEmail(String fdEmail) {
		this.fdEmail = fdEmail;
	}

	/**
	 * 手机号码
	 * 
	 * @return
	 */
	@Column(name = "FD_MOBILE_NO")
	public String getFdMobileNo() {
		return fdMobileNo;
	}

	public void setFdMobileNo(String fdMobileNo) {
		this.fdMobileNo = fdMobileNo;
	}

}
