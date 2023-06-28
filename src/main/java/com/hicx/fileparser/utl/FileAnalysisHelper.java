package com.hicx.fileparser.utl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FileAnalysisHelper {

    public static long calculateNumberOfWords(File file)  {

        String line;
        long wordCount = 0;

        try(FileReader textFile = new FileReader(file.getPath()); BufferedReader br = new BufferedReader(textFile)) {

            while ((line = br.readLine()) != null) {
                String words[] = line.split("");
                wordCount = wordCount + words.length;
            }

        } catch (IOException e) {
            log.error("Exception encountered: {}", e.getMessage());
        }

        return wordCount;
    }

    public static long getDotCount(File file) {

        long dotCount = 0;

        try {
            dotCount = StringUtils.countMatches(FileUtils.readFileToString(file, "UTF-8"), ".");
        } catch (IOException e) {
            log.error("Exception encountered: {}", e.getMessage());
        }

        return dotCount;
    }


    public static Map<String, Integer> getMostRepeatedWord(File file) {

        Map<String, Integer> words = new HashMap<String, Integer>();

        getWords(file.getPath(), words);

        int max = getMaxOccurrence(words);

        return words.entrySet().stream()
                                .filter(word -> word.getValue() == max)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // Method 2 - getMaxOccurrence()
    // To get maximum occurred Word
    private static int getMaxOccurrence(Map<String, Integer> words) {

        int max = 1;
        if (ObjectUtils.isNotEmpty(words)) {
            max = words.get(Collections.max(words.entrySet(), Map.Entry.comparingByValue()).getKey());
        }

        return max;
    }

    // Method 1 - getWords()
    // Reading out words from the file and
    // mapping key value pair corresponding to each different word
    private static void getWords(String fileName, Map<String, Integer> words) {

        try(Scanner file = new Scanner(new File(fileName))) {
            while (file.hasNext()) {

                String word = file.next();
                Integer count = words.get(word);
                count = (ObjectUtils.isEmpty(words.get(word))) ? 1 : ++count;
                words.put(word, count);
            }

            file.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}