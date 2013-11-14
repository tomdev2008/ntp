package cn.me.xdf.service.bam.source.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.service.bam.BamCourseService;
import cn.me.xdf.service.bam.process.SourceNodeService;
import cn.me.xdf.service.bam.source.ISourceService;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.material.ExamQuestionService;
import cn.me.xdf.service.material.MaterialService;
import cn.me.xdf.service.score.ScoreService;
import cn.me.xdf.utils.ShiroUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;


/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-31
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
@Service("materialAttmainService")
public class MaterialAttMainService extends SimpleService implements ISourceService {
	@Autowired
	private SourceNodeService sourceNodeService;
	
	@Autowired
	private AttMainService attMainService;
	
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private BamCourseService bamCourseService;
	
	@Autowired
	private ScoreService scoreService;

    @Override
    public Object findSourceByMaterials(BamCourse bamCourse, CourseCatalog catalog) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object saveSourceNode(WebRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public Object findSubInfoByMaterial(WebRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findMaterialDetailInfo(BamCourse bamCourse, CourseCatalog catalog) {
		Map map = new HashMap();
		List<MaterialInfo> material = bamCourse.getMaterialByCatalog(catalog);
		List listMedia = new ArrayList();
		Map listm=new HashMap();
		Map defaultMedia = new HashMap();
		if(material!=null){
			for(MaterialInfo minfo:material){
				boolean status=minfo.getThrough();
				AttMain attMain=attMainService.getByModelIdAndModelName(minfo.getFdId(), MaterialInfo.class.getName());
				
				listm.put("id", minfo.getFdId());//素材id
				listm.put("name", minfo.getFdName());//素材名称
				listm.put("isPass", minfo.getFdDescription());//素材描述
				if(attMain!=null){
					listm.put("url", attMain.getFdId());//附件id
				}
				//defaultMedia 默认当前还没学习的内容
				if(!status&&defaultMedia.isEmpty()){
					defaultMedia.put("id", minfo.getFdId());//素材id
					defaultMedia.put("name", minfo.getFdName());//素材名称
					defaultMedia.put("intro", minfo.getFdDescription());//素材描述
					if(attMain!=null){
						defaultMedia.put("url", attMain.getFdId());//附件id
					}
					defaultMedia.put("canDownload",minfo.getIsDownload());//是否允许下载
					defaultMedia.put("dowloadCount",minfo.getFdDownloads());//下载次数
					defaultMedia.put("readCount",minfo.getFdPlays());//播放次数
					defaultMedia.put("isPass", status);
					///////////////////////////////////
					Map scorem=new HashMap();
					scorem.put("average:", 0);
					scorem.put("total:", 0);
					scorem.put("five", 0);
					scorem.put("four", 0);
					scorem.put("three", 0);
					scorem.put("two", 0);
					scorem.put("one", 0);
					defaultMedia.put("rating", scorem);
				}
				listMedia.add(listm);
			}
		}
		map.put("listMedia", listMedia);
		map.put("defaultMedia", defaultMedia);
		return map;
	}
}
