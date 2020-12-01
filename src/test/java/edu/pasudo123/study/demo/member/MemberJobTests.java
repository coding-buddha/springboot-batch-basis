package edu.pasudo123.study.demo.member;

import edu.pasudo123.study.demo.MemberBatchConfigurationForTest;
import edu.pasudo123.study.demo.member.config.MemberBatchConfiguration;
import edu.pasudo123.study.demo.member.notification.MemberJobNotificationListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        MemberBatchConfiguration.class,
        MemberJobNotificationListener.class
})
@ContextConfiguration(classes = {MemberBatchConfigurationForTest.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("멤버 잡은")
public class MemberJobTests {

    @Autowired private MemberJobNotificationListener memberJobNotificationListener;
    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Test
    @DisplayName("콘텍스트를 로드한다.")
    public void contextLoadTest() {
        assertThat(jobLauncherTestUtils).isNotNull();
    }

    @BeforeEach
    void clear() {
        // MemberBatchConfiguration 의 job 안에 step 이 모두 등록되어있어야 사용이 가능하다.
        jobLauncherTestUtils.launchStep("clearDbStep");
    }

    @Test
    @DisplayName("step 수행한다.")
    public void memberStepTest() throws Exception {
        // when
        final JobExecution jobExecution = jobLauncherTestUtils.launchStep("csvToDbStep");
        final JobInstance jobInstance = jobExecution.getJobInstance();
        final ExitStatus exitStatus = jobExecution.getExitStatus();

        // then
        assertThat(jobInstance.getJobName()).isEqualTo("TestJob");
        assertThat(exitStatus.getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("job 을 수행한다.")
    public void memberJobTest() throws Exception {
        // when
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        final JobInstance jobInstance = jobExecution.getJobInstance();
        final ExitStatus exitStatus = jobExecution.getExitStatus();

        // then
        assertThat(jobInstance.getJobName()).isEqualTo("memberJob");
        assertThat(exitStatus.getExitCode()).isEqualTo("COMPLETED");
    }
}
