package com.schooldevops.springbatch.batchstudy.jobs.flatfilereader;

import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.schooldevops.springbatch.batchstudy.jobs.models.Customer;

public class CustomerItemSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<Customer> {
	@Override
	public SqlParameterSource createSqlParameterSource(Customer item) {
		return new BeanPropertySqlParameterSource(item);
	}
}
