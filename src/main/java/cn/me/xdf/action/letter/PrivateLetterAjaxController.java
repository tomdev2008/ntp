package cn.me.xdf.action.letter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.common.page.SimplePage;
import cn.me.xdf.common.utils.array.ArrayUtils;
import cn.me.xdf.common.utils.array.SortType;
import cn.me.xdf.model.letter.ConnectLetter;
import cn.me.xdf.model.letter.PrivateLetter;
import cn.me.xdf.model.letter.RelationLetter;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.letter.ConnectLetterService;
import cn.me.xdf.service.letter.PrivateLetterService;
import cn.me.xdf.service.letter.RelationLetterService;
import cn.me.xdf.utils.ShiroUtils;

@Controller
@RequestMapping(value = "/ajax/letter")
@Scope("request")
public class PrivateLetterAjaxController {

	@Autowired
	private PrivateLetterService privateLetterService;
	
	@Autowired
	private RelationLetterService relationLetterService;
	
	@Autowired
	private ConnectLetterService connectLetterService;
	
	/**
	 * 找出详细对话信息
	 * @param request
	 */
	@RequestMapping(value="findDetailLetter")
	@ResponseBody
	public void findDetailLetter(HttpServletRequest request){
		String sendUserId = ShiroUtils.getUser().getId();
		String acceptUserId = request.getParameter("fdId");
		String pageNoStr = request.getParameter("pageNo");
		RelationLetter relationLetter = relationLetterService.getModelByPersonId(sendUserId, acceptUserId);
		SimpleDateFormat sdf=new SimpleDateFormat("h:m a");
	    int pageNo = 1;
	    if(StringUtil.isNotBlank(pageNoStr)){
	    	pageNo = NumberUtils.createInteger(pageNoStr);
	    }
		Pagination page = privateLetterService.findLetterListByRelationId
				(relationLetter.getFdId(), pageNo, SimplePage.DEF_COUNT);//找出私信分页
		List<Map> list = new ArrayList<Map>();
		List<ConnectLetter> connectLetters = (List<ConnectLetter>) page.getList();
		for (ConnectLetter connectLetter : connectLetters) {
			Map map = new HashMap();
			PrivateLetter letter = connectLetter.getPrivateLetter();
			SysOrgPerson acceptUser = letter.getAcceptUser();
			SysOrgPerson sendUser = letter.getSendUser();
			map.put("id", letter.getFdId());
			if(sendUser.getFdId().equals(sendUserId)){
				map.put("isMe", true);
			}else{
				map.put("isMe", false);
			}
			map.put("msg", letter.getBody());
			map.put("time", sdf.format(letter.getFdCreateTime()));
			
		}
/*		List<Map> map = new ArrayList<Map>();
		ArrayUtils.sortListByProperty(map, "time", SortType.LOW);*/ 	
		
	}
	
	@RequestMapping(value="findLetterList")
	@ResponseBody
	public String findLetterList(HttpServletRequest request,Model model){
		String pageNoStr = request.getParameter("pageNo");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd h:m:s a");
	    int pageNo = 1;
	    if(StringUtil.isNotBlank(pageNoStr)){
	    	pageNo = NumberUtils.createInteger(pageNoStr);
	    }
		Pagination page = relationLetterService.findLetterList(
				ShiroUtils.getUser().getId(),pageNo,SimplePage.DEF_COUNT);
		List<RelationLetter> list = (List<RelationLetter>) page.getList();
		Map data =  new HashMap();
		List<Map> listMsgData = new ArrayList<Map>();
		for (RelationLetter relationLetter : list) {
			Map map = new HashMap();
			SysOrgPerson acceptUser = relationLetter.getAcceptUser();
			SysOrgPerson sendUser = relationLetter.getSendUser();
			PrivateLetter letter = privateLetterService.findNewestLetter(
					sendUser.getFdId(), acceptUser.getFdId());
			map.put("id", letter.getFdId());
			map.put("hasUnread", !letter.getIsRead());
			if(!letter.getIsRead()){
				map.put("numUnread", privateLetterService.getunReadNum(sendUser.getFdId(), acceptUser.getFdId()));
			}
			map.put("numTotal", 200);
			map.put("msg", letter.getBody());
			map.put("timeMsg", sdf.format(letter.getFdCreateTime()));
			Map user = new HashMap();
			user.put("imgUrl", acceptUser.getPoto());
			user.put("name", acceptUser.getFdName());
			user.put("mail", acceptUser.getFdEmail());
			user.put("org", acceptUser.getHbmParentOrg().getFdName());
			user.put("department", acceptUser.getDeptName());
			map.put("user", user);
			listMsgData.add(map);
		}
		Map paging = new HashMap();
		paging.put("totalPage", page.getTotalPage());
		paging.put("currentPage", page.getPageNo());
		paging.put("totalCount", page.getTotalCount());
		paging.put("StartPage", page.getStartPage());
		paging.put("EndPage",page.getEndPage());
		paging.put("StartOperate", page.getStartOperate());
		paging.put("EndOperate", page.getEndOperate());
		paging.put("startNum", page.getStartNum());
		paging.put("endNum", page.getEndNum());
		data.put("listMsgData", listMsgData);
		data.put("paging",paging);
		return JsonUtils.writeObjectToJson(data);
	}
	/**
	 * 保存私信
	 * @return
	 */
	@RequestMapping(value="saveLetter", method = RequestMethod.POST)
	@ResponseBody
	public void saveLetter(HttpServletRequest request){
		String body = request.getParameter("body");
		String acceptUsesId = request.getParameter("fdId");
		PrivateLetter letter = privateLetterService.saveLetter(body,ShiroUtils.getUser().getId(), acceptUsesId);
		RelationLetter relationLetter = relationLetterService
				  .getModelByPersonId(ShiroUtils.getUser().getId(), acceptUsesId);
		if(relationLetter==null){
		   relationLetter = relationLetterService.saveRelationLetter(ShiroUtils.getUser().getId(),acceptUsesId);
		}
		saveConnect(letter,relationLetter);
		RelationLetter resverRelation = relationLetterService
				  .getModelByPersonId(acceptUsesId, ShiroUtils.getUser().getId());
		if(resverRelation==null){
			resverRelation = relationLetterService.saveRelationLetter
					(acceptUsesId,ShiroUtils.getUser().getId());
		}
		saveConnect(letter,resverRelation);
		
	}
	private void saveConnect(PrivateLetter letter,RelationLetter relationLetter){
		ConnectLetter connect = new ConnectLetter();
		connect.setPrivateLetter(letter);
		connect.setRelationLetter(relationLetter);
		connectLetterService.save(connect);
	}
	
	
	/**
	 * 删除单个私信
	 * @param request
	 */
	@RequestMapping(value="deleteSingleLetter")
	@ResponseBody
	public void deleteSingleLetter(HttpServletRequest request){
		String letterId = request.getParameter("letterId");
		String acceptUserId = request.getParameter("letterId");
		String sendUserId = ShiroUtils.getUser().getId();
		deleteConectLetter(sendUserId,acceptUserId,letterId);
	}
	
