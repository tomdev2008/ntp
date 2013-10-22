package cn.me.xdf.service.message;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.service.BaseService;

/**
 * 
 * 消息回复service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = false)
public class MessageReplyService extends BaseService{

	@SuppressWarnings("unchecked")
	@Override
	public  Class<MessageReply> getEntityClass() {
		return MessageReply.class;
	}
	
	/**
	 * 查看用户是否支持或反对过指定评论
	 * 
	 * @return MessageReply
	 */
	public MessageReply isContainMessageReply(String userId, String messageId){
		Finder finder = Finder
				.create("from MessageReply messageReply ");
		finder.append("where messageReply.message.fdId=:messageId and messageReply.fdUser.fdId=:userId  and (messageReply.fdType=:fdType1 or messageReply.fdType=:fdType2)");
		finder.setParam("messageId", messageId);
		finder.setParam("userId", userId);
		finder.setParam("fdType1", "01");
		finder.setParam("fdType2", "02");
		List<MessageReply> scores = find(finder);
		return scores.get(0);
	}
	
	
	
	
	
	
	
	
	
	
	
}
