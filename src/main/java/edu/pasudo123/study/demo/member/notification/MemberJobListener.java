package edu.pasudo123.study.demo.member.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * job 수행이후에 호출되는 콜백 리스너
 *
 */
@Slf4j
public class MemberJobListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("");
            log.info("===== [job completed]");
            return;
        }

        if(jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("");
            log.info("===== [job failed]");
        }
    }
}
