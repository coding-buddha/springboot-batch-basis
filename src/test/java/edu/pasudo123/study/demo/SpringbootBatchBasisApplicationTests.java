package edu.pasudo123.study.demo;

import edu.pasudo123.study.demo.store.config.StoreBatchConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * 기본 스프링 배치 테스트 코드 작성.
 */
@SpringBatchTest
@ContextConfiguration(classes = {TestBatchConfig.class, StoreBatchConfiguration.class})
class SpringbootBatchBasisApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void contextLoads() {}
}
