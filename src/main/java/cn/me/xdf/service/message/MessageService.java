package cn.me.xdf.service.message;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.common.hibernate4.Finder;
import cn.me.xdf.common.page.Pagination;
import cn.me.xdf.model.bam.BamCourse;
import cn.me.xdf.model.base.Constant;
import cn.me.xdf.model.course.CourseCatalog;
import cn.me.xdf.model.material.MaterialInfo;
import cn.me.xdf.model.message.Message;
import cn.me.xdf.model.message.MessageReply;
import cn.me.xdf.model.organization.SysOrgPerson;
import cn.me.xdf.model.process.SourceNote;
import cn.me.xdf.service.AccountService;
import cn.me.xdf.service.BaseService;
import cn.me.xdf.service.bam.BamCourseService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * 消息service
 * 
 * @author zhaoq
 * 
 */
@Service
@Transactional(readOnly = false)
public class MessageService extends BaseService implements InitializingBean{

	private static final Logger LOGER = LoggerFactory
            .getLogger(MessageService.class);
	
	/**
     * 模板缓存
     */
    protected Map<String, Template> templateCache;
    
	@Autowired
	private AccountService accountService;
	
	@SuppressWarnings("unchecked")
	@Override
	public  Class<Message> getEntityClass() {
		return Message.class;
	}
	
	@Autowired
	private MessageReplyService messageReplyService;
	
	protected MessageAssembleBuilder messageAssembleBuilder;

    @Autowired
    public void setMessageAssembleBuilder(
			MessageAssembleBuilder messageAssembleBuilder) {
		this.messageAssembleBuilder = messageAssembleBuilder;
	}
	
    @Autowired
    private BamCourseService bamCourseService;
    
