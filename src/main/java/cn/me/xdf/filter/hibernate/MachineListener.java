package cn.me.xdf.filter.hibernate;

import cn.me.xdf.annotaion.AttMainMachine;
import cn.me.xdf.annotaion.AttValues;
import cn.me.xdf.common.utils.MyBeanUtils;
import cn.me.xdf.model.base.AttMain;
import cn.me.xdf.model.base.IAttMain;
import cn.me.xdf.service.base.AttMainService;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-22
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class MachineListener
        implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {

    @Autowired
    private AttMainService attMainService;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {

    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {

    }

    @Override
    public void onPostInsert(PostInsertEvent event) {

    }
}
