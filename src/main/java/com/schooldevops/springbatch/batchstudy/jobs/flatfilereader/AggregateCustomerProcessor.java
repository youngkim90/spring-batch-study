package com.schooldevops.springbatch.batchstudy.jobs.flatfilereader;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchstudy.jobs.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AggregateCustomerProcessor implements ItemProcessor<Customer, Customer> {

	ConcurrentHashMap<String, Integer> aggregateCustomers;

	public AggregateCustomerProcessor(ConcurrentHashMap<String, Integer> aggregateCustomers) {
		this.aggregateCustomers = aggregateCustomers;
	}

	@Override
	public Customer process(Customer item) {
		aggregateCustomers.putIfAbsent("TOTAL_CUSTOMERS", 0);
		aggregateCustomers.putIfAbsent("TOTAL_AGES", 0);

		aggregateCustomers.put("TOTAL_CUSTOMERS", aggregateCustomers.get("TOTAL_CUSTOMERS") + 1);
		aggregateCustomers.put("TOTAL_AGES", aggregateCustomers.get("TOTAL_AGES") + item.getAge());
		return item;
	}
}
