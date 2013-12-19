package cn.me.xdf.action.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.me.xdf.common.download.DownloadHelper;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.utils.Zipper;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.common.utils.excel.AbsExportExcel;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.material.ExamOpinion;
import cn.me.xdf.model.material.ExamQuestion;
import cn.me.xdf.model.material.MaterialAuth;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.material.Task;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.adviser.AdviserService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.studyTack.StudyTrackService;
import cn.me.xdf.utils.DateUtil;
import cn.me.xdf.utils.ShiroUtils;
import cn.me.xdf.view.model.VCheckTaskData;
import cn.me.xdf.view.model.VExamOpinion;
import cn.me.xdf.view.model.VExamPaperData;
import cn.me.xdf.view.model.VExamQuestion;
import cn.me.xdf.view.model.VMaterialData;
import cn.me.xdf.view.model.VStudyTrack;
import cn.me.xdf.view.model.VTask;
import cn.me.xdf.view.model.VTaskPaperAuth;
import cn.me.xdf.view.model.VTaskPaperData;


/**
 * excel导出
 * 
 * @author zq
 *
 */
@Controller
@RequestMapping("/common/exp")
public class ExpController {

	private static final Logger log = LoggerFactory.getLogger(ExpController.class);

    @Autowired
    private AttMainService attMainService;
	
	@Autowired
	private StudyTrackService studyTrackService;
	
    @Autowired
    private BamCourseService bamCourseService;
    
    @Autowired
    private AccountService accountService;
	
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AdviserService adviserService;
    
    @Autowired
    private MaterialService materialService;
    
    public SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd h:m:s a");
    
