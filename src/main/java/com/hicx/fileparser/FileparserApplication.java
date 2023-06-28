package com.hicx.fileparser;

import com.hicx.fileparser.utl.ShutdownApplication;
import com.hicx.fileparser.core.SimpleFileParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class FileparserApplication implements CommandLineRunner {

	private final ShutdownApplication shutdownApplication;

	public static void main(String[] args) {
		SpringApplication.run(FileparserApplication.class, args);
	}

	@Override
	public void run(String... args) {

		if (args != null && args.length > 1) {

			String pathDirectory = args[0];
			SimpleFileParser simpleFileParser = new SimpleFileParser(pathDirectory);
			simpleFileParser.processFiles();
			simpleFileParser.observeDirectoryChanges();
		} else {

			shutdownApplication.terminateFileParser();
		}
	}
}
