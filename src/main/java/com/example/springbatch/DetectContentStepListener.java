package com.example.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class DetectContentStepListener implements StepExecutionListener {

  private ExecutionContext executionContext;

  private static final Logger log = LoggerFactory.getLogger(DetectContentStepListener.class);

  @Override
  public void beforeStep(StepExecution stepExecution) {
    log.info("INITIALIZE STEP CONTEXT");
    this.executionContext = stepExecution.getJobExecution().getExecutionContext();
    stepExecution.setExecutionContext(this.executionContext);
    //    System.out.println("context is " + this.executionContext.toString());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info("Step COMPLETED");
    log.info("no. of files processed {}", executionContext.getInt("filesProcessed"));
    return stepExecution.getExitStatus();
  }
}