	private void deleteConectLetter(String sendUserId ,String acceptUserId,String letterId){
		RelationLetter relationLetter = relationLetterService.
				  getModelByPersonId(sendUserId,acceptUserId);
		ConnectLetter connect = connectLetterService.
				  getModelsByletterIdAndRelationId(letterId,relationLetter.getFdId());
		connectLetterService.delete(connect.getFdId());
		List<PrivateLetter> letters = privateLetterService.
				getModelsBySendIdAndAcceptId(sendUserId,acceptUserId);
		if (CollectionUtils.isEmpty(letters)) {
			relationLetterService.delete(relationLetter.getFdId());//删掉私信关系
		}
	}
	
	/**
	 * 清除当前用户与某个人的私信
	 */
	@RequestMapping(value="deleteLetterByUserId")
	@ResponseBody
	public void deleteLetterByUserId(HttpServletRequest request){
		String sendUserId = ShiroUtils.getUser().getId();
		String acceptUserId = request.getParameter("fdId");
		RelationLetter relationLetter = relationLetterService
				              .getModelByPersonId(sendUserId,acceptUserId);
		deleteLetterByRelation(relationLetter);
	}
	/**
	 * 删除我的所有私信
	 */
	@RequestMapping(value="deleteAllLetter")
	@ResponseBody
	public void deleteAllLetter(){
		String userId = ShiroUtils.getUser().getId();
		List<RelationLetter> list = relationLetterService.findByProperty("sendUser.fdId", userId);
		for(RelationLetter relation:list){
			deleteLetterByRelation(relation);
		}
	}
	private void deleteLetterByRelation(RelationLetter relationLetter){
		List<ConnectLetter> connects = connectLetterService
	            .findByProperty("relationLetter.fdId", relationLetter.getFdId());
		for(ConnectLetter connect:connects){
			connectLetterService.delete(connect.getFdId());
		}
		relationLetterService.delete(relationLetter.getFdId());//删掉私信关系
	}
	
	/**
	 * 得到总共多少未读私信
	 * @return
	 */
	@RequestMapping(value="getUnReadNum")
	@ResponseBody
	public Integer getUnReadNum(){
		if (ShiroUtils.getUser() == null) {
            return 0;
        }
		Integer unReadNum = 0;
		List<PrivateLetter> list = privateLetterService.findLetterList(ShiroUtils.getUser().getId());
		for (PrivateLetter letter : list) {
			if(!letter.getIsRead()){
				unReadNum++;
			}
		}
		return unReadNum;
	}
	
	/**
	 * 得到总共多少私信
	 * @return
	 */
	@RequestMapping(value="getTotalNum")
	@ResponseBody
    public Integer getTotalNum(){
		List<PrivateLetter> list = privateLetterService.findLetterList(ShiroUtils.getUser().getId());
		return list.size();
	}
}
