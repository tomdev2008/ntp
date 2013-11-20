package cn.me.xdf.action.common;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.io.StreamUtil;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.me.xdf.common.download.DownloadHelper;
import cn.me.xdf.common.upload.FileModel;
import cn.me.xdf.common.upload.FileRepository;
import cn.me.xdf.common.utils.Zipper;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.course.CourseService;
import cn.me.xdf.service.material.MaterialDiscussInfoService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.utils.DateUtil;
import cn.me.xdf.utils.GraphUtils;
import cn.me.xdf.utils.ShiroUtils;

/**
 * @author xiaobin
 */
@Controller
@RequestMapping("/common/file")
public class FileController {
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private MaterialDiscussInfoService materialDiscussInfoService;

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    /**
     * 上传文件
     */
    @RequestMapping("/o_upload")
    @ResponseBody
    public FileModel execute(HttpServletRequest request,
                             HttpServletResponse response, ModelMap model) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        commonsMultipartResolver.setDefaultEncoding("utf-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("Filedata");
        FileModel fileModel = fileRepository.storeByExt(file);
        AttMain attMain = fileModel.toAttMain();
        attMain = attMainService.save(attMain);
        fileModel.setAttId(attMain.getFdId());
        return fileModel;
    }

    /**
     * 文件下载
     *
     * @param id (对应AttMain的主键)
     * @return
     */
    @RequestMapping("/download/{id}")
    @ResponseBody
    public DownloadHelper download(@PathVariable("id") String id,
                                   HttpServletRequest request, HttpServletResponse response) {
        DownloadHelper dh = new DownloadHelper();
        dh.setRequest(request);
        AttMain attMain = attMainService.get(AttMain.class, id);
        if (attMain != null) {

            String osName = System.getProperty("os.name");
            String filePath = attMain.getFdFilePath();
            if (StringUtils.isBlank(filePath)) {
                return dh;
            }
            if (osName.contains("Windows")) {
                filePath = filePath.replace("/", "\\\\");
            }
            String webPath = filePath;
            File file = new File(webPath);
            if (!file.exists()) {
                webPath = request.getSession().getServletContext()
                        .getRealPath("/")
                        + System.getProperty("file.separator") + filePath;
            }
            dh.setFile(new File(webPath));
            dh.setFileName(attMain.getFdFileName());
        }
        return dh;
    }

