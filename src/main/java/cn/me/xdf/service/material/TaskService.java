package cn.me.xdf.service.material;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.me.xdf.model.material.Task;
import cn.me.xdf.service.BaseService;

/**
 * 作业的service
 * @author yuhz
 */
@Service
@Transactional(readOnly = true)
public class TaskService extends BaseService {

	@SuppressWarnings("unchecked")
	@Override
	public Class<Task> getEntityClass() {
		return Task.class;
	}
}
