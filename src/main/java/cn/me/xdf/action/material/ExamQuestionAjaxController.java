package cn.me.xdf.action.material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

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
		if (fdType.equals("multiple")) {
			// 单选
			examQuestion.setFdType(Constant.EXAM_QUESTION_SINGLE_SELECTION);
		} else if (fdType.equals("single")) {
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
				if (i % 2 == 0) {
					res = res + s[i];
				} else {
					res = res + "____";
				}
			}
			examQuestion.setFdQuestion(res);
		} else {
			List<Map> poinion = JsonUtils.readObjectByJson(opinionString,
					List.class);
			for (Map map : poinion) {
				ExamOpinion e = new ExamOpinion();
				e.setFdOrder(new Integer(map.get("index").toString()));
				e.setOpinion((String) map.get("name"));
				e.setQuestion(examQuestion);
				examOpinionService.save(e);
				opinions.add(e);
				// 得到答案
				String isAnswer = map.get("isAnswer").toString();
				if (isAnswer.equals("true")) {
					answer += e.getFdId() + "#";
				}
			}
			examQuestion.setFdQuestion(answer);
		}
		// 更新答案
		if (StringUtil.isNotBlank(attString)) {
			examQuestionService.save(examQuestion);
			List<Map> att = JsonUtils.readObjectByJson(attString, List.class);
			for (Map map : att) {
				AttMain e = new AttMain();
				e.setFdId(map.get("id").toString());
				e.setFdModelId(examQuestion.getFdId());
				e.setFdModelName(ExamQuestion.class.getName());
				e.setFdKey(map.get("index").toString());
				e.setFdCreateTime(new Date());
				e.setFdCreatorId(ShiroUtils.getUser().getId());
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
			questionType = "multiple";
		} else if (type == 2) {
			questionType = "single";
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
			map2.put("index", attMain.getFdKey());
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
		MaterialInfo info = materialService.get(id);
		if(StringUtil.isBlank(kingUser)){
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
			ExamQuestion q =examQuestionService.get(map.get("id").toString());
			q.setFdStandardScore(new Double(map.get("editingCourse").toString()));
			examQuestionService.update(q);
			examQuestions.add(q);
		}
		List<MaterialAuth> materialAuths = new ArrayList<MaterialAuth>();
		if(!permission.equals("open")){
			for (Map map : users) {
				MaterialAuth materialAuth = new MaterialAuth();
				materialAuth.setFdUser((SysOrgPerson)accountService.load(map.get("id").toString()));
				materialAuth.setIsEditer(map.get("tissuePreparation").toString().equals("true")?true:false);
				materialAuth.setIsReader(map.get("editingCourse").toString().equals("true")?true:false);
				materialAuth.setMaterial(info);
				materialAuths.add(materialAuth);
			}
		}
		//跟新权限
		materialService.updateMaterialAuth(id, materialAuths);
		//更改测试信息
		info.setFdName(examPaperName);
		info.setFdDescription(examPaperIntro);
		info.setFdCreateTime(new Date());
		info.setFdAuthor(author);
		info.setFdAuthorDescription(authorIntro);
		info.setFdScore(new Double(score));
		info.setIsDownload(true);
		info.setIsAvailable(true);
		info.setIsPublish(permission.equals("open")?true:false);
		info.setFdStudyTime(new Integer(studyTime));
		info.setQuestions(examQuestions);
		materialService.update(info);
	}
}
