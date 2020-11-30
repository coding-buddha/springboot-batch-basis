package edu.pasudo123.study.demo.store.writer;

import edu.pasudo123.study.demo.store.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CustomStoreWriter implements ItemWriter<Long> {

    private final EntityManagerFactory entityManagerFactory;

    public CustomStoreWriter(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    @Transactional
    public void write(List<? extends Long> items) throws Exception {
        final EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);

        if(entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
        }

        doWrite(entityManager, items);
        entityManager.flush();
    }

    private void doWrite(EntityManager entityManager, List<? extends Long> items) {
        if(items.isEmpty()) {
            return;
        }

        Query query = entityManager.createQuery("DELETE FROM Store s WHERE s.id IN (:ids)");
        query.setParameter("ids", items);
        query.executeUpdate();
    }

//    public void write(List<? extends Store> items) {
//        final EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
//
//        if(entityManager == null) {
//            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
//        }
//
//        doWrite(entityManager, items);
//        entityManager.flush();
//    }
//
//    private void doWrite(EntityManager entityManager, List<? extends Store> stores) {
//        if(stores.isEmpty()) {
//            return;
//        }
//
//        final List<Long> ids = stores.stream()
//                .map(Store::getId)
//                .collect(Collectors.toList());
//
//        Query query = entityManager.createQuery("DELETE FROM Store s WHERE s.id IN (:ids)");
//        query.setParameter("ids", ids);
//        query.executeUpdate();
//    }
}
