package edu.pasudo123.study.demo.member.repository;

import edu.pasudo123.study.demo.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
