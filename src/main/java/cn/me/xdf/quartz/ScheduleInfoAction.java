package cn.me.xdf.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-9
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleInfoAction {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleInfoAction.class);
    private Scheduler scheduler;

    // 设值注入，通过setter方法传入被调用者的实例scheduler
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void reScheduleJob() throws SchedulerException {
        // 运行时可通过动态注入的scheduler得到trigger，注意采用这种注入方式在有的项目中会有问题，如果遇到注入问题，可以采取在运行方法时候，获得bean来避免错误发生。
        CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger(new TriggerKey(
                "cronTrigger", Scheduler.DEFAULT_GROUP));


        logger.info("*********** trigger: " + trigger);
        String dbCronExpression = "10,15,20,25,30,35,40,45,50,55 * * * * ?";
        //每10s执行一次;
        //String dbCronExpression = "0/10 * * * * ?";

        logger.info("*********** dbCronExpression: " + dbCronExpression);
        String originConExpression = trigger.getCronExpression();
        logger.info("*********** originConExpression: " + originConExpression);
        // 判断从DB中取得的任务时间(dbCronExpression)和现在的quartz线程中的任务时间(originConExpression)是否相等
        // 如果相等，则表示用户并没有重新设定数据库中的任务时间，这种情况不需要重新rescheduleJob
        if (!originConExpression.equalsIgnoreCase(dbCronExpression)) {
            logger.info("*********** 执行时间: 不相等");
            // 如果触发时间不相同
            //  trigger.setCronExpression(dbCronExpression);


            scheduler.rescheduleJob(new TriggerKey("cronTrigger", Scheduler.DEFAULT_GROUP),
                    trigger);
        }
        // 执行task
        logger.info("task start time: " + new Date());
        System.out.println("red stone 工作了!");
        logger.info("  task end time: " + new Date());
    }
}
