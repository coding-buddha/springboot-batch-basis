package edu.pasudo123.study.demo.store.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "no", nullable = false, columnDefinition = "BIGINT")
    private Long no;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(100)")
    private String address;

    @Column(name = "phone_number", nullable = true, columnDefinition = "VARCHAR(100)")
    private String phoneNumber;

    @Builder
    public Store(Long no, String name, String address, String phoneNumber) {
        this.no = no;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
