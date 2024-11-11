package com.schooldevops.springbatch.batchstudy.jobs.jpa;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchstudy.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {
	@Override
	public Customer process(Customer item) {
		log.info("Item Processor ------------------- {}", item);
		return item;
	}
}
