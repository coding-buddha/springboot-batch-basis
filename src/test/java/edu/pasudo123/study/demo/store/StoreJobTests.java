package edu.pasudo123.study.demo.store;

import edu.pasudo123.study.demo.TestBatchConfig;
import edu.pasudo123.study.demo.notification.CustomNotificationListener;
import edu.pasudo123.study.demo.store.config.DeleteStepConfiguration;
import edu.pasudo123.study.demo.store.config.SaveStepConfiguration;
import edu.pasudo123.study.demo.store.config.StoreBatchConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
//@SpringBatchTest
@SpringBootTest(classes = {
        StoreBatchConfiguration.class,
        SaveStepConfiguration.class,
        DeleteStepConfiguration.class,
        CustomNotificationListener.class,
        JobBuilderFactory.class
})
@ContextConfiguration(classes = TestBatchConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("스토어 배치 잡은")
public class StoreJobTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Qualifier("storeJob")
    @Autowired
    private Job job;

    @BeforeAll
    void init() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @DisplayName("배치 잡을 수행한다.")
    public void jobLaunchTest() throws Exception {
        jobLauncherTestUtils.setJob(job);
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    }
}
