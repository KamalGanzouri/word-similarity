package org.ganzouri.evision_task.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.ganzouri.evision_task.exception.EmptyFileException;
import org.ganzouri.evision_task.exception.TooManyFilesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.a.path}")
    private String fileAPath;

    @Value("${file.pool.path}")
    private String poolFilesPath;

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private Set<String> fileAWordCount ;

    @PostConstruct
    void init() {
        this.fileAWordCount = readFile(resolver.getResource(fileAPath));
        if (fileAWordCount.isEmpty()) {
            throw new EmptyFileException("A.txt is empty or contains no valid words.");
        }
    }

    @PreDestroy
    void destroy() {
        fileAWordCount.clear();
        System.out.println("FileService is being destroyed.");
    }

    private static Set<String> readFile(Resource resource) {
        Set<String> wordCountSet = new HashSet<>();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (word.matches("[a-zA-Z]+")) {
                        wordCountSet.add(word);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return wordCountSet;
    }

    private Double compareToFileA(Set<String> fileBWordCount) {
        if (fileAWordCount.isEmpty() || fileBWordCount.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(fileAWordCount);
        intersection.retainAll(fileBWordCount);

        Set<String> union = new HashSet<>(fileAWordCount);
        union.addAll(fileBWordCount);

        return intersection.size()*100.0/ union.size();
    }

    public Map<String, Double> getSimilarityOfFilesPool() {
        Map<String, Double> similarityMap = new HashMap<>();
        try {
            Resource[] poolOfFiles = resolver.getResources(poolFilesPath);

            if (poolOfFiles.length == 0) {
                throw new EmptyFileException("No .txt files found in classpath: poolOfFiles/");
            }else if (poolOfFiles.length > 20) {
                throw new TooManyFilesException("More than 20 .txt files found in classpath: poolOfFiles/");
            }

            for (Resource resource : poolOfFiles) {
                similarityMap.put(resource.getFilename(),compareToFileA(readFile(resource)));
            }
            return similarityMap;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load .txt files: " + e.getMessage());
        }
    }

}
