package cn.me.xdf.action.adviser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/adviser")
@Scope("request")
public class AdviserController {
	
	/**
	 * 返回到时批改作业页面
	 * @return
	 */
	@RequestMapping(value = "checkTask")
	public String checkTask(){
		
		return "/adviser/checkTask";
	}

}
