package cn.me.xdf.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HeadPageContoller {
	@RequestMapping(value = "getSchoolInfo")
	public String getSchoolInfo() {
		String sql = "select count(*) from sys_org_element  o where o.fd_parentid='142eac9fb5cac1c910431544a39ba22a'";// 学校数量

		String sql1 = "select count(*) from sys_user_role where person_role='guidance'"; // 导师数量

		String sql2 = "select count(distinct preteachid) from ixdf_ntp_bam_score ";// --新教师数量

		String sql3 = "select count(*) from ixdf_ntp_course c where c.isavailable='Y' ";// --课程数量
		
		Map schoolinfo=new HashMap();
		schoolinfo.put("scnum", "");
		schoolinfo.put("thnum", "");
		schoolinfo.put("mnum", "");
		schoolinfo.put("cnum", "");
		return "";
	}
}
