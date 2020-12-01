package edu.pasudo123.study.demo.member.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

/**
 * 현재 스텝에 대한 조건절을 설정하기 위한 서포트 객체
 * ExitStatus.class 에 대한 값을 반환하고 flow().on()...from() 을 가능토록 해준다.
 * - COMPLETE
 * - FAILED
 */
@Slf4j
public class MemberUpdateListener extends StepExecutionListenerSupport {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // return ExitStatus.FAILED;
        // return ExitStatus.COMPLETED;
        return stepExecution.getExitStatus();
    }
}
