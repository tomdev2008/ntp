package cn.me.xdf.model.base;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import cn.me.xdf.service.plugin.AttMainPlugin;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * 附件存储表
 *
 * @author zuoyi
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IXDF_NTP_ATT_MAIN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AttMain extends IdEntity {

    /**
     * 附件名称
     */
    private String fdFileName;

    /**
     * 排序号
     */
    private String fdOrder;

    /**
     * 附件路径
     */
    private String fdFilePath;

    /**
     * 附件大小
     */
    private Double fdSize;

    /**
     * 存储方式01:数据库 02:磁盘
     */
    private String fdStoreType;

    /**
     * 文档类型
     * 01视频
     * 02音频
     * 03图片
     * 04文档
     * 05幻灯片
     */
    private String fdFileType;

    /**
     * 文件上传头
     */
    private String fdContentType;

    /**
     * 文件的URL路径
     */
    private String fileUrl;

    /**
     * 内容
     */
    private byte[] fdData;

    /**
     * 业务模型
     */
    private String fdModelName;

    /**
     * 业务Id
     */
    private String fdModelId;

    /**
     * 关键字
     */
    private String fdKey;

    /**
     * 位置编号
     */
    private String fdPositionNo;

    /**
     * 创建时间
     */
    private Date fdCreateTime;

    /**
     * 创建者
     */
    private String fdCreatorId;

    /**
     * 文档转化标志
     */
    private Integer flag;

    private String fileNetId;

    public String getFdFileName() {
        return fdFileName;
    }

    public void setFdFileName(String fdFileName) {
        this.fdFileName = fdFileName;
    }

    public String getFdOrder() {
        return fdOrder;
    }

    public void setFdOrder(String fdOrder) {
        this.fdOrder = fdOrder;
    }

    public String getFdContentType() {
        return fdContentType;
    }

    public void setFdContentType(String fdContentType) {
        this.fdContentType = fdContentType;
    }

    public String getFdFilePath() {
        return fdFilePath;
    }

    public void setFdFilePath(String fdFilePath) {
        this.fdFilePath = fdFilePath;
    }

    public Double getFdSize() {
        return fdSize;
    }

    public void setFdSize(Double fdSize) {
        this.fdSize = fdSize;
    }

    public String getFdStoreType() {
        return fdStoreType;
    }

    public void setFdStoreType(String fdStoreType) {
        this.fdStoreType = fdStoreType;
    }

    @Basic(fetch = FetchType.LAZY)
    public String getFileUrl() {
        try {
            //视频、文档、幻灯片
            if (("01".equals(fdFileType) || "04".equals(fdFileType) || "05".equals(fdFileType)) && StringUtils.isNotBlank(fileNetId)) {
                if (StringUtils.isBlank(fileUrl)) {
                    fileUrl = AttMainPlugin.getSwfPath(this);
                }
            }
        } catch (Exception e) {
            fileUrl = null;
        }
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFdFileType() {
        return fdFileType;
    }

    public void setFdFileType(String fdFileType) {
        this.fdFileType = fdFileType;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public byte[] getFdData() {
        return fdData;
    }

    public void setFdData(byte[] fdData) {
        this.fdData = fdData;
    }

    public String getFdModelName() {
        return fdModelName;
    }

    public void setFdModelName(String fdModelName) {
        this.fdModelName = fdModelName;
    }

    public String getFdModelId() {
        return fdModelId;
    }

    public void setFdModelId(String fdModelId) {
        this.fdModelId = fdModelId;
    }

    public String getFdKey() {
        return fdKey;
    }

    public void setFdKey(String fdKey) {
        this.fdKey = fdKey;
    }

    public String getFdPositionNo() {
        return fdPositionNo;
    }

    public void setFdPositionNo(String fdPositionNo) {
        this.fdPositionNo = fdPositionNo;
    }

    public Date getFdCreateTime() {
        return fdCreateTime;
    }

    public void setFdCreateTime(Date fdCreateTime) {
        this.fdCreateTime = fdCreateTime;
    }

    public String getFdCreatorId() {
        return fdCreatorId;
    }

    public void setFdCreatorId(String fdCreatorId) {
        this.fdCreatorId = fdCreatorId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getFileNetId() {
        return fileNetId;
    }

    public void setFileNetId(String fileNetId) {
        this.fileNetId = fileNetId;
    }
}
