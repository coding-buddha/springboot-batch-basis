package edu.pasudo123.study.demo.store.repository;

import edu.pasudo123.study.demo.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
