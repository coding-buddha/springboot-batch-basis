## springboot-batch-basis
* 스프링부트 기반의 배치를 hello world 수준으로 do-it do-it
* 환경
    * springboot 2.3.6.RELEASE
    * spring-batch 4.2.4.RELEASE
    * mysql 8.0.x
    * java11
    * windows10

## 🔥 배치 에플리케이션
* 수 많은 데이터를 일괄처리하기 위한 애플리케이션을 의미
    * 왜, 수 많은 데이터를 일괄로 처리해야 하는가?
        * 단 건으로 처리할 시에 많은 리소스가 소요된다.
    * 어떠한 것들이 배치를 적용하기에 적합한가?
        * 일별 정산이 필요한 경우
        * 매달 새로운 할인가로 갱신이 필요한 경우
        * 사용하지 않는 데이터의 가비지가 쌓이는 삭제가 필요한 경우
        * 등등
* 배치와 스케줄링은 서로 긴밀하게 연관되어 있다.
    * `배치` 는 일괄처리
    * `스케줄링` 은 정해진 시간마다 프로세스 구동
    * `배치` 를 일정시간마다 실행시킬 수 있도록 `스케줄링` 을 적용한다. 라는 개념

## 🔥 배치를 도와줄 스케줄러
* 배치를 도와줄 스케줄러는 아래와 같이 있다.
    * linux cron
    * jenkins
    * quartz
    * spring scheduler (spring dependency 가 있는 경우)
    * github action
    * 등등

## 🔥 배치를 작성하면서 겪는 `유의사항`
* 프로세스 실행 중에 재할당에 시간을 소모하지 않도록 배치 애플리케이션 시작 시 충분한 메모리를 할당하기


## 🔥 배치를 작성하면서 겪는 `궁금증`
* [ItemReader 의 cursor & paging 의 차이](./docs/ItemReader_cursor_vs_paging.md)
* [spring batch 테스트코드 작성 시 롤백은 어떻게 하는가?](#)
    * https://stackoverflow.com/questions/47884685/rollback-spring-batch-job
    * https://docs.spring.io/spring-batch/docs/current/reference/html/job.html#txConfigForJobRepository
* [batch 메타테이블의 역할](./docs/batch_meta_data_table.md)

## reference
* https://docs.spring.io/spring-batch/docs/current/reference/html/index.html
* [jojoldu blog](https://jojoldu.tistory.com/324)
* [renuevo blog](https://renuevo.github.io/spring/batch/spring-batch-chapter-1/)
* [batch skip](https://www.baeldung.com/spring-batch-skip-logic)