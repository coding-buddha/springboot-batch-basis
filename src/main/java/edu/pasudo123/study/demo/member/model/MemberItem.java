package edu.pasudo123.study.demo.member.model;

import lombok.Setter;

@Setter
public class MemberItem {
    private String name;
    private String status;

    public Member toMemberEntity() {
        return Member.builder()
                .name(name)
                .status(status)
                .build();
    }
}
