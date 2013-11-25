package cn.me.xdf.action.studyTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.utils.excel.AbsExportExcel;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.studyTack.StudyTrackService;
import cn.me.xdf.view.model.VStudyTrack;
/**
 * 学习跟踪
 * 
 * @author zhaoq
 * 
 */
@Controller
@RequestMapping(value = "/studyTrack")
public class StudyTrackController {
	
	@Autowired
	private StudyTrackService studyTrackService;

	@Autowired
	private BamCourseService bamCourseService ;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CourseService courseService;
	
	/**
	 * 学习跟踪首页(我是主管)
	 * @param request
	 */
	@RequestMapping(value = "getStudyTrackDirector")
	public String getStudyTrackDirector(HttpServletRequest request) {
		return "/studyTrack/study_track_director";
	}
	
	/**
	 * 学习跟踪首页(我是导师)
	 * @param request
	 */
	@RequestMapping(value = "getStudyTrackTutor")
	public String getStudyTrackTutor(HttpServletRequest request) {
		return "/studyTrack/study_track_tutor";
	}
	
	/**
	 * 导出当前页面
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/expStudyTrackList/{modelIds}")
	public String expStudyTrackList(@PathVariable("modelIds") String[] modelIds,HttpServletRequest request,HttpServletResponse response){

		List<VStudyTrack> studyTrackList = new ArrayList<VStudyTrack>();
		for (String string : modelIds) {
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, string);
			SysOrgPerson person = (SysOrgPerson)accountService.load(bamCourse.getPreTeachId());
			CourseInfo courseInfo = courseService.load(bamCourse.getCourseId());
			VStudyTrack vStudyTrack = new VStudyTrack();
			vStudyTrack.setUserName(person.getRealName());
			vStudyTrack.setUserDep(person.getDeptName());
			vStudyTrack.setUserTel(person.getFdWorkPhone());
			vStudyTrack.setUserEmai(person.getFdEmail());
			vStudyTrack.setCourseName(courseInfo.getFdTitle());
			if(StringUtil.isEmpty(bamCourse.getGuideTeachId())){
				vStudyTrack.setGuideTeachName("没有导师");
			}else{
				vStudyTrack.setGuideTeachName(((SysOrgPerson)accountService.load(bamCourse.getGuideTeachId())).getRealName());
			}
			Map passMap = studyTrackService.passInfoByBamId(bamCourse.getFdId());
			String currLecture="";
			if(passMap.size()==0){
				currLecture="尚未开始学习";
			}else{
				CourseCatalog catalog = (CourseCatalog)passMap.get("courseCatalogNow");
				MaterialInfo materialInfo = (MaterialInfo) passMap.get("materialInfoNow");
				currLecture = catalog.getFdName()+"  ,  "+materialInfo.getFdName();
			}
			vStudyTrack.setLinkNow(currLecture);
			Map map2 = studyTrackService.getMessageInfoByBamId(bamCourse.getFdId());
			if(map2.size()==0){
				vStudyTrack.setStudyInofNow("没有学习记录");
			}else{
				vStudyTrack.setStudyInofNow((String)map2.get("cot"));
			}
			studyTrackList.add(vStudyTrack);
		}
		AbsExportExcel.exportExcel(studyTrackList, "studyTrack.xls", response);
		return null;
	}
	
	/**
	 * 导出全部（带查询）
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/expStudyTrackAll")
	public String expStudyTrackAll(HttpServletRequest request,HttpServletResponse response){
		List<VStudyTrack> studyTrackList = new ArrayList<VStudyTrack>();
		String selectType = request.getParameter("selectType");
		String orderType = request.getParameter("order");
		String key = request.getParameter("key");
		List<Object[]> bamCourses = studyTrackService.getStudyTrackAll(selectType, orderType, key);
		for (Object[] s : bamCourses) {
			BamCourse bamCourse = bamCourseService.get(BamCourse.class, (String)s[0]);
			SysOrgPerson person = (SysOrgPerson)accountService.load(bamCourse.getPreTeachId());
			CourseInfo courseInfo = courseService.load(bamCourse.getCourseId());
			VStudyTrack vStudyTrack = new VStudyTrack();
			vStudyTrack.setUserName(person.getRealName());
			vStudyTrack.setUserDep(person.getDeptName());
			vStudyTrack.setUserTel(person.getFdWorkPhone());
			vStudyTrack.setUserEmai(person.getFdEmail());
			vStudyTrack.setCourseName(courseInfo.getFdTitle());
			if(StringUtil.isEmpty(bamCourse.getGuideTeachId())){
				vStudyTrack.setGuideTeachName("没有导师");
			}else{
				vStudyTrack.setGuideTeachName(((SysOrgPerson)accountService.load(bamCourse.getGuideTeachId())).getRealName());
			}
			Map passMap = studyTrackService.passInfoByBamId(bamCourse.getFdId());
			String currLecture="";
			if(passMap.size()==0){
				currLecture="尚未开始学习";
			}else{
				CourseCatalog catalog = (CourseCatalog)passMap.get("courseCatalogNow");
				MaterialInfo materialInfo = (MaterialInfo) passMap.get("materialInfoNow");
				currLecture = catalog.getFdName()+"  ,  "+materialInfo.getFdName();
			}
			vStudyTrack.setLinkNow(currLecture);
			Map map2 = studyTrackService.getMessageInfoByBamId(bamCourse.getFdId());
			if(map2.size()==0){
				vStudyTrack.setStudyInofNow("没有学习记录");
			}else{
				vStudyTrack.setStudyInofNow((String)map2.get("cot"));
			}
			studyTrackList.add(vStudyTrack);
		}
		AbsExportExcel.exportExcel(studyTrackList, "studyTrack.xls", response);
		return null;
	}

}
