package cn.me.xdf.workflow.listener;

import cn.me.xdf.model.base.BamProcess;
import cn.me.xdf.workflow.event.SourceEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-25
 * Time: 上午11:51
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SourceListener implements SmartApplicationListener {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == SourceEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        try {
            return (sourceType.newInstance() instanceof BamProcess)  ;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("资源发生了改变---------------" + event.getSource());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
