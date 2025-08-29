package org.ganzouri.evision_task.controller;

import org.ganzouri.evision_task.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.Map;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/similarity")
    public Map<String, Double> getSimilarity() throws FileNotFoundException {
        return fileService.getSimilarityOfFilesPool();
    }

}
