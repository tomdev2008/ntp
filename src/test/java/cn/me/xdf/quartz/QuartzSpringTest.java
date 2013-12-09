package cn.me.xdf.quartz;

import cn.me.xdf.BaseTest;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-9
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class QuartzSpringTest extends BaseTest {

    @Autowired
    private SchedulerFactoryBean startQuertz;

    @Test
    public void testQueryAll() throws SchedulerException {
        Scheduler scheduler = startQuertz.getScheduler();


        List<String> schedulerName = scheduler.getJobGroupNames();
        for (String scname : schedulerName) {
           // scheduler.getTriggerNames(triggerGroups[i]);

            System.out.println("scname====" + scname);
        }
    }
}
