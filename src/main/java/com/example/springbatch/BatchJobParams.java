package com.example.springbatch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class BatchJobParams {
    @Value("${job.path}")
    String path;

    @Value("${job.path2}")
    String path2;
}
