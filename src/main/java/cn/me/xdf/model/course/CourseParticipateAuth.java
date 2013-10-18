package cn.me.xdf.model.course;

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
 * 课程参与权限实体的定义
 * 
 * @author zuoyi
 * 
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "IXDF_NTP_COURSE_PARTICI_AUTH")
@Inheritance(strategy = InheritanceType.JOINED)
public class CourseParticipateAuth extends IdEntity{

	/**
	 * 课程
	 */
	private CourseInfo course;
	
	/**
	 * 用户
	 */
	private SysOrgPerson user;
	
	/**
	 * 导师
	 */
	private SysOrgPerson teacher;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdCourseId")
	public CourseInfo getCourse() {
		return course;
	}

	public void setCourse(CourseInfo course) {
		this.course = course;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdUserId")
	public SysOrgPerson getUser() {
		return user;
	}

	public void setUser(SysOrgPerson user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fdTeacherId")
	public SysOrgPerson getTeacher() {
		return teacher;
	}

	public void setTeacher(SysOrgPerson teacher) {
		this.teacher = teacher;
	}
	
	
}
