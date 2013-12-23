package cn.me.xdf.service.system;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.model.system.PageConfig;
import cn.me.xdf.model.system.SysAppConfig;
import cn.me.xdf.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysPageConfigService extends BaseService {


    @Override
    public Class<PageConfig> getEntityClass() {
        return PageConfig.class;
    }
    
    public int getMaxOrder(){
    	 Finder finder=Finder.create("from PageConfig page where page.fdType='02' ");
    	 finder.append(" order by page.fdOrder  desc");
    	 List orders=find(finder);   	
    	 if(orders!=null&&orders.size()>0){
    		 return ((PageConfig)orders.get(0)).getFdOrder();
    	}
    	 return 1;
    }
    public List<PageConfig> getPageConfigs(String ptype){
    	Finder finder=Finder.create(" from PageConfig page ");
    	finder.append(" where page.fdType=:ptype");
    	finder.setParam("ptype", ptype);
    	List<PageConfig> pages=find(finder);
    	if(pages!=null&&pages.size()>0){
    		return pages;
    		
    	}
    	return null;
    }
}
