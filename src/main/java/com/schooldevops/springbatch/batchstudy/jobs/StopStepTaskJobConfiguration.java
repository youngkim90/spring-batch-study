package com.schooldevops.springbatch.batchstudy.jobs;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StopStepTaskJobConfiguration {

	public static final String STOP_STEP_TASK = "STOP_STEP_TASK";

	@Bean(name = "stepStop01")
	public Step stepStop01(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init myStep -----------------");

		return new StepBuilder("stepStop01", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				log.info("Execute Step 01 Tasklet ...");

				Random random = new Random();
				int randomValue = random.nextInt(1000);

				if (randomValue % 2 == 0) {
					return RepeatStatus.FINISHED;
				} else {
					throw new RuntimeException("Error This value is Odd: " + randomValue);
				}
			}, transactionManager)
			.build();
	}

	@Bean(name = "stepStop02")
	public Step stepStop02(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init myStep -----------------");

		return new StepBuilder("stepStop02", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				log.info("Execute Step 02 Tasklet ...");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Job stopStepJob(Step stepStop01, Step stepStop02, JobRepository jobRepository) {
		log.info("------------------ Init myJob -----------------");
		return new JobBuilder(STOP_STEP_TASK, jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(stepStop01)
			.on("FAILED").stop()
			.from(stepStop01).on("COMPLETED").to(stepStop02)
			.end()
			.build();
	}
}
