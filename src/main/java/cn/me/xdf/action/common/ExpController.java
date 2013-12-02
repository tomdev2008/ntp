package cn.me.xdf.action.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import cn.me.xdf.common.utils.excel.AbsExportExcel;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.adviser.AdviserService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.studyTack.StudyTrackService;
import cn.me.xdf.utils.ShiroUtils;
import cn.me.xdf.view.model.VCheckTaskData;
import cn.me.xdf.view.model.VStudyTrack;


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
			exportExcel(list, "studyTrack.xls", response);
		}else{//全部导出
			if(page.getTotalPage()==1){//全部导出（只导出一个模板，不需要打包）
				List<VStudyTrack> list = studyTrackService.buildStudyTrackList(page.getList());
				exportExcel(list, "studyTrack.xls", response);
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
			AbsExportExcel.exportExcel(adviserList, fdType+"Data.xls", response);
		} else {
			Pagination page = adviserService.findAdivserCouserList(fdType, 1, 20000, fdName, order);
			if(page.getTotalPage()==1){//全部导出（只导出一个模板，不需要打包）
				List<VCheckTaskData> adviserList = adviserService.findCheckDataByPageList(page.getList());
				AbsExportExcel.exportExcel(adviserList, fdType+"Data.xls", response);
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
    private void exportExcel(List<?> list, String templateName, HttpServletResponse response){
    	AbsExportExcel.exportExcel(list, templateName, response);
    }
	
}
