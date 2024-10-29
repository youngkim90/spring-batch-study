package com.schooldevops.springbatch.batchstudy.jobs.flatfilereader;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class CustomerHeader implements FlatFileHeaderCallback {
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("ID,AGE");
	}
}
