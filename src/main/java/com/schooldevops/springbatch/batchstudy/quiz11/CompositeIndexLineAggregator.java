package com.schooldevops.springbatch.batchstudy.quiz11;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.schooldevops.springbatch.batchstudy.models.CompositeIndex;
import com.schooldevops.springbatch.batchstudy.models.Customer;

public class CompositeIndexLineAggregator implements LineAggregator<CompositeIndex> {
	@Override
	public String aggregate(CompositeIndex item) {
		StringBuilder sb = new StringBuilder();
		sb.append(item.getName()).append(",,");
		sb.append(item.getRatio1()).append(",");
		sb.append(item.getRatio2()).append(",");
		sb.append(item.getRatio3()).append(",");
		sb.append(item.getRatio4()).append(",");
		sb.append(item.getRatio5());
		return sb.toString();
	}
}
