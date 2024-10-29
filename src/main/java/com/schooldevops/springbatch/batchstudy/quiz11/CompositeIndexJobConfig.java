package com.schooldevops.springbatch.batchstudy.quiz11;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.schooldevops.springbatch.batchstudy.models.CompositeIndex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CompositeIndexJobConfig {

	/**
	 * CHUNK 크기를 지정한다.
	 */
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "EUC-KR";
	public static final String FLAT_FILE_WRITER_CHUNK_JOB = "FLAT_FILE_WRITER_CHUNK_JOB";

	private ConcurrentHashMap<String, Integer> aggregateInfos = new ConcurrentHashMap<>();

	// private final ItemProcessor<Customer, Customer> itemProcessor = new AggregateCustomerProcessor(aggregateInfos);
	private final ItemProcessor<CompositeIndex, CompositeIndex> itemProcessor = new AggregateCompositeIndexProcessor(
		aggregateInfos);

	@Bean
	public FlatFileItemReader<CompositeIndex> flatFileItemReader() {

		return new FlatFileItemReaderBuilder<CompositeIndex>()
			.name("FlatFileItemReader")
			.resource(new ClassPathResource("./composite_index.csv"))
			.encoding(ENCODING)
			.delimited().delimiter(",")
			.names("name", "index1", "index2", "index3", "index4", "index5", "index6")
			.targetType(CompositeIndex.class)
			.linesToSkip(1)
			.build();
	}

	@Bean
	public FlatFileItemWriter<CompositeIndex> flatFileItemWriter() {

		return new FlatFileItemWriterBuilder<CompositeIndex>()
			.name("flatFileItemWriter")
			.resource(new FileSystemResource("./output/composite_index_new.csv"))
			.encoding(ENCODING)
			.delimited().delimiter("\t")
			.names("Ratio1", "Ratio2", "Ratio3", "Ratio4", "Ratio5")
			.append(false)
			.lineAggregator(new CompositeIndexLineAggregator())
			.headerCallback(new CompositeHeader())
			// .footerCallback(new CustomerFooter(aggregateInfos))
			.build();
	}

	@Bean
	public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init flatFileStep -----------------");

		return new StepBuilder("flatFileStep", jobRepository)
			.<CompositeIndex, CompositeIndex>chunk(CHUNK_SIZE, transactionManager)
			.reader(flatFileItemReader())
			.processor(itemProcessor)
			.writer(flatFileItemWriter())
			.build();
	}

	@Bean
	public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init flatFileJob -----------------");

		return new JobBuilder(FLAT_FILE_WRITER_CHUNK_JOB, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(flatFileStep)
			.build();
	}
}
