package com.schooldevops.springbatch.batchstudy.jobs.jpa.reader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.CollectionUtils;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

	private EntityManager em;
	private final Function<JPAQueryFactory, JPAQuery<T>> querySupplier;

	private final Boolean alwaysReadFromZero;

	public QuerydslPagingItemReader(String name, EntityManagerFactory entityManagerFactory,
		Function<JPAQueryFactory, JPAQuery<T>> querySupplier, int chunkSize, Boolean alwaysReadFromZero) {
		super.setPageSize(chunkSize);
		setName(name);
		this.querySupplier = querySupplier;
		this.em = entityManagerFactory.createEntityManager();
		this.alwaysReadFromZero = alwaysReadFromZero;

	}

	@Override
	protected void doClose() throws Exception {
		if (em != null)
			em.close();
		super.doClose();
	}

	@Override
	protected void doReadPage() {
		initQueryResult();

		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
		long offset = 0;
		if (!alwaysReadFromZero) {
			offset = (long)getPage() * getPageSize();
		}

		JPAQuery<T> query = querySupplier.apply(jpaQueryFactory).offset(offset).limit(getPageSize());

		List<T> queryResult = query.fetch();
		for (T entity : queryResult) {
			em.detach(entity);
			results.add(entity);
		}
	}

	private void initQueryResult() {
		if (CollectionUtils.isEmpty(results)) {
			results = new CopyOnWriteArrayList<>();
		} else {
			results.clear();
		}
	}
}
