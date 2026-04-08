package com.solvd.autoservicerepair.utils;

import com.solvd.Main;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileReaderUtil {

    private FileReaderUtil() {
    }

    public static void readFile(String resourcePath) {

        try {
            File file = new File(Main.class.getClassLoader().getResource(resourcePath).getFile());
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            Map<String, Long> wordCountMap = new HashMap<>();

            for (String line : lines) {
                for (String word : StringUtils.split(line)) { // splits by whitespace
                    if (StringUtils.isNotBlank(word)) {
                        String formattedWord = word.replaceAll("\\W", "").toLowerCase();
                        wordCountMap.put(formattedWord, wordCountMap.getOrDefault(formattedWord, 0L) + 1);
                    }
                }
            }

            List<String> uniqueWords = new ArrayList<>();

            wordCountMap.forEach((word, count) -> {
                if (count == 1) {
                    uniqueWords.add(word);
                }

            });

            log.info("Word Map: {}", wordCountMap);
            log.info("Unique Words: {}", uniqueWords);
            log.info("Unique Words Count: {}", uniqueWords.size());

        } catch (IOException e) {
            throw new RuntimeException("Either couldn't read file or file is null", e);
        }

    }

}
