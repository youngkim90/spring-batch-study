package com.schooldevops.springbatch.batchstudy.quiz11;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class CompositeHeader implements FlatFileHeaderCallback {
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("지수별,2024.03,2024.04,2024.05,2024.06,2024.07,2024.08");
	}
}
