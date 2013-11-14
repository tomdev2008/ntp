package cn.me.xdf.action.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.common.json.JsonUtils;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseInfo;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.message.Message;
import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.message.MessageReplyService;
import cn.me.xdf.service.message.MessageService;
import cn.me.xdf.utils.DateUtil;
import cn.me.xdf.utils.ShiroUtils;

/**
 * 消息
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/ajax/message")
@Scope("request")
public class MessageAjaxController {

	@Autowired
	private MessageService messageService ;
	
	@Autowired
	private MessageReplyService messageReplyService ;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 支持或反对评论
	 * 
	 * @return 支持数和反对数，格式：支持数_反对数（例：12_11）
	 */
	@RequestMapping(value = "supportOrOpposeMessage")
	@ResponseBody
	public String supportOrOpposeMessage(String messageId,String fdType) {
		return messageService.supportOrOpposeMessage(ShiroUtils.getUser().getId(), messageId, fdType);
	}
	
	/**
	 * 根据消息ID删除消息信息(先删去消息的全部回复信息，再删除消息)
	 * 
	 */
	@RequestMapping(value = "deleteMessageById")
	@ResponseBody
	public void deleteMessageById(String messageId) {
		List<MessageReply> list = messageService.findMessageReplysByMessageId(messageId);
		System.out.println(list.size());
		for (MessageReply messageReply : list) {
			messageReplyService.delete(messageReply);
		}
		messageService.delete(messageId);
	}
	
	/**
	 * 查看用户是否可以支持或反对评论
	 * 
	 * @return String(true:能；supportOrOpposeed：评论过了；isme：自己的)
	 */
	@RequestMapping(value = "canSupportOrOppose")
	@ResponseBody
	public String canSupportOrOppose(String messageId) {
		String userId = ShiroUtils.getUser().getId();
		if(messageReplyService.isContainMessageReply(userId, messageId)!=null){
			return "supportOrOpposeed";
		}else{
			if(messageService.canSupportOrOppose(userId, messageId)){
				return "true";
			}else{
				return "isme";
			}
		}
	}
	
	/**
	 * 添加评论
	 * 
	 */
	private void addMessage(String fdModelName,String fdModelId,String fdContent,String fdType,boolean isAnonymous,String userId) {
		Message message = new Message();
		message.setFdModelName(fdModelName);
		message.setFdModelId(fdModelId);
		message.setFdContent(fdContent);
		message.setFdCreateTime(new Date());
		message.setFdType(fdType);
		message.setIsAnonymous(isAnonymous);
		message.setFdUser((SysOrgPerson)accountService.load(userId));
		messageService.save(message);
	}
	
	/**
	 * 添加评论(针对课程，当前用户)
	 * 
	 */
	@RequestMapping(value = "addCourseMessage")
	@ResponseBody
	private void addCourseMessage(String courseId,String fdContent) {
		addMessage(CourseInfo.class.getName(), courseId, fdContent, Constant.MESSAGE_TYPE_REVIEW, false, ShiroUtils.getUser().getId());
	}
	
	/**
	 * 添加评论(针对资源，当前用户)
	 * 
	 */
	@RequestMapping(value = "addMaterialMessage")
	@ResponseBody
	private void addMaterialMessage(String materialId,String fdContent,String isAnonymity) {
		addMessage(MaterialInfo.class.getName(), materialId, fdContent, Constant.MESSAGE_TYPE_REVIEW, false, ShiroUtils.getUser().getId());
	}
	
	/**
	 * 根据Id评论分页信息
	 * 
	 */
	@RequestMapping(value = "findCommentByModelId")
	@ResponseBody
	private String findCommentByModelId(String modelName,String modelId,int pageNo,int pageSize) {
		
		Pagination pagination =messageService.findCommentPage(modelName, modelId, pageNo, pageSize);
		List<Message> messages = (List<Message>) pagination.getList();
		List<Map> list = new ArrayList<Map>();
		for (int i=0;i<messages.size();i++){
			Message message =messages.get(i);
			Map map = new HashMap();
			map.put("fdId", message.getFdId());
			map.put("content", message.getFdContent());
			map.put("isAnonymous", message.getIsAnonymous());
			map.put("fdCreateTime", DateUtil.getInterval(DateUtil.convertDateToString(message.getFdCreateTime()), "yyyy/MM/dd hh:mm aa"));
			map.put("fdUserName", message.getFdUser().getRealName());
			map.put("fdUserURL", message.getFdUser().getFdPhotoUrl());
			map.put("fdUserEmail", message.getFdUser().getFdEmail());
			map.put("fdUserDept", message.getFdUser().getDeptName());
			map.put("supportCount", messageService.getSupportCount(message.getFdId()));
			map.put("opposeCount", messageService.getOpposeCount(message.getFdId()));
			map.put("replyCount", messageService.getReplyCount(message.getFdId()));
			int no = pagination.getTotalCount()-i-(pageNo-1)*pageSize;
			map.put("no", no);
			list.add(map);
		}
		Map maps = new HashMap();
		maps.put("listComments", list);
		return JsonUtils.writeObjectToJson(maps);
	}
	
	/**
	 * 根据课程Id查找分页信息
	 * 
	 */
	@RequestMapping(value = "getCommentPageInfo")
	@ResponseBody
	private String getCommentPageInfo(String modelName , String modelId,int pageNo,int pageSize) {
		Pagination pagination = messageService.findCommentPage(modelName, modelId, pageNo, pageSize);
		int totalSize = pagination.getTotalCount();
		int startLine = (pageNo-1)*(pageSize)+1;
		int totalPage = pagination.getTotalPage();
		int endLine = 0;
		if(totalPage==pageNo){
			endLine = startLine + totalSize%pageSize-1;
		}else{
			endLine = startLine + pageSize-1;
		}
		Map map = new HashMap();
		map.put("totalSize", totalSize);
		map.put("pageNo", pageNo);
		map.put("pageSize", pageSize);
		map.put("startLine",startLine);
		map.put("endLine", endLine);
		map.put("totalPage", totalPage);
		return JsonUtils.writeObjectToJson(map);
	}

}
