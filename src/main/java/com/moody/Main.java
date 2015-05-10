package com.moody;

import com.moody.config.SchedulerConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main class that load the configuration. This alone is enough to start the Quartz Scheduler.
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SchedulerConfiguration.class);
        ctx.refresh();
    }
}
