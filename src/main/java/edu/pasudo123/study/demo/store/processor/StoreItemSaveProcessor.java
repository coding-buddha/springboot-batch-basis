package edu.pasudo123.study.demo.store.processor;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.model.StoreItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class StoreItemSaveProcessor implements ItemProcessor<StoreItem, Store> {

    @Override
    public Store process(StoreItem item) {
        log.info("current item : {}", item);
        return item.toStoreEntity();
    }
}
