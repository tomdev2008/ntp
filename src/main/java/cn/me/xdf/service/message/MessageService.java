package cn.me.xdf.service.message;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.message.Message;
import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.score.Score;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.service.ShiroDbRealm.ShiroUser;
import cn.me.xdf.utils.ShiroUtils;

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

	@SuppressWarnings("unchecked")
	@Override
	public  Class<Message> getEntityClass() {
		return Message.class;
	}
	
	@Autowired
	private MessageReplyService messageReplyService;
	
	/**
	 * 查看用户是否可以支持或反对指定评论
	 * 
	 * @return boolean
	 */
	public boolean canSupportOrOppose(String userId, String messageId){
		Message message = findUniqueByProperty("fdId", messageId);
		if(!message.getFdType().equals("01")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}
		if(messageReplyService.isContainMessageReply(userId, messageId)!=null){
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
		if(!canSupportOrOppose(userId,messageId)){
			throw new RuntimeException("不能支持或反对");
		}
		MessageReply messageReply = new MessageReply();
		messageReply.setMessage(message);
		SysOrgPerson orgPerson = new SysOrgPerson();
		orgPerson.setFdId(userId);
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
	
	
	
	
}
