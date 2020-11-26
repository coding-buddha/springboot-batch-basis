package edu.pasudo123.study.demo.store.processor;

import edu.pasudo123.study.demo.store.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import javax.persistence.EntityManagerFactory;

@Slf4j
public class StoreItemDeleteProcessor implements ItemProcessor<Store, Store> {

    @Override
    public Store process(Store item) throws Exception {
        log.info("current item : {}", item);
        return item;
    }
}
