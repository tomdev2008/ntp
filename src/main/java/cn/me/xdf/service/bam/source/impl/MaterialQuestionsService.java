package cn.me.xdf.service.bam.source.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseContent;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.source.ISourceService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.ShiroUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;


/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
@Service("materialQuestionsService")
public class MaterialQuestionsService extends SimpleService implements ISourceService {
	
	
	@Autowired
	private AttMainService attMainService;
	
    @Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
    	String fdMtype = request.getParameter("fdMtype");
		String catalogId = request.getParameter("catalogId");
		String bamId = request.getParameter("bamId");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public Object findSubInfoByMaterial(MaterialInfo materialInfo) {
		List<Map> listExam = new ArrayList<Map>();
		List<ExamQuestion> examQuestions = materialInfo.getQuestions();
		for (ExamQuestion examQuestion2 : examQuestions) {
			Map map2 = new HashMap();
			map2.put("id", examQuestion2.getFdId());
			map2.put("index", examQuestion2.getFdOrder());
			map2.put("status", null);
			map2.put("examScore", examQuestion2.getFdStandardScore());
			map2.put("examType", examQuestion2.getFdType().equals(Constant.EXAM_QUESTION_SINGLE_SELECTION)?"single":(examQuestion2.getFdType().equals(Constant.EXAM_QUESTION_MULTIPLE_SELECTION)?"multiple":"completion"));
			map2.put("examStem", examQuestion2.getFdSubject());
			List<ExamOpinion> examOpinions = examQuestion2.getOpinions();
			List<Map> opinionlist = new ArrayList<Map>();
			for (ExamOpinion examOpinion : examOpinions) {
				Map opinionMap = new HashMap();
				opinionMap.put("index", examOpinion.getFdOrder());
				opinionMap.put("name", examOpinion.getOpinion());
				opinionMap.put("isAnswer", examOpinion.getIsAnswer());
				opinionMap.put("isChecked", false);
				opinionlist.add(opinionMap);
			}
			map2.put("listExamAnswer", opinionlist);
			List<AttMain> attMains = attMainService.findByCriteria(AttMain.class,
	                Value.eq("fdModelId", examQuestion2.getFdId()),
	                Value.eq("fdModelName", ExamQuestion.class.getName()));	
			List<Map> attlist = new ArrayList<Map>();
			for (AttMain attMain : attMains) {
				Map attMap = new HashMap();
				attMap.put("index", attMain.getFdOrder());
				attMap.put("name", attMain.getFdFileName());
				attMap.put("url", attMain.getFdFilePath());
				attlist.add(attMap);
			}
			map2.put("listAttachment", attlist);
			listExam.add(map2);
		}
		return listExam;
	}
}
