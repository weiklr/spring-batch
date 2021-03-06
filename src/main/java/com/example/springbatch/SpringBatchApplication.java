package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SpringBatchApplication implements CommandLineRunner {

  @Autowired Job importUserJob;

  @Autowired Job importUserJob2;

  @Autowired JobLauncher jobLauncher;

  @Autowired BatchJobParams batchJobParams;

  public static void main(String[] args) {
    SpringApplication.run(SpringBatchApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    runJob();
  }

  @Scheduled(cron = "0 */1 * * * ?")
  public void runJob() throws Exception {
    JobParameters jobParameters = getJobParameters();
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    jobParametersBuilder.addString("test", "TEST!!!");
    jobParametersBuilder.addString("test2", "TEST2");

    JobExecution execution = jobLauncher.run(importUserJob, jobParameters);
    //        JobExecution execution1 = jobLauncher.run(importUserJob2, jobParameters);
    System.out.println(execution.getExitStatus());
    //        System.out.println(execution1.getExecutionContext().toString());
    System.out.println("[SUCCESS]!");
  }

  public JobParameters getJobParameters() {
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    jobParametersBuilder.addString("test", "TEST!!!");
    jobParametersBuilder.addString("test2", "TEST2");
    jobParametersBuilder.addLong("time", System.currentTimeMillis());
    return jobParametersBuilder.toJobParameters();
  }
}
