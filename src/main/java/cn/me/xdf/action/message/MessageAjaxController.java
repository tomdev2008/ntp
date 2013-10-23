package cn.me.xdf.action.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.service.message.MessageReplyService;
import cn.me.xdf.service.message.MessageService;

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
	
	/**
	 * 支持或反对评论
	 * 
	 * @return 支持数和反对数，格式：支持数_反对数（例：12_11）
	 */
	@RequestMapping(value = "supportOrOpposeMessage")
	@ResponseBody
	public String supportOrOpposeMessage(String userId, String messageId,String fdType) {
		return messageService.supportOrOpposeMessage(userId, messageId, fdType);
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
	 * @return String(true:能；false：不能)
	 */
	@RequestMapping(value = "canSupportOrOppose")
	@ResponseBody
	public String canSupportOrOppose(String userId, String messageId) {
		return messageService.canSupportOrOppose(userId, messageId)+"";
	}
}
