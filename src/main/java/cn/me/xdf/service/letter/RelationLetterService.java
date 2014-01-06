package cn.me.xdf.service.letter;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.hibernate4.Value;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.letter.RelationLetter;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.BaseService;
@Service
@Transactional(readOnly = false)
public class RelationLetterService extends BaseService{
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 找出我的私信列表
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination findLetterList(String sendUserId, Integer pageNo, Integer pageSize){
		Finder finder = Finder.create(" from RelationLetter r ");
		finder.append("where r.sendUser.fdId='"+sendUserId+"' ");
		finder.append("and r.fdId in ( select m.relationLetter.fdId from ConnectLetter m where m.relationLetter.fdId=r.fdId)");
		Pagination page = getPage(finder, pageNo, pageSize);
		return page;
	}
	
	/**
	 * 保存私信的关系表
	 * @param sendUserId
	 * @param acceptUsesId
	 * @param letterId
	 */
	public RelationLetter saveRelationLetter(String sendUserId,String acceptUsesId){
		RelationLetter relationLetter = getModelByPersonId(sendUserId, acceptUsesId);
		if(relationLetter==null){
			SysOrgPerson acceptUser = accountService.get(acceptUsesId);
			SysOrgPerson sendUser = accountService.get(sendUserId);
			relationLetter = new RelationLetter();
			relationLetter.setAcceptUser(acceptUser);
			relationLetter.setSendUser(sendUser);
			save(relationLetter);
		}
		return relationLetter;
	}
	/**
	 * 根据发送者id和接受者id找两人私信关系表
	 * @param sendUserId
	 * @param acceptUsesId
	 * @return
	 */
	public RelationLetter getModelByPersonId(String sendUserId,
			String acceptUserId) {
		List<RelationLetter> relations = findByCriteria(RelationLetter.class,
				Value.eq("sendUser.fdId", sendUserId),
				Value.eq("acceptUser.fdId", acceptUserId));
		if (CollectionUtils.isNotEmpty(relations)) {
			return relations.get(0);
		}
		return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public  Class<RelationLetter> getEntityClass() {
		return  RelationLetter.class;
	}
}
