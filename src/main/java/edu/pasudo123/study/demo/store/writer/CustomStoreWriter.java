package edu.pasudo123.study.demo.store.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

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
}
