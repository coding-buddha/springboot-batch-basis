package edu.pasudo123.study.demo.member.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PrintStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @Qualifier("middleStep")
    public Step middleStep() {
        return stepBuilderFactory.get("middleStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("****************************************************");
                    log.info("****************************************************");
                    log.info("****************************************************");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