    /**
     * 文件下载（打包）
     * @param ids
     * @param zipname
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/downloadZip/{ids}/{zipname}")
    public String downloadZips(@PathVariable("ids") String[] ids, @PathVariable("zipname") String zipname,
                               HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        DownloadHelper dh = new DownloadHelper();
        dh.setRequest(request);
        List<AttMain> attMains = attMainService.getAttsByIds(ids);
        String agent = request.getHeader("USER-AGENT");
        downloadAttMain(attMains, agent, zipname, response);
        return null;
    }

    /**
     * 文件下载 （打包）
     *
     * @param
     * @return
     */
    @RequestMapping("/downloadZip/{modelId}/{zipname}")
    public String downloadZip(@PathVariable("modelId") String modelId, @PathVariable("zipname") String zipname,
                              HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        DownloadHelper dh = new DownloadHelper();
        dh.setRequest(request);
        List<AttMain> attMains = attMainService.getAttsByModelId(modelId);
        String agent = request.getHeader("USER-AGENT");
        downloadAttMain(attMains, agent, zipname, response);
        return null;
    }
    /**
     * 按文件modelId进行批量Download(打包) yuhz
     * @param ids
     * @param zipname
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/batchDownloadZip/{modelIds}/{zipname}")
    public String batchDownloadZip(@PathVariable("modelIds") String[] modelIds, @PathVariable("zipname") String zipname,
                 HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
    	DownloadHelper dh = new DownloadHelper();
        dh.setRequest(request);
        List<AttMain> attMainList = new ArrayList<AttMain>();
        for(int i=0;i<modelIds.length;i++){
        	List<AttMain> attMains = attMainService.getAttsByModelId(modelIds[i]);
        	if(attMains!=null&&attMains.size()>0){
        		for (AttMain attMain : attMains) {
        			attMainList.add(attMain);
				}
        	}
        }
        String agent = request.getHeader("USER-AGENT");
        downloadAttMain(attMainList, agent, zipname, response);     
    	return null;
    }
    /**
     * 根据素材类型进行附件全部下载 yuhz
     * @param fdType
     * @param zipname
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/allDownloadZip/{fdType}/{zipname}")
    public String allDownloadZip(@PathVariable("fdType") String fdType, @PathVariable("zipname") String zipname,
                 HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
    	DownloadHelper dh = new DownloadHelper();
        dh.setRequest(request);
        List<AttMain> attMainList = new ArrayList<AttMain>();
        List<MaterialInfo> list = materialService.findByProperty("fdType", fdType);
        if(list!=null&&list.size()>0){
        	for (MaterialInfo materialInfo : list) {
        		List<AttMain> attMains = attMainService.getAttsByModelId(materialInfo.getFdId());
        		if(attMains!=null&&attMains.size()>0){
        			for (AttMain attMain : attMains) {
            			attMainList.add(attMain);
    				}
        		}
			}
        	
        }
        String agent = request.getHeader("USER-AGENT");
        downloadAttMain(attMainList, agent, zipname, response);     
    	return null;
    }
    

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
                // 模板一般都在windows下编辑，所以默认编码为GBK
                Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
            } catch (IOException e) {
                log.error("export db error!", e);
            }
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        return BooleanUtils.toString(attMainService.deleteAttMain(id), "true", "false");
    }

    /**
     * 查看图片
     *
     * @param id       对应业务表主键，也就是AttMain的modelId
     * @param request
     * @param response
     */
    @RequestMapping("/image/{id}")
    public void image(@PathVariable("id") String id,
                      HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Cache-Control", "max-age=2592000");

        AttMain attMain = attMainService.get(id);

        //response.setContentType(attMain.getFdContentType());
        response.setContentType("image/jpeg");
        response.addHeader("Content-Length",
                String.valueOf(attMain.getFdSize()));
        OutputStream out;
        try {
            out = response.getOutputStream();
            InputStream is = new FileInputStream(attMain.getFdFilePath());
            byte buffer[] = new byte[1024];
            while (is.read(buffer, 0, 1024) != -1) {
                out.write(buffer);
            }
            out.flush();
            StreamUtil.close(is);
            StreamUtil.close(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private FileRepository fileRepository;

    @Autowired
    private AttMainService attMainService;

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    
    @Autowired
    private BamCourseService bamCourseService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CourseService courseService;
    /**
     * html转图片，并且下载
     *
     * @param id (对应AttMain的主键)
     * @return
     */
    @RequestMapping("/downloadImg")
    public void downloadImg(HttpServletRequest request, HttpServletResponse response) {
    	String bamId = request.getParameter("bamId");
    	BamCourse bamCourse = bamCourseService.get(BamCourse.class, bamId);
    	CourseInfo courseInfo = courseService.get(bamCourse.getCourseInfo().getFdId()) ;
    	SysOrgPerson orgPerson = accountService.get(ShiroUtils.getUser().getId()) ; 
    	String rootUrl=request.getScheme() + "://" + request.getServerName()+":" + request.getServerPort()+ request.getContextPath() + "/";
    	String imgUrl="";
    	if(orgPerson.getPoto().indexOf("http")>-1){
    		imgUrl=orgPerson.getPoto();
    	}else{
    		imgUrl=rootUrl+orgPerson.getPoto();
    	}
    	try {
    		String html = 
    				"<!DOCTYPE HTML>"+
		       		"<section class='container'>"+
		        	"  <section class='page-body' >"+
		        	"            <section class='bdbt2 box-certificate' id='cardId'>"+
		        	"               <div class='hd'><h2>"+courseInfo.getFdTitle()+"</h2></div>"+
		        	"                <div class='bd'>"+
		        	"                    <div class='media'>"+
		        	"                        <a class='pull-left' href='#'>"+
		        	"								<img src='"+imgUrl+"' alt=''/>"+
		        	"                            	<i class='icon-medal-lg'></i>"+
		        	"                        </a>"+
		        	"                        <div class='media-body'>"+
		        	"                            <div class='media-heading'><em>"+orgPerson.getRealName()+"</em>老师，</div>"+
		        	"                            <p>已于 "+DateUtil.convertDateToString(((bamCourse.getEndDate()==null)?new Date():bamCourse.getEndDate())) +" 完成《<em>"+courseInfo.getFdTitle()+"</em>》，特此认证。"+
		        	"                                This is to certify MR/MS <span class='upper'>"+orgPerson.getLoginName()+"</span> 's successful completion of the New Oriental Teacher Online Training.</p>"+
		        	"                            <p class='muted'>颁发者："+courseInfo.getCreator().getDeptName()+"</p>"+
		        	"                        </div>"+
		        	"                    </div>"+
		        	"                </div>"+
		        	"            </section>"+
		        	"        </section>"+
		        	"</section>";
    		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();  
	        imageGenerator.loadHtml (html);
	        byte[] img = GraphUtils.toJpeg(imageGenerator.getBufferedImage());
            response.setHeader("Content-type", "text/html;charset=utf-8");
            String imgName = java.net.URLEncoder.encode("结业证书.jpg", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+imgName);
            OutputStream out;
            response.addHeader("Content-Length",
                    String.valueOf(img.length));
            out = response.getOutputStream();
            out.write(img);
            out.flush();
            StreamUtil.close(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
