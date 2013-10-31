package cn.me.xdf.filter.hibernate;


import org.hibernate.event.spi.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-22
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class MachineListener
        implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {


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
