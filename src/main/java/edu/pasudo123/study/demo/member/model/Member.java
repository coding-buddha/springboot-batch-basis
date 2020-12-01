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
    private Status status = Status.CREATE;

    public void updateId(final Long id) {
        this.id = id;
    }

    enum Status {
        CREATE,
        UPDATE, UPDATE_FAILED,
        DELETE, DELETE_FAILED
    }

    @Builder
    public Member(final String name, final String status) {
        this.name = name;
        this.status = Status.valueOf(status);
    }

    public void changeStatusToUpdate() {
        this.status = Status.UPDATE;
    }

    public void changeStatusToUpdateFailed() {
        this.status = Status.UPDATE_FAILED;
    }

    public void changeStatusToDelete() {
        this.status = Status.DELETE;
    }

    public void changeStatusToDeleteFailed() {
        this.status = Status.DELETE_FAILED;
    }

    public void doForceError() {
        throw new RuntimeException("doForceError");
    }
}
