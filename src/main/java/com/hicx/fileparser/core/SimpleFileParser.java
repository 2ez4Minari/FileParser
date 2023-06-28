package com.hicx.fileparser.core;

import com.hicx.fileparser.utl.FileAnalysisHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Component
public class SimpleFileParser implements FileParser{

    private String pathDirectory;

    @Override
    public void observeDirectoryChanges() {
        WatchService watchService = null;

        try {

            watchService = FileSystems.getDefault().newWatchService();
            WatchKey key;

            Path path = Paths.get(pathDirectory);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    processFiles();
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            log.error("Exception encountered: {}", e.getMessage());
        }
    }

    @Override
    public void processFiles() {
        String processedDirectory = pathDirectory + "\\processed\\";
        File directoryPath = new File(pathDirectory);

        File[] files = directoryPath.listFiles((d, name) -> name.endsWith(".txt"));
        List<File> textFilesList = Arrays.asList(files);

        try {
            if(!Files.exists(Paths.get(processedDirectory))) {
                Files.createDirectory(Paths.get(processedDirectory));
            }

            textFilesList.forEach(file -> {

                    long wordCount = FileAnalysisHelper.calculateNumberOfWords(file);
                    long dotCount = FileAnalysisHelper.getDotCount(file);
                    Map<String, Integer> mostRepeatedWord = null;
                    mostRepeatedWord = FileAnalysisHelper.getMostRepeatedWord(file);


                    System.out.println("File: " + file.getName());
                    System.out.println("Number of word/s: " + wordCount);
                    System.out.println("Number of dot/s: " + dotCount);
                    System.out.println("Most used words: ");
                    mostRepeatedWord.entrySet().forEach(System.out::println);

                    System.out.println(file.getAbsolutePath());

                try {
                    Files.move(Paths.get(file.getPath()), Paths.get(processedDirectory + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (IOException e) {
            log.error("Exception encountered: {}", e.getMessage());
        }


    }
}

