package cn.me.xdf.action.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.service.message.MessageService;

/**
 * 消息
 * 
 * @author
 * 
 */
@Controller
@RequestMapping(value = "/message")
@Scope("request")
public class MessageController {

	@Autowired
	private MessageService messageService ;
	
	/**
	 * 支持或反对评论
	 * 
	 * @return 支持数和反对数，格式：支持数_反对数（例：12_11）
	 */
	@RequestMapping(value = "ajax/supportOrOpposeMessage")
	@ResponseBody
	public String supportOrOpposeMessage(String userId, String messageId,String fdType) {
		return messageService.supportOrOpposeMessage(userId, messageId, fdType);
	}
	
	/**
	 * 根据消息ID删除消息信息
	 * 
	 */
	@RequestMapping(value = "ajax/deleteMessageById")
	@ResponseBody
	public void deleteMessageById(String fdid) {
		messageService.delete(fdid);
	}
	
	/**
	 * 查看用户是否可以支持或反对评论
	 * 
	 * @return String(true:能；false：不能)
	 */
	@RequestMapping(value = "ajax/canSupportOrOppose")
	@ResponseBody
	public String canSupportOrOppose(String userId, String messageId) {
		return messageService.canSupportOrOppose(userId, messageId)+"";
	}
}
