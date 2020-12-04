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
    @Qualifier("middleSuccessStep")
    public Step middleSuccessStep() {
        return stepBuilderFactory.get("middleSuccessStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("****************************************************");
                    log.info("***                                              ***");
                    log.info("***                                              ***");
                    log.info("***              MIDDLE SUCCESS STEP             ***");
                    log.info("***                                              ***");
                    log.info("***                                              ***");
                    log.info("****************************************************");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @Qualifier("middleFailedStep")
    public Step middleFailedStep() {
        return stepBuilderFactory.get("middleFailedStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("****************************************************");
                    log.info("***                                              ***");
                    log.info("***                                              ***");
                    log.info("***              MIDDLE FAILED STEP              ***");
                    log.info("***                                              ***");
                    log.info("***                                              ***");
                    log.info("****************************************************");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
