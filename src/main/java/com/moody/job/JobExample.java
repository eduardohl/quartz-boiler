package com.moody.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class JobExample extends QuartzJobBean {

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
