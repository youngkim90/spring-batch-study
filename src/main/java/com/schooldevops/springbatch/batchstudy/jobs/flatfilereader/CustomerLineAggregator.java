package com.schooldevops.springbatch.batchstudy.jobs.flatfilereader;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.schooldevops.springbatch.batchstudy.models.Customer;

public class CustomerLineAggregator implements LineAggregator<Customer> {
	@Override
	public String aggregate(Customer item) {
		return item.getName() + "," + item.getAge();
	}
}
