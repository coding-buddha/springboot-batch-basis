package edu.pasudo123.study.demo.member.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('CREATE', 'UPDATE', 'DELETE')")
    private Status status;

    enum Status {
        CREATE, UPDATE, DELETE
    }

    @Builder
    public Member(final String name, final String status) {
        this.name = name;
        this.status = Status.valueOf(status);
    }
}
