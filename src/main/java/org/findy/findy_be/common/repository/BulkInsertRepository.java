package org.findy.findy_be.common.repository;

import java.util.List;

public interface BulkInsertRepository<T> {
	List<T> bulkInsert(List<T> entities);
}
