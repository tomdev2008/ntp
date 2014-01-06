package cn.me.xdf.service.letter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.letter.PrivateLetter;
import cn.me.xdf.model.letter.RelationLetter;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.BaseService;
/**
 * 私信的service
 * @author yuhuizhe
 *
 */
@Service
@Transactional(readOnly = false)
public class PrivateLetterService extends BaseService{
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RelationLetterService relationLetterService;
	
	
	/**
	 * 根据私信内容和发送对象保存私信
	 * @param body
	 * @param acceptUsesId
	 */
	public PrivateLetter saveLetter(String body,String sendUserId, String acceptUserId){
		PrivateLetter letter = new PrivateLetter();
		SysOrgPerson sendUser = accountService.get(sendUserId);
		letter.setFdCreateTime(new Date());//时间
		letter.setBody(body);//内容
		letter.setIsRead(false);//接受者未阅读
		letter.setSendUser(sendUser);//发送者
		SysOrgPerson acceptUser = accountService.get(acceptUserId);
		letter.setAcceptUser(acceptUser);//接受者
		save(letter);
		return letter;
	}
	/**
	 * 找出相关两个人之间的未读数量
	 * @param sendUserId
	 * @param acceptUsesId
	 * @return
	 */
	public Integer getunReadNum(String sendUserId,String acceptUsesId){
		Integer unReadNum = 0;
		List<PrivateLetter> list = getModelsBySendIdAndAcceptId(sendUserId,acceptUsesId);
		for (PrivateLetter privateLetter : list) {
			if(!privateLetter.getIsRead()){
				unReadNum++;
			}
		}
		return unReadNum;
	}
	/**
	 * 根据发送者id和接受者id找出私信
	 * @param sendUserId
	 * @param acceptUsesId
	 * @return
	 */
	public List<PrivateLetter> getModelsBySendIdAndAcceptId(String sendUserId,
			String acceptUsesId) {
		List<PrivateLetter> letters = findByCriteria(PrivateLetter.class,
				Value.eq("sendUser.fdId", sendUserId),
				Value.eq("acceptUser.fdId", acceptUsesId));
		return letters;
	}
	/**
	 * 通过私信的人员关系id找出私信内容
	 * @param letterId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination findLetterListByRelationId(String relationId, int pageNo, int pageSize){
		Finder finder = Finder.create("from ConnectLetter cl");
		finder.append("where cl.relationLetter.fdId =:relationId");
		finder.setParam("relationId", relationId);
		Pagination page = getPage(finder, pageNo, pageSize);
		return page;
	}
	/**
	 * 获得最新的私信内容(找出两个人最新的对话)
	 * @return
	 */
	public PrivateLetter findNewestLetter(String sendUserId, String acceptUserId){
		Finder finder = Finder.create(" from PrivateLetter t ");
		finder.append("where (t.sendUser.fdId=:sendUserId");
		finder.append("and t.acceptUser.fdId=:acceptUserId)");
		finder.append("or(t.sendUser.fdId=:acceptUserId");
		finder.append("and t.acceptUser.fdId=:sendUserId)");
		finder.setParam("sendUserId", sendUserId);
		finder.setParam("acceptUserId", acceptUserId);
		finder.append(" order by t.fdCreateTime desc");
		finder.setMaxResults(1);
		List<PrivateLetter> list = find(finder);
		return list.get(0);
	}
	/**
	 * 所有对话私信
	 * @param acceptUserId
	 * @return
	 */
	public List<PrivateLetter> findLetterList(String userId){
		Finder finder = Finder.create(" from PrivateLetter r ");
		finder.append("where r.sendUser.fdId='"+userId+"' ");
		finder.append("and r.fdId in ( select m.privateLetter.fdId from ConnectLetter m where m.privateLetter.fdId=r.fdId)");
		List<PrivateLetter> list = find(finder);
		return list;
	}
	
//	public List<RelationLetter> findLetterList(String userId){
//		Finder finder = Finder.create(" from RelationLetter r ");
//		finder.append("where r.sendUser.fdId='"+userId+"' ");
//		finder.append("and r.fdId in ( select m.relationLetter.fdId from ConnectLetter m where m.relationLetter.fdId=r.fdId)");
//		List<RelationLetter> list = find(finder);
//		return list;
//	}
	
	/**
	 * 根据发送者和接受者改变该私信是否已读状态
	 * @param sendUserId
	 * @param acceptUserId
	 */
	public void updateLetterStatus(String sendUserId, String acceptUserId){
		List<PrivateLetter> letters = getModelsBySendIdAndAcceptId(sendUserId,acceptUserId);
		for(PrivateLetter letter : letters){
			if(!letter.getIsRead()){
				letter.setIsRead(true);
				save(letter);
			}
		}
	}
	/**
	 * 根据人员id改变改人员的私信全部为已读状态
	 * @param sendUserId
	 */
	public void updateLetterStatusBySendId(String userId){
		List<PrivateLetter> letters = findByProperty("acceptUser.fdId", userId);
		for(PrivateLetter letter : letters){
			if(!letter.getIsRead()){
				letter.setIsRead(true);
				save(letter);
			}
		}
	}
	/**
	 * 找出与某人的对话(暂时留着)
	 * @param sendUserId
	 * @param acceptUserId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination findLetterList(String sendUserId, String acceptUserId, int pageNo, int pageSize){
		Finder finder = Finder.create("select t.* from ixdf_ntp_letter t ");
		finder.append("left join ixdf_ntp_letter_relation r on r.senduserid=t.senduserid");
		finder.append("where t.senduserid=:sendUserId");
		finder.setParam("sendUserId", sendUserId);
		finder.append(" and t.acceptuserid=:acceptUserId");
		finder.setParam("acceptUserId", acceptUserId);
		finder.append(" and exists(select m.privateletterid from IXDF_NTP_LETTER_CONNECT m");
		finder.append(" where m.privateletterid=t.fdId and m.relationletterid=r.fdid) ");
		finder.append("order by fdCreateTime desc");
		Pagination page = getPageBySql(finder, pageNo, pageSize);
		return page;
	}

	@Override
	@SuppressWarnings("unchecked")
	public  Class<PrivateLetter> getEntityClass() {
		return  PrivateLetter.class;
	}

}
