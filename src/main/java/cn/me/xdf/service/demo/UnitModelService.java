package cn.me.xdf.service.demo;

import cn.me.xdf.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-23
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class UnitModelService extends SimpleService{


}
