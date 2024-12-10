package com.schooldevops.springbatch.batchstudy.jobs.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.schooldevops.springbatch.batchstudy.jobs.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Configuration
public class MybatisItemJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "UTF-8";
	public static final String MYBATIS_ITEM_WRITER_JOB = "MYBATIS_ITEM_WRITER_JOB";

	// @Autowired
	SqlSessionFactory sqlSessionFactory;

	@Bean
	public FlatFileItemReader<Customer> flatFileItemReader() {

		return new FlatFileItemReaderBuilder<Customer>()
			.name("FlatFileItemReader")
			.resource(new ClassPathResource("./customer.csv"))
			.encoding(ENCODING)
			.delimited().delimiter(",")
			.names("name", "age", "gender")
			.targetType(Customer.class)
			.build();
	}

	@Bean
	public MyBatisBatchItemWriter<Customer> mybatisItemWriter() {
		return new MyBatisBatchItemWriterBuilder<Customer>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.schooldevops.springbatch.batchstudy.jobs.insertCustomers")
			.build();
	}

	// @Bean
	public MyBatisBatchItemWriter<Customer> mybatisItemWriter2() {
		return new MyBatisBatchItemWriterBuilder<Customer>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.schooldevops.springbatch.batchsample.jobs.insertCustomers")
			.itemToParameterConverter(item -> {
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("name", item.getName());
				parameter.put("age", item.getAge());
				parameter.put("gender", item.getGender());
				return parameter;
			})
			.build();
	}

	@Bean
	public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init flatFileStep -----------------");

		return new StepBuilder("flatFileStep", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(flatFileItemReader())
			.writer(mybatisItemWriter())
			.build();
	}

	@Bean
	public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init flatFileJob -----------------");
		return new JobBuilder(MYBATIS_ITEM_WRITER_JOB, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(flatFileStep)
			.build();
	}
}
