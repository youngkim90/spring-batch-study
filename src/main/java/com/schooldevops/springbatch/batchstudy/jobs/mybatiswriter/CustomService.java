package com.schooldevops.springbatch.batchstudy.jobs.mybatiswriter;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schooldevops.springbatch.batchstudy.jobs.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomService {

	public Map<String, String> processToOtherService(Customer item) {

		log.info("Call API to OtherService....");

		return Map.of("code", "200", "message", "OK");
	}
}
