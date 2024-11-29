package com.schooldevops.springbatch.batchstudy.jobs.mybatis;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchstudy.models.Customer;

public class After20YearsItemProcessor implements ItemProcessor<Customer, Customer> {

	/**
	 * 나이에 20년을 더하는 ItemProcessor
	 */
	@Override
	public Customer process(Customer item) {
		System.out.println("### After20YearsItemProcessor execute");
		item.setAge(item.getAge() + 20);
		return item;
	}
}
