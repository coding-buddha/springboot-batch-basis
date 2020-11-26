package edu.pasudo123.study.demo.store.processor;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.model.StoreItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * itemProcessor 의 Input 및 Output 설정이 가능하다.
 */
@Slf4j
public class StoreItemSaveProcessor implements ItemProcessor<StoreItem, Store> {

    @Override
    public Store process(StoreItem item) {
        log.info("to save item : {}", item);
        return item.toStoreEntity();
    }
}
