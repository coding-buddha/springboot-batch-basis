package edu.pasudo123.study.demo.store.model;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class StoreItem {

    private Long no;
    private String name;
    private String address;
    private String phoneNumber;

    public StoreItem() {}

    public Store toStoreEntity() {
        return Store.builder()
                .no(no)
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
