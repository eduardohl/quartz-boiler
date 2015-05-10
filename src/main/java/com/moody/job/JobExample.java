package com.moody.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;



/**
 * Simple job sample. Replace the code inside by process code.
 */
//@DisallowConcurrentExecution - use this annotation if you don't want concurrent executions (i.e. when you have jobs that could take longer than your trigger schedule)
public class JobExample extends QuartzJobBean {

    //Sample of a dependency that can be injected to the bean.
    private String parameterExample;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("");
        System.out.println("  -- Job started... --  ");
        System.out.println("  -- Job processing " + parameterExample + "... --  ");
        System.out.println("  -- ...Job finished --  ");
        System.out.println("");
    }

    public void setParameterExample(String parameterExample) {
        this.parameterExample = parameterExample;
    }
}
