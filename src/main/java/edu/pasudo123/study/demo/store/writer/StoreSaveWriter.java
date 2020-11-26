package edu.pasudo123.study.demo.store.writer;

import edu.pasudo123.study.demo.store.model.Store;
import edu.pasudo123.study.demo.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@RequiredArgsConstructor
//public class StoreSaveWriter extends JpaItemWriter<Store> {
//
//    private final StoreRepository storeRepository;
//
//    @Override
//    public void write(List<? extends Store> items) {
//        storeRepository.saveAll(items);
//    }
//}
