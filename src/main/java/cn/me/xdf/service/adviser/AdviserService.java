package cn.me.xdf.service.adviser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.model.process.TaskRecord;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.utils.ShiroUtils;
/**
 * 指导老师的service
 * @author hp
 *
 */
@Service
@Transactional(readOnly = true)
public class AdviserService extends BaseService{
	
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private AttMainService attMainService;
	
	
	@Transactional(readOnly = false)
	public List<AttMain> findNotesAtts(String fdId){
		SourceNote note = sourceNodeService.get(SourceNote.class, fdId);
		Set<TaskRecord> taskRexords = note.getTaskRecords();
		List<AttMain> attMains = new ArrayList();
		for (TaskRecord taskRecord : taskRexords) {
		   attMains = attMainService.getAttMainsByModelIdAndModelName(taskRecord.getFdId(), TaskRecord.class.getName());
		}
		return attMains;
	}
	
	/**
	 * 找出我是指导老师的所有课程信息
	 * @param fdType 已批改/或批改
	 * @param pageNo 
	 * @param pageSize
	 * @param fdName 搜索栏输入条目
	 * @param order 排序（事件/课程/导师/新教师
	 * @return
	 */
	@Transactional(readOnly = false)
	public Pagination findAdivserCouserList(String fdType,Integer pageNo, Integer pageSize,String fdName,String order){
		 Finder finder = Finder.create(" select note.* from ixdf_ntp_source_note note");
		 finder.append(" left join sys_org_person person on person.fdid = note.fduserid");
		 finder.append(" left join ixdf_ntp_course course on course.fdid = note.fdcourseid");
		 if(fdType.equalsIgnoreCase("checked")){//导师已批改
			 finder.append(" where note.fdappraiserid=:appraiserid");
			 finder.setParam("appraiserid", ShiroUtils.getUser().getId());
		 }else{
			 finder.append(" where note.fdappraiserid is null"); 
		 }
		 finder.append(" and exists(select auth.fdid from ixdf_ntp_course_partici_auth auth");
		 finder.append(" where note.fdcourseid=auth.fdcourseid");
		 finder.append(" and auth.fdteacherid=:teacherId )");
		 finder.setParam("teacherId", ShiroUtils.getUser().getId());
		 finder.append(" and exists(select material.fdid from ixdf_ntp_material material where material.fdid=note.fdmaterialid");
		 finder.append("and material.fdtype=:fdType )");
		 finder.setParam("fdType", Constant.MATERIAL_TYPE_JOBPACKAGE);//作业包
		 if(StringUtil.isNotBlank(fdName)&&StringUtil.isNotEmpty(fdName)){
			finder.append(" and person.realname like :fdName");
			finder.setParam("fdName", '%' + fdName + '%');
		 }
		 if(order.equalsIgnoreCase("fdcreatetime")){
			 finder.append(" order by note.fdoperationdate desc ");
		 }
		 if(order.equalsIgnoreCase("fdName")){
			 finder.append(" order by course.fdtitle ");
		 }
		 if(order.equalsIgnoreCase("user")){
			 finder.append(" order by user.realname ");
		 }
		 
		 Pagination page = getPageBySql(finder, pageNo, pageSize);
		 return page;
	}
	
	@Override
	public <T> Class<T> getEntityClass() {
		return null;
	}

}
