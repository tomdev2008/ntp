package cn.me.xdf.action.studyTrack;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 学习跟踪
 * 
 * @author zhaoq
 * 
 */
@Controller
@RequestMapping(value = "/studyTrack")
@Scope("request")
public class StudyTrackController {

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

}
