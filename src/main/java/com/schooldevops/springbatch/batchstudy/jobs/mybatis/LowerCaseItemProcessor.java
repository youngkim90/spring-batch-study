package com.schooldevops.springbatch.batchstudy.jobs.mybatis;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchstudy.jobs.models.Customer;

public class LowerCaseItemProcessor implements ItemProcessor<Customer, Customer> {

	/**
	 * 이름, 성별을 소문자로 변경하는 ItemProcessor
	 */
	@Override
	public Customer process(Customer item) {
		System.out.println("### LowerCaseItemProcessor execute");
		item.setName(item.getName().toLowerCase());
		item.setGender(item.getGender().toLowerCase());
		return item;
	}
}
