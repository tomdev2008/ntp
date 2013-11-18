package cn.me.xdf.action.material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.ExamOpinionService;
import cn.me.xdf.service.material.ExamQuestionService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/examquestion")
@Scope("request")
public class ExamQuestionAjaxController {

	@Autowired
	private MaterialService materialService;

	@Autowired
	private ExamOpinionService examOpinionService;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private AttMainService attMainService;

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "saveOrUpdateExamQuestion")
	@ResponseBody
	public Map saveOrUpdateExamQuestion(HttpServletRequest request) {

		String materialName = request.getParameter("materialName");
		String materIalId = request.getParameter("materIalId");
		MaterialInfo exam;
		if(StringUtil.isBlank(materIalId)){
			exam = new MaterialInfo();
			exam.setCreator((SysOrgPerson) accountService.load(ShiroUtils.getUser()
					.getId()));
			exam.setFdType(Constant.MATERIAL_TYPE_TEST);
			exam.setFdName(materialName);
			exam.setFdCreateTime(new Date());
			exam.setIsPublish(true);
			exam.setIsDownload(true);
			exam.setIsAvailable(true);
			// /////////////////////保存测试
			materialService.save(exam);
		}else{
			exam = materialService.get(materIalId);
		}
		String questionId = request.getParameter("questionId");
		ExamQuestion examQuestion;
		if(StringUtil.isEmpty(questionId)||questionId.equals("undefined")){
			examQuestion = new ExamQuestion();
			examQuestion.setFdOrder(exam.getQuestions().isEmpty()?0:exam.getQuestions().size());
		}else{
			examQuestion = examQuestionService.get(questionId);
		}
		List<ExamOpinion> opinions = new ArrayList<ExamOpinion>();
		List<AttMain> attMains = new ArrayList<AttMain>();
		String fdType = request.getParameter("examType");
		String fdSubject = request.getParameter("examStem");
		String fdStandardScore = request.getParameter("examScore");
		String attString = request.getParameter("listAttachment");
		String answer = "";
		String opinionString = request.getParameter("listExamAnswer");
		examQuestion.setExam(exam);
		examQuestion.setFdStandardScore(new Double(fdStandardScore));
		examQuestion.setFdSubject(fdSubject);
		if (fdType.equals("single")) {
			// 单选
			examQuestion.setFdType(Constant.EXAM_QUESTION_SINGLE_SELECTION);
		} else if (fdType.equals("multiple")) {
			// 多选
			examQuestion.setFdType(Constant.EXAM_QUESTION_MULTIPLE_SELECTION);
		} else {
			// 填空
			examQuestion.setFdType(Constant.EXAM_QUESTION_CLOZE);
		}
		// ///////////////////////////////保存试题
		examQuestionService.save(examQuestion);
		if (StringUtil.isBlank(opinionString)) {
			String[] s = fdSubject.split("#");
			String res = "";
			for (int i = 0; i < s.length; i++) {
				if (i % 2 == 1) {
					res = res + s[i]+"#";
				}
			}
			examQuestion.setFdQuestion(res);
		} else {
			//删除之前的选项
			List<ExamOpinion> oldPoinions = examOpinionService.findByProperty("question.fdId", examQuestion.getFdId());
			for (ExamOpinion examOpinion : oldPoinions) {
				examOpinionService.delete(examOpinion.getFdId());
			}
			//保存新选项
			List<Map> poinion = JsonUtils.readObjectByJson(opinionString,
					List.class);
			for (Map map : poinion) {
				ExamOpinion e = new ExamOpinion();
				e.setFdOrder(new Integer(map.get("index").toString()));
				e.setOpinion((String) map.get("name"));
				e.setQuestion(examQuestion);
				String isAnswer = map.get("isAnswer").toString();
				e.setIsAnswer(isAnswer.equals("true"));
				examOpinionService.save(e);
				opinions.add(e);
				// 得到答案
				if (isAnswer.equals("true")) {
					answer += e.getFdId() + "#";
				}
			}
			examQuestion.setFdQuestion(answer);
		}
		examQuestionService.save(examQuestion);
		// 更新选项附件
		if (StringUtil.isNotBlank(attString)) {
			//删除之前的选项
			List<AttMain> oldAttMains = attMainService.findByCriteria(AttMain.class,
	                Value.eq("fdModelId", examQuestion.getFdId()),
	                Value.eq("fdModelName", ExamQuestion.class.getName()));
			for (AttMain attMain : oldAttMains) {
				attMain.setFdModelId("");
				attMain.setFdModelName("");
				attMainService.save(attMain);
			}
			List<Map> att = JsonUtils.readObjectByJson(attString, List.class);
			for (Map map : att) {
				AttMain e = attMainService.get(map.get("id").toString());
				e.setFdModelId(examQuestion.getFdId());
				e.setFdModelName(ExamQuestion.class.getName());
				e.setFdOrder(map.get("index").toString());
				e.setFdKey("ExamQuestion");
				attMains.add(e);
				attMainService.update(e);
			}
		}
		Map retMap = new HashMap();
		retMap.put("materIalId", exam.getFdId());
		retMap.put("examQuestionId", examQuestion.getFdId());
		retMap.put("examQuestionSub", examQuestion.getFdSubject());
		return retMap;
	}

	@RequestMapping(value = "getExamsByMaterialId")
	@ResponseBody
	public String getExamsByMaterialId(HttpServletRequest request) {
		String questionId = request.getParameter("id");
		ExamQuestion question = examQuestionService.get(questionId);
		Map map = new HashMap();
		int type = question.getFdType();
		String questionType = "";
		if (type == 1) {
			questionType = "single";
		} else if (type == 2) {
			questionType = "multiple";
		} else {
			questionType = "completion";
		}
		map.put("examType", questionType);
		map.put("examScoreTotal", question.getFdStandardScore());
		map.put("examScore", question.getFdStandardScore());
		map.put("examStem", question.getFdSubject());
		List<ExamOpinion> examOpinions = question.getOpinions();
		List<Map> examOpinionsList = new ArrayList<Map>();
		for (ExamOpinion examOpinion : examOpinions) {
			Map map1 = new HashMap();
			map1.put("id", examOpinion.getFdId());
			map1.put("index", examOpinion.getFdOrder());
			map1.put("name", examOpinion.getOpinion());
			map1.put("isAnswer", examOpinion.getIsAnswer());
			examOpinionsList.add(map1);
		}
		map.put("listExamAnswer", examOpinionsList);
		List<AttMain> atts = attMainService.findByCriteria(AttMain.class,
				Value.eq("fdModelId", question.getFdId()),
				Value.eq("fdModelName", ExamQuestion.class.getName()));
		List<Map> attList = new ArrayList<Map>();
		for (AttMain attMain : atts) {
			Map map2 = new HashMap();
			map2.put("id", attMain.getFdId());
			map2.put("index", attMain.getFdOrder());
			map2.put("name", attMain.getFdFileName());
			map2.put("url", attMain.getFdFilePath());
			attList.add(map2);
		}
		map.put("listAttachment",  attList);
		return JsonUtils.writeObjectToJson(map);
	}
	
	@RequestMapping(value = "UpdateExamQuestionAndMaterial")
	@ResponseBody
	public void UpdateExamQuestionAndMaterial(HttpServletRequest request){
		//获取数据
		String id = request.getParameter("id");
		String examPaperName = request.getParameter("examPaperName");
		String examPaperIntro = request.getParameter("examPaperIntro");
		String author = request.getParameter("author");
		String authorIntro = request.getParameter("authorIntro");
		String permission = request.getParameter("permission");
		String listExam = request.getParameter("listExam");
		String kingUser = request.getParameter("kingUser");
		String score = request.getParameter("score");
		String studyTime = request.getParameter("studyTime");
		List<Map> exams;
		MaterialInfo info = materialService.findUniqueByProperty("fdId", id);
		if(StringUtil.isBlank(listExam)){
			exams = new ArrayList<Map>();
		}else{
			exams = JsonUtils.readObjectByJson(listExam, List.class);
		}
		List<Map> users;
		if(StringUtil.isBlank(kingUser)){
			users = new ArrayList<Map>();
		}else{
			users = JsonUtils.readObjectByJson(kingUser, List.class);
		}
		//更新试题分数
		List<ExamQuestion> examQuestions = new ArrayList<ExamQuestion>();
		for (Map map : exams) {
			ExamQuestion q =examQuestionService.findUniqueByProperty("fdId",map.get("id").toString());
			q.setFdStandardScore(new Double(map.get("editingCourse").toString()));
			q.setFdOrder(new Integer(map.get("index").toString()));
			examQuestionService.save(q);
			examQuestions.add(q);
		}
		//删除多余的试题
		List<ExamQuestion> oldExamQuestions = examQuestionService.findByProperty("exam.fdId", info.getFdId());
		for (ExamQuestion examQuestion : oldExamQuestions) {
			if(!examQuestions.contains(examQuestion)){
				examQuestionService.deleteQuestion(examQuestion.getFdId());
			}
		}
		List<MaterialAuth> materialAuths = new ArrayList<MaterialAuth>();
		if(!permission.equals("open")){
			for (Map map : users) {
				String personid =map.get("id").toString();
				if(personid.equals("creater")){
					continue;
				}
				MaterialAuth materialAuth = new MaterialAuth();
				materialAuth.setFdUser((SysOrgPerson)accountService.load(personid));
				materialAuth.setIsReader(map.get("tissuePreparation").toString().equals("true")?true:false);
				materialAuth.setIsEditer(map.get("editingCourse").toString().equals("true")?true:false);
				materialAuth.setMaterial(info);
				materialAuths.add(materialAuth);
			}
		}
		//跟新权限
		materialService.updateMaterialAuth(id, materialAuths);
		//更改测试信息
		info.setFdName(examPaperName);
		info.setFdDescription(examPaperIntro);
		info.setFdAuthor(author);
		info.setFdAuthorDescription(authorIntro);
		info.setFdScore(new Double(score));
		info.setIsDownload(true);
		info.setIsAvailable(true);
		info.setIsPublish(permission.equals("open")?true:false);
		info.setFdStudyTime(new Integer(studyTime));
		info.setQuestions(examQuestions);
		materialService.save(info);
	}
	
	@RequestMapping(value = "deleExamsByMaterialId")
	@ResponseBody
	public void deleExamsByMaterialId(HttpServletRequest request) {
		String materIalId = request.getParameter("materialId");
		MaterialInfo info = materialService.findUniqueByProperty("fdId", materIalId);
		List<ExamQuestion> examQuestions = info.getQuestions();
		for (ExamQuestion examQuestion : examQuestions) {
			examQuestionService.deleteQuestion(examQuestion.getFdId());
		}
		
	}
	/**
	 * 删除测试中的某个试题
	 * @param request
	 */
	@RequestMapping(value = "deleQuestionToExam")
	@ResponseBody
	public void deleQuestionToExam(HttpServletRequest request){
		String questionId = request.getParameter("questionId");
		String examId = request.getParameter("examId");		
		ExamQuestion delQuestion = examQuestionService.get(questionId);
		MaterialInfo info = materialService.get(examId);
		List<ExamQuestion> examQuestions = info.getQuestions();
		examQuestions.remove(delQuestion);
		info.setQuestions(examQuestions);
		materialService.save(info);
		examQuestionService.deleteQuestion(questionId);
	}
}
