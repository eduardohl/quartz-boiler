package com.moody.config;

import com.moody.job.JobExample;
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
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(JobExample.class);
        jobDetailFactoryBean.setName("JobExampleName");
        jobDetailFactoryBean.setGroup("JobExampleGroup");

        Map<String, Object> jobData = new HashMap<>();
        jobData.put("parameterExample", "aggregation job value string");
        jobDetailFactoryBean.setJobDataAsMap(jobData);

        return jobDetailFactoryBean;
    }

    @Bean
    /**
     * @see http://quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-06
     * @see http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
     */
    public CronTriggerFactoryBean cronTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(job().getObject());
        cronTriggerFactoryBean.setCronExpression("0 * * * * ?"); // Every minute
        return cronTriggerFactoryBean;
    }

    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        //schedulerFactoryBean.setDataSource();
        //schedulerFactoryBean.setTransactionManager();
        //schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setSchedulerName("JobSchedulerExampleName");

        // custom job factory of spring with DI support for @Autowired!
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        Trigger[] triggers = {cronTrigger().getObject()};
        schedulerFactoryBean.setTriggers(triggers);
        return schedulerFactoryBean;
    }

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
