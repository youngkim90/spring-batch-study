package com.schooldevops.springbatch.batchstudy.quiz11;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.item.ItemProcessor;

import com.schooldevops.springbatch.batchstudy.models.CompositeIndex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AggregateCompositeIndexProcessor implements ItemProcessor<CompositeIndex, CompositeIndex> {

	ConcurrentHashMap<String, Integer> aggregateCompositeIndex;

	public AggregateCompositeIndexProcessor(ConcurrentHashMap<String, Integer> aggregateCompositeIndex) {
		this.aggregateCompositeIndex = aggregateCompositeIndex;
	}

	@Override
	public CompositeIndex process(CompositeIndex item) {
		item.setRatio1(calculateChange(item.getIndex1(), item.getIndex2()));
		item.setRatio2(calculateChange(item.getIndex2(), item.getIndex3()));
		item.setRatio3(calculateChange(item.getIndex3(), item.getIndex4()));
		item.setRatio4(calculateChange(item.getIndex4(), item.getIndex5()));
		item.setRatio5(calculateChange(item.getIndex5(), item.getIndex6()));
		return item;
	}

	private String calculateChange(float previous, float current) {
		float change = ((current - previous) / previous) * 100;
		return Math.round(change * 100) / 100.0f + "%";
	}
}
