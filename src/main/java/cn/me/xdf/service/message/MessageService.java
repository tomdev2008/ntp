package cn.me.xdf.service.message;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.message.Message;
import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 消息service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = false)
public class MessageService extends BaseService{

	@Autowired
	private AccountService accountService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<Message> getEntityClass() {
		return Message.class;
	}
	
	@Autowired
	private MessageReplyService messageReplyService;
	
	/**
	 * 查看用户是否可以支持指定评论
	 * 
	 * @return boolean
	 */
	public boolean canSupport(String userId, String messageId){
		Message message = findUniqueByProperty("fdId", messageId);
		if(!message.getFdType().equals("01")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}
		if(messageReplyService.isSupportMessage(userId, messageId)!=null){
			return false;
		}else{
			if(message.getIsAnonymous().equals(false)){
				if(userId.equals(message.getFdUser().getFdId())){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
	}
	
	/**
	 * 查看用户是否可以反对指定评论
	 * 
	 * @return boolean
	 */
	public boolean canOppose(String userId, String messageId){
		Message message = findUniqueByProperty("fdId", messageId);
		if(!message.getFdType().equals("01")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}
		if(messageReplyService.isOpposeMessage(userId, messageId)!=null){
			return false;
		}else{
			if(message.getIsAnonymous().equals(false)){
				if(userId.equals(message.getFdUser().getFdId())){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
	}
	/**
	 * 对评论支持或反对
	 * 
	 * @return 支持数和反对数，格式：支持数_反对数（例：12_11）
	 */
	public String supportOrOpposeMessage(String userId, String messageId,String fdType){
		Message message = findUniqueByProperty("fdId", messageId);
		if(!message.getFdType().equals("01")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}
		if(fdType.equals("02")&&!canOppose(userId,messageId)){
			return "err";
		}
		if(fdType.equals("01")&&!canSupport(userId,messageId)){
			return "err";
		}
		MessageReply messageReply = new MessageReply();
		messageReply.setMessage(message);
		SysOrgPerson orgPerson = accountService.load(userId);
		messageReply.setFdUser(orgPerson);
		messageReply.setFdCreateTime(new Date());
		messageReply.setFdType(fdType);
		messageReplyService.save(messageReply);
		return getSupportCount(messageId)+"_"+getOpposeCount(messageId);
	}
	
	/**
	 * 计算指定评论的支持数
	 * 
	 * @return 支持数
	 */
	@Transactional(readOnly = true)
	public int getSupportCount(String messageId){
		Finder finder = Finder
				.create("select count(*) from MessageReply messageReply ");
		finder.append("where messageReply.message.fdId = :messageId and messageReply.fdType = :fdType");
		finder.setParam("messageId", messageId);
		finder.setParam("fdType", "01");
		List scores = find(finder);
		long obj = (Long)scores.get(0);
		return (int)obj;
	}
	/**
	 * 计算指定评论的反对数
	 * 
	 * @return 反对数
	 */
	@Transactional(readOnly = true)
	public int getOpposeCount(String messageId){
		Finder finder = Finder
				.create("select count(*) from MessageReply messageReply ");
		finder.append("where messageReply.message.fdId = :messageId and messageReply.fdType = :fdType");
		finder.setParam("messageId", messageId);
		finder.setParam("fdType", "02");
		List scores = find(finder);
		long obj = (Long)scores.get(0);
		return (int)obj;
	}
	
	/**
	 * 根据MessageId得到消息回复
	 * 
	 * @return List
	 */
	public List<MessageReply> findMessageReplysByMessageId(String messageId){
		Finder finder = Finder
				.create("from MessageReply messageReply ");
		finder.append("where messageReply.message.fdId = :messageId");
		finder.setParam("messageId", messageId);
		return messageReplyService.find(finder);
	}
	
	/**
	 * 分页查找Message
	 * 
	 * @return List
	 */
	public Pagination findCommentPage(String fdModelName,String fdModelId ,int pageNo, int pageSize){
		Finder finder = Finder
				.create("from Message message ");
		finder.append("where message.fdModelName=:fdModelName and message.fdModelId=:fdModelId and message.fdType=:fdType ");
		finder.append("order by message.fdCreateTime desc ");
		
		finder.setParam("fdModelName", fdModelName);
		finder.setParam("fdModelId", fdModelId);
		finder.setParam("fdType", Constant.MESSAGE_TYPE_REVIEW);
		List<Message> messages = (List<Message>) getPage(finder, pageNo, pageSize).getList();
		return getPage(finder, pageNo, pageSize);
	}

	/**
	 * 计算指定评论的评论数
	 * 
	 * @return 评论的评论数
	 */
	@Transactional(readOnly = true)
	public int getReplyCount(String messageId){
		Finder finder = Finder
				.create("select count(*) from MessageReply messageReply ");
		finder.append("where messageReply.message.fdId = :messageId and messageReply.fdType = :fdType");
		finder.setParam("messageId", messageId);
		finder.setParam("fdType", "03");
		List scores = find(finder);
		long obj = (Long)scores.get(0);
		return (int)obj;
	}
	
}
