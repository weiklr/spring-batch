package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.nio.file.Path;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Autowired public JobBuilderFactory jobBuilderFactory;

  @Autowired public StepBuilderFactory stepBuilderFactory;

  @Autowired JobCompletionNotificationListener jobCompletionNotificationListener;

  @Autowired DataSource dataSource;

  @Value("${job.dir}")
  private Path directory;

  @Bean
  public ExecutionContextPromotionListener promotionListener() {
    ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

    listener.setKeys(new String[] {"filesProcessed"});

    return listener;
  }

  @Bean
  public DetectContentTypeTasklet detectContentTypeTasklet() {
    DetectContentTypeTasklet detectContentTypeTasklet = new DetectContentTypeTasklet();
    FileSystemResource fileSystemResource = new FileSystemResource(directory);
    detectContentTypeTasklet.setDirectory(fileSystemResource);
    return detectContentTypeTasklet;
  }

  @Bean
  @JobScope
  public FlatFileItemReader<Person> reader() {
    return new FlatFileItemReaderBuilder<Person>()
        .name("personItemReader")
        .resource(new ClassPathResource("sample-data.csv"))
        .delimited()
        .names(new String[] {"firstName", "lastName"})
        .fieldSetMapper(
            new BeanWrapperFieldSetMapper<Person>() {
              {
                setTargetType(Person.class);
              }
            })
        .build();
  }

  @Bean
  public PersonItemProcessor processor() {
    return new PersonItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
        .dataSource(dataSource)
        .build();
  }

  @Bean
  public Job importUserJob(Step detectContentType, Step step1) {
    return jobBuilderFactory
        .get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .listener(jobCompletionNotificationListener)
        .start(detectContentType)
        .next(step1)
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory
        .get("step1")
        .<Person, Person>chunk(1)
        .reader(reader())
        .processor(processor())
        .writer(writer(dataSource))
        .build();
  }

  @Bean
  public Step detectContentType() {
    return stepBuilderFactory
        .get("detectContentType")
        .listener(new DetectContentStepListener())
        .listener(promotionListener())
        .tasklet(detectContentTypeTasklet())
        .build();
  }
}
