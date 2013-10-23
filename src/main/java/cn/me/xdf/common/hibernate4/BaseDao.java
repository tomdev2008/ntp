package cn.me.xdf.common.hibernate4;import java.io.Serializable;import java.util.List;import java.util.Map;import org.hibernate.criterion.Criterion;import cn.me.xdf.common.hibernate4.dynamic.DynamicBaseDao;import cn.me.xdf.common.page.Pagination;/** *  * @author xiaobin *  */public interface BaseDao extends DynamicBaseDao {	/**	 * 通过ID查找对象	 * 	 * @param id	 *            记录的ID	 * @param lock	 *            是否锁定对象	 * @return 实体对象	 */	<T> T get(Class<T> clazz, Serializable id, boolean lock);	/**	 * 通过ID查找对象,不锁定对象	 * 	 * @param id	 *            记录的ID	 * @return 实体对象	 */	<T> T load(Class<T> clazz, Serializable id);	<T> T get(Class<T> clazz, Serializable id);	/**	 * 保存对象	 * 	 * @param entity	 *            实体对象	 * @return 实体对象	 */	<T> T save(T entity);	/**	 * 更新对象	 * 	 * @param entity	 *            实体对象	 * @return 实体对象	 */	<T> T update(T entity);	/**	 * 保存或更新对象	 * 	 * @param entity	 *            实体对象	 * @return 实体对象	 */	<T> T saveOrUpdate(T entity);	/**	 * 保存或更新对象拷贝	 * 	 * @param entity	 * @return 已更新的持久化对象	 */	<T> T merge(T entity);	/**	 * 删除对象	 * 	 * @param entity	 *            实体对象	 */	<T> void delete(T entity);	/**	 * 根据ID删除记录	 * 	 * @param id	 *            记录ID	 */	<T> T deleteById(Class<T> clazz, Serializable id);	Pagination find(Finder finder, int pageNo, int pageSize);		Pagination findBySql(Finder finder, int pageNo, int pageSize);	Pagination find(Finder finder, int pageNo);	@SuppressWarnings("rawtypes")	List find(Finder finder);	<T> T findUnique(Finder finder);	boolean hasValue(Finder finder);	/**	 * 按属性查找对象列表	 */	<T> List<T> findByProperty(Class<T> clazz, String property, Object value);	/**	 * 按属性查找唯一对象	 * 	 * @param property	 * @param value	 * @return	 */	<T> T findUniqueByProperty(Class<T> clazz, String property, Object value);	/**	 * 按属性查找唯一对象	 * 	 * @param clazz	 * @param criterion	 * @return	 */	<T> T findUniqueByProperty(Class<T> clazz, Criterion... criterion);	/**	 * 按属性统计记录数	 * 	 * @param property	 * @param value	 * @return	 */	<T> int countByProperty(Class<T> clazz, String property, Object value);	@SuppressWarnings("rawtypes")	<T> List findByCriteria(Class<T> clazz, Criterion... criterion);	<T> List<T> findByCriteria(Class<T> clazz, int maxCount,			Criterion... criterion);	<T> List<T> findByCriteria(Class<T> clazz, OrderBy[] order,			Criterion... criterion);			<X> List<X> findBySQL(final String sql, Class<?> clazz,			final Map<String, ?> values);	/**	 * 通过Updater更新对象	 * 	 * @see cn.xdf.me.otp.common.hibernate4.Updater	 * @param updater	 * @return	 */	<T> T updateByUpdater(Updater<T> updater);	int executeUpdate(String hql, Object... values);	int executeSql(String sql, Object... values);	/**	 * <p>	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 如果传入entity,	 * 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,需执行:	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.	 * Hibernate.initialize	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.	 * </p>	 * 	 * @param proxy	 */	void initProxyObject(Object proxy);}