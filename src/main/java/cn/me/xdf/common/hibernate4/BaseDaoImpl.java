package cn.me.xdf.common.hibernate4;import java.io.Serializable;import java.util.List;import org.apache.commons.lang3.StringUtils;import org.hibernate.Criteria;import org.hibernate.LockOptions;import org.hibernate.Query;import org.hibernate.criterion.Criterion;import org.hibernate.criterion.Projections;import org.hibernate.criterion.Restrictions;import org.hibernate.metadata.ClassMetadata;import org.springframework.stereotype.Repository;import org.springframework.util.Assert;import cn.me.xdf.common.hibernate4.dynamic.DynamicBaseDaoImpl;import cn.me.xdf.common.utils.MyBeanUtils;import cn.me.xdf.model.base.IdEntity;/** * hibernate DAO基类 * <p/> * 提供hql分页查询，拷贝更新等一些常用功能。 *  * @author xiaobin */@Repository(value = "baseDao")public class BaseDaoImpl extends DynamicBaseDaoImpl implements BaseDao {	/**	 * @param id	 * @return 持久化对象	 * @see org.hibernate.Session get(Class,Serializable)	 */	public <T> T get(Class<T> clazz, Serializable id) {		return get(clazz, id, false);	}	@SuppressWarnings("unchecked")	public <T> T load(Class<T> clazz, Serializable id) {		Assert.notNull(id);		return (T) getSession().load(clazz, id);	}	/**	 * @param id	 *            对象ID	 * @param lock	 *            是否锁定，使用LockMode.UPGRADE	 * @return 持久化对象	 * @see org.hibernate.Session get(Class,Serializable,LockMode)	 */	@SuppressWarnings({ "unchecked" })	public <T> T get(Class<T> clazz, Serializable id, boolean lock) {		T entity;		if (lock) {			entity = (T) getSession().get(clazz, id, LockOptions.UPGRADE);		} else {			entity = (T) getSession().get(clazz, id);		}		return entity;	}	public <T> T save(T entity) {		Assert.notNull(entity);		getSession().save(entity);		return entity;	}	public <T> T update(T entity) {		Assert.notNull(entity);		getSession().update(entity);		return entity;	}	public <T> T saveOrUpdate(T entity) {		Assert.notNull(entity);		getSession().saveOrUpdate(entity);		return entity;	}	@SuppressWarnings("unchecked")	public <T> T merge(T entity) {		Assert.notNull(entity);		return (T) getSession().merge(entity);	}	public <T> void delete(T entity) {		Assert.notNull(entity);		getSession().delete(entity);	}	public <T> T deleteById(Class<T> clazz, Serializable id) {		Assert.notNull(id);		T entity = load(clazz, id);		getSession().delete(entity);		return entity;	}	/**	 * 查询唯一对象	 */	@SuppressWarnings("unchecked")	public <T> T findUnique(Finder finder) {		Query query = finder.createQuery(getSession());		return (T) query.uniqueResult();	}	/**	 * 按属性查找对象列表	 */	@SuppressWarnings("unchecked")	public <T> List<T> findByProperty(Class<T> clazz, String property,			Object value) {		Assert.hasText(property);		if (StringUtils.isNotBlank(property)) {			return createCriteria(clazz, null, Restrictions.eq(property, value))					.list();		}		throw new RuntimeException(clazz + " property is null");	}	/**	 * 按属性查找唯一对象	 */	@SuppressWarnings("unchecked")	public <T> T findUniqueByProperty(Class<T> clazz, String property,			Object value) {		Assert.hasText(property);		Assert.notNull(value);		return (T) createCriteria(clazz, null, Restrictions.eq(property, value))				.uniqueResult();	}	/**	 * 按属性统计记录数	 * 	 * @param property	 * @param value	 * @return	 */	public <T> int countByProperty(Class<T> clazz, String property, Object value) {		Assert.hasText(property);		Assert.notNull(value);		return ((Number) (createCriteria(clazz, null,				Restrictions.eq(property, value)).setProjection(				Projections.rowCount()).uniqueResult())).intValue();	}	/**	 * 按Criterion查询列表数据.	 * 	 * @param criterion	 *            数量可变的Criterion.	 */	@SuppressWarnings("unchecked")	public <T> T findUniqueByProperty(Class<T> clazz, Criterion... criterion) {		return (T) createCriteria(clazz, null, criterion).uniqueResult();	}	/**	 * 按Criterion查询列表数据.	 * 	 * @param criterion	 *            数量可变的Criterion.	 */	@SuppressWarnings("unchecked")	public <T> List<T> findByCriteria(Class<T> clazz, Criterion... criterion) {		return createCriteria(clazz, null, criterion).list();	}	/**	 * 按Criterion查询列表数据.	 * 	 * @param criterion	 *            数量可变的Criterion.	 */	@SuppressWarnings("unchecked")	public <T> List<T> findByCriteria(Class<T> clazz, OrderBy[] order,			Criterion... criterion) {		return createCriteria(clazz, order, criterion).list();	}	/**	 * 按Criterion查询列表数据.	 * 	 * @param clazz	 * @param maxCount	 * @param criterion	 * @param <T>	 * @return	 */	@SuppressWarnings("unchecked")	public <T> List<T> findByCriteria(Class<T> clazz, int maxCount,			Criterion... criterion) {		return createCriteria(clazz, null, maxCount, criterion).list();	}	/**	 * 通过Updater更新对象	 * 	 * @param updater	 * @return	 */	@SuppressWarnings("unchecked")	public <T> T updateByUpdater(Updater<T> updater) {		ClassMetadata cm = sessionFactory.getClassMetadata(updater.getBean()				.getClass());		if (!(updater.getBean() instanceof IdEntity)) {			throw new RuntimeException("not support :"					+ updater.getBean().getClass());		}		Serializable id = ((IdEntity) updater.getBean()).getFdId();		T po = (T) getSession().get(updater.getBean().getClass(), id);		updaterCopyToPersistentObject(updater, po, cm);		return po;	}	/**	 * 将更新对象拷贝至实体对象，并处理many-to-one的更新。	 * 	 * @param updater	 * @param po	 */	private <T> void updaterCopyToPersistentObject(Updater<T> updater, T po,			ClassMetadata cm) {		String[] propNames = cm.getPropertyNames();		String identifierName = cm.getIdentifierPropertyName();		T bean = updater.getBean();		Object value = null;		for (String propName : propNames) {			if (propName.equals(identifierName)) {				continue;			}			try {				value = MyBeanUtils.getSimpleProperty(bean, propName);				if (!updater.isUpdate(propName, value)) {					continue;				}				cm.setPropertyValue(po, propName, value);			} catch (Exception e) {				throw new RuntimeException(e);			}		}	}	/**	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.	 */	protected <T> Criteria createCriteria(Class<T> clazz, OrderBy[] orders,			Criterion... criterions) {		Criteria criteria = getSession().createCriteria(clazz);		for (Criterion c : criterions) {			criteria.add(c);		}		if (orders != null) {			for (OrderBy order : orders) {				criteria.addOrder(order.getOrder());			}		}		return criteria;	}	protected <T> Criteria createCriteria(Class<T> clazz, OrderBy[] orders,			int maxCount, Criterion... criterions) {		Criteria criteria = getSession().createCriteria(clazz);		for (Criterion c : criterions) {			criteria.add(c);		}		if (orders != null) {			for (OrderBy order : orders) {				criteria.addOrder(order.getOrder());			}		}		criteria.setMaxResults(maxCount);		return criteria;	}	public BaseDaoImpl() {	}	/*	 * @Autowired private ProceeListener listener;	 * 	 * @PostConstruct public void registerListeners() { final	 * EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)	 * .getServiceRegistry().getService(EventListenerRegistry.class);	 * registry.getEventListenerGroup	 * (EventType.POST_UPDATE).appendListener(listener); }	 */}