package com.example.springbatch;

import lombok.Getter;
import lombok.Setter;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Setter
@Getter
public class DetectContentTypeTasklet implements Tasklet, InitializingBean {

  private static Tika TikaContentDetector = new Tika();

  private static final Logger log = LoggerFactory.getLogger(DetectContentTypeTasklet.class);

  private Resource directory;

  private StepExecution stepExecution;

  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
      throws Exception {

    StepContext stepContext = chunkContext.getStepContext();
    ExecutionContext executionContext = stepContext.getStepExecution().getExecutionContext();

    if (!directory.exists()) {
      throw new IllegalArgumentException(
          "invalid directory:" + directory.getFile().getAbsolutePath());
    }

    File[] files = directory.getFile().listFiles();
    int noOfFilesProcessed = 0;

    if (files == null) {
      log.info("no files in {}", directory.getFilename());
      return RepeatStatus.FINISHED;
    }

    for (File file : files) {
      if (Files.isDirectory(Paths.get(file.getPath()))) {
        continue;
      }
      String mimeType = TikaContentDetector.detect(file);
      log.info("{}'s mimetype is {}", file.getName(), mimeType);
      noOfFilesProcessed++;
    }
    executionContext.putInt("filesProcessed", noOfFilesProcessed);
    return RepeatStatus.FINISHED;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(directory, "directory must be set");
  }
}
