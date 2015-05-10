package com.moody.config;

import com.moody.job.JobExample;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class SchedulerConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobDetailFactoryBean job() {
        //Set job with process code
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(JobExample.class);
        jobDetailFactoryBean.setName("JobExampleName");
        jobDetailFactoryBean.setGroup("JobExampleGroup");

        //Set datamap
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("parameterExample", "aggregation job value string");
        jobDetailFactoryBean.setJobDataAsMap(jobData);

        return jobDetailFactoryBean;
    }

    /**
     * Simple CronTrigger. The links below show how to create the scheduling string.
     * @see http://quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-06
     * @see http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
     */
    @Bean
    public CronTriggerFactoryBean cronTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(job().getObject());
        cronTriggerFactoryBean.setCronExpression("0 * * * * ?"); // Fire job every minute
        cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING); //When trying to fire a job that cannot run (because of DisallowConcurrentExecution for example), just do nothing and abort this execution.
        return cronTriggerFactoryBean;
    }

    /**
     *  This bean represents the scheduler context, and should be central part of your job scheduling system.
     *  It has all of your previously configured triggers and the configuration to be used.
     */
    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        //schedulerFactoryBean.setDataSource();
        //schedulerFactoryBean.setTransactionManager();
        //schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setSchedulerName("JobSchedulerExampleName");
        schedulerFactoryBean.setQuartzProperties(quartzProperties());

        // custom job factory of spring with DI support for @Autowired!
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        Trigger[] triggers = {cronTrigger().getObject()};
        schedulerFactoryBean.setTriggers(triggers);
        return schedulerFactoryBean;
    }

    /**
     * Load Quartz properties configuration
     * @return
     */
    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();

        } catch (IOException e) {
            log.warn("Cannot load quartz.properties.");
        }
        return properties;
    }
}
