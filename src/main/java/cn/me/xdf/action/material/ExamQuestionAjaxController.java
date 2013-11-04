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

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
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
	private  AttMainService attMainService;
	
	@Autowired
	private  AccountService accountService;
	
	@RequestMapping(value = "saveOrUpdateExamQuestion")
	@ResponseBody
	public Map saveOrUpdateExamQuestion(HttpServletRequest request) {
		
		String materialName = request.getParameter("materialName");
		MaterialInfo exam = new MaterialInfo();
		exam.setCreator((SysOrgPerson)accountService.load(ShiroUtils.getUser().getId()));
		exam.setFdType(Constant.MATERIAL_TYPE_TEST);
		exam.setFdName(materialName);
		exam.setFdCreateTime(new Date());
		exam.setIsPublish(true);
		exam.setIsDownload(true);
		exam.setIsAvailable(true);
		///////////////////////保存测试
		materialService.save(exam);
		ExamQuestion examQuestion = new ExamQuestion();
		List<ExamOpinion> opinions = new ArrayList<ExamOpinion>();
		List<AttMain> attMains = new ArrayList<AttMain>();
		String fdType = request.getParameter("examType");
		String fdSubject = request.getParameter("examStem");
		String fdStandardScore = request.getParameter("examScore");
		String attString = request.getParameter("listAttachment");
		String answer="";
		String opinionString = request.getParameter("listExamAnswer");
		examQuestion.setExam(exam);
		examQuestion.setFdStandardScore(new Double(fdStandardScore));
		examQuestion.setFdSubject(fdSubject);
		if(fdType.equals("multiple")){
			//单选
			examQuestion.setFdType(Constant.EXAM_QUESTION_SINGLE_SELECTION);
		}else if(fdType.equals("single")){
			//多选
			examQuestion.setFdType(Constant.EXAM_QUESTION_MULTIPLE_SELECTION);
		}else{
			//填空
			examQuestion.setFdType(Constant.EXAM_QUESTION_CLOZE);
		}
		/////////////////////////////////保存试题
		examQuestionService.save(examQuestion);
		if(StringUtil.isBlank(opinionString)){
			String[] s = fdSubject.split("#");
			String res = "";
			for(int i=0;i<s.length;i++){
				if(i%2==0){
					res = res+s[i];
				}else{
					res = res+"____";
				}
			}
			examQuestion.setFdQuestion(res);
		}else{
			List<Map> poinion = JsonUtils.readObjectByJson(opinionString, List.class);
			for (Map map : poinion) {
				ExamOpinion e = new ExamOpinion();
				e.setFdOrder(new Integer(map.get("index").toString()));
				e.setOpinion((String)map.get("name"));
				e.setQuestion(examQuestion);
				examOpinionService.save(e);
				opinions.add(e);
				//得到答案
				String isAnswer = map.get("isAnswer").toString();
				if(isAnswer.equals("true")){
					answer+=e.getFdId()+"#";
				}
			}
			examQuestion.setFdQuestion(answer);
		}
		//更新答案
		if(StringUtil.isNotBlank(attString)){
			examQuestionService.save(examQuestion);
			List<Map> att = JsonUtils.readObjectByJson(attString, List.class);
			for (Map map : att) {
				AttMain e = new AttMain();
				e.setFdModelId(examQuestion.getFdId());
				e.setFdModelName(ExamQuestion.class.getName());
				e.setFdKey(map.get("index").toString());
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
	public Map getExamsByMaterialId(HttpServletRequest request) {
		String materIalId = request.getParameter("materIalId");
		MaterialInfo info = materialService.get(materIalId);
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
