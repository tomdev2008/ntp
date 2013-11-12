package cn.me.xdf.service.bam.process;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.SimpleService;
import cn.me.xdf.utils.ShiroUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-6
 * Time: 下午2:46
 * 素材学习记录
 */
@Service
@Transactional(readOnly = true)
public class SourceNodeService extends SimpleService {

    @Transactional(readOnly = false)
    public Object saveSourceNode(SourceNote sourceNote) {
        return this.save(sourceNote);
    }
    
    
    @Transactional(readOnly = true)
    public SourceNote getSourceNote(String materialId,String catalogId,String userId) {
    	Finder finder = Finder
				.create("from SourceNote sourceNote ");
		finder.append("where sourceNote.fdCatalogId=:fdCatalogId and sourceNote.fdMaterialId=:fdMaterialId and fdUserId=:fdUserId ");
		finder.append("order by sourceNote.fdOperationDate desc");
		finder.setParam("fdCatalogId", catalogId);
		finder.setParam("fdMaterialId", materialId);
		finder.setParam("fdUserId", ShiroUtils.getUser().getId());
		return (SourceNote) getPage(finder, 1, 1).getList().get(0);
    }
    
}
