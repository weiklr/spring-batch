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

@SpringBootApplication
public class SpringBatchApplication implements CommandLineRunner {

    @Autowired
    Job importUserJob;

    @Autowired
    Job importUserJob2;

    @Autowired
    JobLauncher jobLauncher;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

    @Override
    public void run(String ...args) throws Exception {
        JobExecution execution = jobLauncher.run(
                importUserJob,
                getJobParameters()
        );
        JobExecution execution1 = jobLauncher.run(importUserJob2, getJobParameters());
        System.out.println(execution.getExitStatus());
        System.out.println(execution1.getExecutionContext().toString());
    }

    public JobParameters getJobParameters() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("test", "TEST!!!");
        jobParametersBuilder.addString("test2", "TEST2");
        return jobParametersBuilder.toJobParameters();
    }
}