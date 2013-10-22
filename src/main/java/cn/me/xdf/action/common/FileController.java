package cn.me.xdf.action.common;

import cn.me.xdf.common.download.DownloadHelper;
import cn.me.xdf.common.upload.FileRepository;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.service.base.AttMainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 
 * @author xiaobin
 *
 */
@Controller
public class FileController {


    /**
     * 上传文件
     */
    @RequestMapping("/common/o_upload")
    @ResponseBody
    public cn.me.xdf.common.upload.FileModel execute(HttpServletRequest request,
                             HttpServletResponse response, ModelMap model) {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        commonsMultipartResolver.setDefaultEncoding("utf-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("Filedata");
        return fileRepository.storeByExt(file);

    }

    /**
     * 文件下载
     *
     * @param id
     *            (对应AttMain的主键)
     * @return
     */
    @RequestMapping("/common/download/{id}")
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
     * 图片查看
     */
    public void image(@PathVariable("modelId") String modelId,
                      HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Cache-Control", "max-age=2592000");

        AttMain attMain = attMainService.getByModelId(modelId);

        response.setContentType(attMain.getFdContentType());
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
            is.close();
            out.flush();
            out.close();
            out = null;
            is = null;
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

}
