package com.example.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

  private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

  private StepExecution stepExecution;

  @BeforeStep
  public void beforeProcess(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  @Override
  public Person process(final Person person) throws Exception {
    final String firstName = person.getFirstName().toUpperCase();
    final String lastName = person.getLastName().toUpperCase();

    final Person transformedPerson = new Person(firstName, lastName);

    ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

    log.info("Converting (" + person + ") into (" + transformedPerson + ")");
    executionContext.putInt("rowsProcessed", executionContext.getInt("rowsProcessed") + 1);
    return transformedPerson;
  }

  @AfterStep
  public void afterProcess(StepExecution stepExecution) {
    System.out.println("TEST PROCESS AFTER STEP");
  }
}
