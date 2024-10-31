package org.findy.findy_be.common.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class BulkInsertRepositoryImpl<T> implements BulkInsertRepository<T> {

	private static final int BATCH_SIZE = 100;
	private final EntityManager entityManager;

	@Override
	public List<T> bulkInsert(List<T> entities) {
		for (int i = 0; i < entities.size(); i++) {
			entityManager.persist(entities.get(i));
			if (i > 0 && i % BATCH_SIZE == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
		entityManager.flush();
		entityManager.clear();
		return entities;
	}
}
