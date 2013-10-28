package cn.me.xdf.common.upload;

import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.utils.ShiroUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * 文件上传存储类
 *
 * @author xiaobin
 */
public class FileModel {


    /**
     * 附件ID，对应AttMain的主键
     */
    private String attId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 存储类型 返回绝对路径
     */
    private StoreType storeType;

    /**
     * 文档链接类型
     */
    private String contentType;


    /**
     * 附件ID，对应AttMain的主键
     *
     * @return
     */
    public String getAttId() {
        return attId;
    }

    /**
     * 附件ID，对应AttMain的主键
     *
     * @param attId
     */
    public void setAttId(String attId) {
        this.attId = attId;
    }

    /**
     * 文件名称
     * <p/>
     * 带文件扩展名
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 文件名称 带文件扩展名
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件路径
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 文件路径
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 文件扩展名
     *
     * @return
     */
    public String getExt() {
        return ext;
    }

    /**
     * 文件扩展名
     *
     * @param ext
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * 文件大小
     *
     * @return
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * 文件大小
     *
     * @param fileSize
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 文件返回的存储路径类型
     * <p/>
     * <pre>
     * StoreType.DISK:绝对路径
     *
     * SotreType.PROJECT:项目陆军
     * </pre>
     *
     * @return
     */
    public StoreType getStoreType() {
        return storeType;
    }

    /**
     * 文件返回的存储路径类型
     * <p/>
     * <pre>
     * StoreType.DISK:绝对路径
     *
     * SotreType.PROJECT:项目陆军
     * </pre>
     *
     * @param storeType
     */
    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    /**
     * 文档链接类型
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 文档链接类型
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonIgnore
    public AttMain toAttMain() {
        AttMain att = new AttMain();
        att.setFdId(attId);
        att.setFdFileName(fileName);
        att.setFdContentType(getContentType());
        att.setFdCreateTime(new Date());
        if (ShiroUtils.getUser() != null) {
            att.setFdCreatorId(ShiroUtils.getUser().id);
        }

        att.setFdFilePath(getFilePath());
        att.setFdStoreType(getStoreType().getName());
        return att;
    }


}