	/**
	 * 查看用户是否可以支持指定评论
	 * 
	 * @return boolean
	 */
	public boolean canSupport(String userId, String messageId){
		Message message = findUniqueByProperty("fdId", messageId);
		/*if(!message.getFdType().equals("01")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}*/
		if(message.getFdType().equals(Constant.MESSAGE_TYPE_SYS)){
			return true;
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
		/*if(!message.getFdType().equals("01")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}*/
		if(message.getFdType().equals(Constant.MESSAGE_TYPE_SYS)){
			return true;
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
		/*if(!message.getFdType().equals("01")&&!message.getFdType().equals("04")){
			throw new RuntimeException("只有评论消息才能支持或反对");
		}*/
		if(fdType.equals("02")&&!canOppose(userId,messageId)){
			return "cannot";
		}
		if(fdType.equals("01")&&!canSupport(userId,messageId)){
			return "cannot";
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
		finder.append("where message.fdModelName=:fdModelName and message.fdModelId=:fdModelId and (message.fdType=:fdType1 or message.fdType=:fdType2) ");
		finder.append("order by message.fdCreateTime desc ");
		
		finder.setParam("fdModelName", fdModelName);
		finder.setParam("fdModelId", fdModelId);
		finder.setParam("fdType1", Constant.MESSAGE_TYPE_REVIEW);
		finder.setParam("fdType2", Constant.MESSAGE_TYPE_REPLY);
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
	
	/**
	 * 保存课程学习过程中系统发的课程通过消息
	 * @param bamCourse 进程
	 */
	public void saveCourseMessage(BamCourse bamCourse){
		Map<String, String> param = new HashMap<String, String>();
		param.put("courseName", bamCourse.getCourseInfo().getFdTitle());
		param.put("link", "getCertificate?bamId="+bamCourse.getFdId());
		saveSysMessage(bamCourse.getFdId(),"source",param);
	}
	
	/**
	 * 保存课程学习过程中系统发的节通过消息
	 * @param bamCourse 进程
	 * @param catalog 节
	 */
	public void saveLectureMessage(BamCourse bamCourse,CourseCatalog catalog){
		Map<String, String> param = new HashMap<String, String>();
		param.put("lectureNo", catalog.getFdNo().toString());
		param.put("lectureName", catalog.getFdName());
		saveSysMessage(bamCourse.getFdId(),"lecture",param);
	}
	
	/**
	 * 保存课程学习过程中系统发的素材通过消息
	 * @param bamCourse 进程
	 * @param catalog 节
	 * @param material 素材
	 */
	public void saveMaterialMessage(BamCourse bamCourse,CourseCatalog catalog,MaterialInfo material){
		Map<String, String> param = new HashMap<String, String>();
		param.put("lectureNo", catalog.getFdNo().toString());
		param.put("lectureName", catalog.getFdName());
		param.put("materialName", material.getFdName());
		String type = catalog.getFdMaterialType();
		if(Constant.MATERIAL_TYPE_TEST.equals(type) || Constant.MATERIAL_TYPE_JOBPACKAGE.equals(type)){
			param.put("ispass", "已通过");
		}
		saveSysMessage(bamCourse.getFdId(),catalog.getMaterialType(),param);
	}
	
	/**
	 * 保存课程学习过程中系统发的素材通过消息
	 * @param bamCourse 进程
	 * @param catalog 节
	 * @param material 素材
	 */
	public void saveMaterialMessage(SourceNote note){
		BamCourse bamCourse = bamCourseService.getCourseByUserIdAndCourseId(note.getFdUserId(), note.getFdCourseId());
		CourseCatalog catalog = bamCourse.getCatalogById(note.getFdCatalogId());
		MaterialInfo material = bamCourse.getMaterialInfoById(note.getFdCatalogId(),note.getFdMaterialId());
		Map<String, String> param = new HashMap<String, String>();
		param.put("lectureNo", catalog.getFdNo().toString());
		param.put("lectureName", catalog!=null?catalog.getFdName():"");
		param.put("materialName", material!=null?material.getFdName():"");
		if(note.getIsStudy()!=null){
			if(!note.getIsStudy()){
				param.put("ispass", "未通过");
			}else if(note.getIsStudy()){
				param.put("ispass", "已通过");
			}
		}
		else{
			if(Constant.TASK_STATUS_FINISH.equals(note.getFdStatus())){
				param.put("ispass", "已提交");
			}
		}
		saveSysMessage(bamCourse.getFdId(),catalog.getMaterialType(),param);
	}
	
	/**
	 * 保存课程学习过程中系统发的通过消息
	 * @param bamId 进程ID
	 * @param name 消息配置中的name
	 * @param parameters 消息配置中填入的参数 
	 */
	public void saveSysMessage(String bamId,final String name,final Map<String, ?> parameters){
		Template template = templateCache.get(name);
        String content = processTemplate(template, parameters);
        Message message = new Message();
        message.setFdType(Constant.MESSAGE_TYPE_SYS);
        message.setFdContent(content);
        message.setFdModelId(bamId);
        message.setFdModelName(BamCourse.class.getName());
        message.setFdCreateTime(new Date());
        super.save(message);
	}
	
	@Override
    public void afterPropertiesSet() throws Exception {
		templateCache = new ConcurrentHashMap<String, Template>();
        messageAssembleBuilder.init();
        Map<String, String> message = messageAssembleBuilder.getMessages();
        Configuration configuration = new Configuration();
        configuration.setNumberFormat("#");
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        for (Entry<String, String> entry : message.entrySet()) {
            stringLoader.putTemplate(entry.getKey(), entry.getValue());
            templateCache.put(entry.getKey(),new Template(entry.getKey(),
                                            new StringReader(entry.getValue()),
                                            configuration));
        }
        configuration.setTemplateLoader(stringLoader);
    }

    protected String processTemplate(Template template,
                                     Map<String, ?> parameters) {
        StringWriter stringWriter = new StringWriter();
        try {
        	template.process(parameters, stringWriter);
        } catch (Exception e) {
            LOGER.error("处理系统消息参数模板时发生错误：{}", e.toString());
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }
    
    public void deleteMessage(String messageId){
    	Message message = get(messageId);
    	if(message!=null){
    		//删除MessageReply
        	List<MessageReply> list = messageReplyService.findByProperty("message.fdId", messageId);
        	for (MessageReply message2 : list) {
        		messageReplyService.delete(message2.getFdId());
			}
        	delete(messageId);
        	if(message.getFdType().equals(Constant.MESSAGE_TYPE_REPLY)){
        		messageReplyService.delete(messageId);
        	}
    	}
    }
    
    
    
    
    
    
    
    
    
	
}