    @RequestMapping(value = "/getExpExamPaper/{id}")
    public String getExpExamPaper(@PathVariable("id") String id,
    		HttpServletRequest request,HttpServletResponse response){
    	MaterialInfo info = materialService.get(id);
    	List<VExamPaperData> examPaperList = new ArrayList<VExamPaperData>();	
    	VExamPaperData data = new VExamPaperData();
    	data.setFdAuthor(info.getFdAuthor());//作者
    	data.setFdAuthorDescription(info.getFdAuthorDescription());//作者简介
    	data.setFdStudyTime(info.getFdStudyTime());//建议学习时间
    	data.setFdName(info.getFdName());//作业包名
    	data.setFdDescription(info.getFdDescription()==null?"":info.getFdDescription());//作业包简介
    	data.setFdCreateTime(sdf.format(info.getFdCreateTime()));//创建时间
    	data.setCreatorName(info.getCreator().getRealName());//创建者
    	List<VExamQuestion> vExamList = new ArrayList<VExamQuestion>();
    	List<ExamQuestion> examList = info.getQuestions();
    	ArrayUtils.sortListByProperty(examList, "fdOrder", SortType.HIGHT); 
    	Integer totalScore = 0;
    	for (ExamQuestion examQuestion : examList) {
    		VExamQuestion question = new VExamQuestion();
    		question.setFdStandardScore(examQuestion.getFdStandardScore().intValue());//标准分
    		totalScore += examQuestion.getFdStandardScore().intValue();//总分
    		question.setFdSubject(examQuestion.getFdSubject());//简介
    		question.setFdOrder(examQuestion.getFdOrder()+1);
    		Integer fdType = examQuestion.getFdType();
    		if(fdType.equals(Constant.EXAM_QUESTION_SINGLE_SELECTION)){
    			question.setFdType("单选题");
    			question.setFdQuestion("");
    		}else if(fdType.equals(Constant.EXAM_QUESTION_MULTIPLE_SELECTION)){
    			question.setFdType("多选题");
    			question.setFdQuestion("");
    		}else if(fdType.equals(Constant.EXAM_QUESTION_CLOZE)){
    			question.setFdType("填空题");
    			question.setFdQuestion(examQuestion.getFdQuestion());
    		}
    		List<VExamOpinion> vOpinionList = new ArrayList<VExamOpinion>();
			List<ExamOpinion> opinion = examQuestion.getOpinions();
    		ArrayUtils.sortListByProperty(opinion, "fdOrder", SortType.HIGHT); 
    		for (ExamOpinion examOpinion : opinion) {
    			VExamOpinion vOpinion = new VExamOpinion();
    			vOpinion.setIsAnswer(examOpinion.getIsAnswer()==true?"是":"否");
    			vOpinion.setOpinion(examOpinion.getOpinion());
    			vOpinionList.add(vOpinion);
			}
    		question.setOpinions(vOpinionList);	
    		vExamList.add(question);
		}
    	data.setQuestions(vExamList);//设置试题
    	data.setTotalScore(totalScore);//总分
    	data.setTotalExam(examList.size());//题数
    	data.setPassScore(info.getFdScore().intValue());//及格分
    	List<VTaskPaperAuth> taskPaperAuthList = new ArrayList<VTaskPaperAuth>();
    	List<MaterialAuth> authList = info.getAuthList();//权限列表
    	data.setAuthCategory(info.getIsPublish()==true?"公开":"加密");//权限
    	for (MaterialAuth materialAuth : authList) {
    		VTaskPaperAuth auth = new VTaskPaperAuth();
    		auth.setFdName(materialAuth.getFdUser().getRealName());
    		auth.setDept(materialAuth.getFdUser().getDeptName());
    		auth.setIsEditer(materialAuth.getIsEditer()==true?"是":"否");
    		auth.setIsReader(materialAuth.getIsReader()==true?"是":"否");
    		taskPaperAuthList.add(auth);
		}
    	if(taskPaperAuthList!=null&&!taskPaperAuthList.isEmpty()){
    		data.setTaskPaperAuth(taskPaperAuthList);
    	}
    	examPaperList.add(data);
    	if(info.getIsPublish()){
    		exportExcel(examPaperList, "examPaperDetail.xls", response,"examPaperDetail.xls");
    	}else{
    		exportExcel(examPaperList, "examPaperEncrypt.xls", response,"examPaperDetail.xls");
    	}
    	
    	return null;
    }
    /**
     * 导出单个作业包信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getExpTaskPaper/{id}")
    public String getExpTaskPaper(@PathVariable("id") String id,
    		HttpServletRequest request,HttpServletResponse response){
    	MaterialInfo info = materialService.get(id);
    	List<VTaskPaperData> studyTrackList = new ArrayList<VTaskPaperData>();	
    	VTaskPaperData data = new VTaskPaperData();
    	data.setFdAuthor(info.getFdAuthor());//作者
    	data.setFdAuthorDescription(info.getFdAuthorDescription());//作者简介
    	data.setFdStudyTime(info.getFdStudyTime());//建议学习时间
    	data.setTaskPaperName(info.getFdName());//作业包名
    	data.setTaskPaperDescription(info.getFdDescription()==null?"":info.getFdDescription());//作业包简介
    	data.setFdCreateTime(sdf.format(info.getFdCreateTime()));//创建时间
    	data.setCreatorName(info.getCreator().getRealName());//创建者
    	List<VTask> vtaskList = new ArrayList<VTask>();
    	List<Task> taskList = info.getTasks();
    	ArrayUtils.sortListByProperty(taskList, "fdOrder", SortType.HIGHT); 
    	for (Task task : taskList) {
			VTask vtask = new VTask();
			vtask.setFdName(task.getFdName());
			vtask.setFdOrder(task.getFdOrder()+1);
			vtask.setFdSubject(task.getFdSubject());
			vtask.setFdStandardScore(task.getFdStandardScore());
			if(task.getFdType().equals(Constant.TASK_TYPE_UPLOAD)){
				vtask.setFdType("上传作业");
			}else{
				vtask.setFdType("在线作答");
			}
			vtaskList.add(vtask);
		}
    	data.setTasks(vtaskList);//作业list
    	Integer totalScore = 0;
    	for (Task task : taskList) {
    		totalScore += task.getFdStandardScore().intValue();
		}
    	data.setTotalScore(totalScore);//总分
    	data.setTotalTask(taskList.size());//题数
    	data.setPassScore(info.getFdScore().intValue());//及格分
    	List<VTaskPaperAuth> taskPaperAuthList = new ArrayList<VTaskPaperAuth>();
    	List<MaterialAuth> authList = info.getAuthList();//权限列表
    	data.setAuthCategory(info.getIsPublish()==true?"公开":"加密");//权限
    	for (MaterialAuth materialAuth : authList) {
    		VTaskPaperAuth auth = new VTaskPaperAuth();
    		auth.setFdName(materialAuth.getFdUser().getRealName());
    		auth.setDept(materialAuth.getFdUser().getDeptName());
    		auth.setIsEditer(materialAuth.getIsEditer()==true?"是":"否");
    		auth.setIsReader(materialAuth.getIsReader()==true?"是":"否");
    		taskPaperAuthList.add(auth);
		}
    	if(taskPaperAuthList!=null&&!taskPaperAuthList.isEmpty()){
    		data.setTaskPaperAuth(taskPaperAuthList);
    	}
    	studyTrackList.add(data);
    	if(info.getIsPublish()){
    		exportExcel(studyTrackList, "taskPaperDetail.xls", response,"taskPaperEncrypt.xls");
    	}else{
    		exportExcel(studyTrackList, "taskPaperEncrypt.xls", response,"taskPaperEncrypt.xls");
    	}
    	return null;
    }
	
    /**
	 * 导出学习跟踪excel方法
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getExpStudyTrack")
	public String getExpStudyTrack(HttpServletRequest request,HttpServletResponse response){
		String selectType = request.getParameter("selectType");
		String orderType = request.getParameter("order");
		String key = request.getParameter("key");
		String isAll = request.getParameter("isAll");
		Pagination page = studyTrackService.getStudyTrack(selectType, ShiroUtils.getUser().getId(), 1, 20000, orderType, key);
		if("noPage".equals(isAll)){//根据bamId进行导出
			String [] modeiIds = request.getParameter("modelIds").split(",");
			List<VStudyTrack> list = studyTrackService.buildStudyTrackList(modeiIds);
			String userName = ((SysOrgPerson)accountService.load(ShiroUtils.getUser().getId())).getFdName();
			Date date = new Date();
			
			exportExcel(list, "studyTrack.xls", response,"课程学习跟踪统计报表-"+userName+"-"+DateUtil.convertDateToString(date, "yyyyMMddHHmmss")+".xls");
		}else{//全部导出
			if(page.getTotalPage()==1){//全部导出（只导出一个模板，不需要打包）
				List<VStudyTrack> list = studyTrackService.buildStudyTrackList(page.getList());
				String userName = ((SysOrgPerson)accountService.load(ShiroUtils.getUser().getId())).getFdName();
				Date date = new Date();
				exportExcel(list, "studyTrack.xls", response,"课程学习跟踪统计报表-"+userName+"-"+DateUtil.convertDateToString(date, "yyyyMMddHHmmss")+".xls");
			}else if(page.getTotalPage()>1){//全部导出（导出多个模板，需要打包）
				String [] attMainIds= new String[page.getTotalPage()];
				for(int i=1;i<=page.getTotalPage();i++){
					Pagination pageZip =studyTrackService.getStudyTrack(selectType, ShiroUtils.getUser().getId(), i, 20000, orderType, key);
					AttMain attMain = AbsExportExcel.exportExcels(studyTrackService.buildStudyTrackList(pageZip.getList()), "studyTrack.xls");
					attMainService.save(attMain);
					attMainIds[i-1] = attMain.getFdId();
				}
				try {
					downloadZipsByArrayIds(attMainIds, "studyTrack.xls", request, response);
				} catch (UnsupportedEncodingException e) {
					  log.error("export excleZip error!", e);
				}
				//删除下载后的无用附件
				attMainService.deleteAttMainByIds(attMainIds);
			}
		}
		return null;
	}
	/**
	 * 导出作业包xls（导出）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExportAdviserTask")
    public String getExportAdviserTask(HttpServletRequest request,HttpServletResponse response){
		String isAll = request.getParameter("isAll");
		String fdType = request.getParameter("fdType");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		if("noPage".equals(isAll)){
			String [] modelIds = request.getParameter("modelIds").split(",");
			List<VCheckTaskData> adviserList = adviserService.findCheckDataList(modelIds, fdType);
			AbsExportExcel.exportExcel(adviserList, fdType+"Data.xls", response,fdType+"Data.xls");
		} else {
			Pagination page = adviserService.findAdivserCouserList(fdType, 1, 20000, fdName, order);
			if(page.getTotalPage()==1){//全部导出（只导出一个模板，不需要打包）
				List<VCheckTaskData> adviserList = adviserService.findCheckDataByPageList(page.getList());
				AbsExportExcel.exportExcel(adviserList, fdType+"Data.xls", response,fdType+"Data.xls");
			}else if(page.getTotalPage()>1){//全部导出（导出多个模板，需要打包）
				String [] attMainIds= new String[page.getTotalPage()];
				for(int i=1;i<=page.getTotalPage();i++){
					Pagination pageZip = adviserService.findAdivserCouserList(fdType, 1, 20000, fdName, order);
					AttMain attMain = AbsExportExcel.exportExcels(adviserService.findCheckDataByPageList(pageZip.getList()), fdType+"Data.xls");
					attMainService.save(attMain);
					attMainIds[i-1] = attMain.getFdId();
				}
				try {
					downloadZipsByArrayIds(attMainIds, fdType+"Data.xls", request, response);
				} catch (UnsupportedEncodingException e) {
					  log.error("export excleZip error!", e);
				}
				//删除下载后的无用附件
				attMainService.deleteAttMainByIds(attMainIds);
			}
		}
    	return null;
    }
	
	/**
	 * 导出日志xls（导出）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExpLog")
    public String getExpLog(HttpServletRequest request,HttpServletResponse response){
		String isAll = request.getParameter("isAll");
		String fdType = request.getParameter("fdType");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		return null;
	}
     
	@RequestMapping(value = "/getExportMaterialList")
    public String getExportMaterialList(HttpServletRequest request,HttpServletResponse response){
		String isAll = request.getParameter("isAll");
		String fdType = request.getParameter("fdType");
		String fdName = request.getParameter("fdName");
		String order = request.getParameter("order");
		if("noPage".equals(isAll)){
			String [] modelIds = request.getParameter("modelIds").split(",");
			List<VMaterialData> materialList = materialService.findExportMaterialList(modelIds, fdType);
			AbsExportExcel.exportExcel(materialList, "materialData.xls", response,"materialData.xls");
		}else{
			Pagination page = materialService.findMaterialList(fdType, 1, 20000, fdName, order);
			if(page.getTotalPage()==1){//全部导出（只导出一个模板，不需要打包）
				List<VMaterialData> materialList = materialService.findExportMaterialByPageList(page.getList());
				AbsExportExcel.exportExcel(materialList, "materialData.xls", response,"materialData.xls");
			}else if(page.getTotalPage()>1){//全部导出（导出多个模板，需要打包）
				String [] attMainIds= new String[page.getTotalPage()];
				for(int i=1;i<=page.getTotalPage();i++){
					Pagination pageZip = materialService.findMaterialList(fdType, 1, 20000, fdName, order);
					AttMain attMain = AbsExportExcel.exportExcels(
							materialService.findExportMaterialByPageList(pageZip.getList()), "materialData.xls");
					attMainService.save(attMain);
					attMainIds[i-1] = attMain.getFdId();
				}
				try {
					downloadZipsByArrayIds(attMainIds, "materialData.xls", request, response);
				} catch (UnsupportedEncodingException e) {
					  log.error("export excleZip error!", e);
				}
				//删除下载后的无用附件
				attMainService.deleteAttMainByIds(attMainIds);
			}
		}
		return null;
	}

	/**
	 * 
	 * 根据attId打包下载附件
	 * 
	 * @param ids
	 * @param zipname
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private String downloadZipsByArrayIds(String [] ids, @PathVariable("zipname") String zipname,
                                         HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        DownloadHelper dh = new DownloadHelper();
        dh.setRequest(request);
        List<AttMain> attMains = attMainService.getAttsByIds(ids);
        String agent = request.getHeader("USER-AGENT");
        downloadAttMain(attMains, agent, zipname, response);
        return null;
    }
    
    /**
     * 
     * 打包下载附件
     * 
     * @param attMains
     * @param agent
     * @param zipname
     * @param response
     * @throws UnsupportedEncodingException
     */
    private void downloadAttMain(List<AttMain> attMains, String agent, String zipname, HttpServletResponse response) throws UnsupportedEncodingException {
        if (attMains != null && !attMains.isEmpty()) {
            String temp = "";
            // 设置文件头，文件名称或编码格式
            if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
                temp = java.net.URLEncoder.encode(zipname, "UTF-8");
            } else {
                temp = new String(zipname.getBytes("UTF-8"), "ISO8859-1");
            }
            List<Zipper.FileEntry> fileEntrys = new ArrayList<Zipper.FileEntry>();
            response.setContentType("application/x-download;charset=UTF-8");
            response.addHeader("Content-disposition", "filename=" + temp + ".zip");

            for (AttMain attMain : attMains) {
                File file = new File(attMain.getFdFilePath());
                fileEntrys.add(new Zipper.FileEntry(attMain.getFdFileName(), "", file));
            }
            try {
                Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
            } catch (IOException e) {
                log.error("export db error!", e);
            }
        }
    }
    
    /**
     * 
     * 根据Vmodel导出数据
     * 
     * @param list
     * @param templateName
     * @param response
     */
    private void exportExcel(List<?> list, String templateName, HttpServletResponse response,String expFileName){
    	AbsExportExcel.exportExcel(list, templateName, response ,expFileName);
    }
	
}
