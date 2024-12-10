package com.schooldevops.springbatch.batchstudy.jobs.mybatiswriter;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.schooldevops.springbatch.batchstudy.jobs.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomItemWriter implements ItemWriter<Customer> {

	private final CustomService customService;

	public CustomItemWriter(CustomService customService) {
		this.customService = customService;
	}

	@Override
	public void write(Chunk<? extends Customer> chunk) {
		for (Customer customer : chunk) {
			log.info("Call Porcess in CustomItemWriter...");
			customService.processToOtherService(customer);
		}
	}
}
