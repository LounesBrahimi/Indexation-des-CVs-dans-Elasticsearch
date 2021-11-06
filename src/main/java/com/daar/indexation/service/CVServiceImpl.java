package com.daar.indexation.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Base64;
import java.util.HashMap;
import com.daar.indexation.model.CV;
import com.daar.indexation.model.CVResult;
import com.daar.indexation.repository.CVRepository;

@Service
public class CVServiceImpl implements CVService {
    private static final Logger log = LoggerFactory.getLogger(CVServiceImpl.class);

    private static final String[] VALID_FILES_EXTENSION = {"pdf", "doc", "docx"};
    private static final String INDEX_NAME = "cv";
    private static final String FOLDER_PATH = "cv";
    private final Path rootPath = Paths.get(FOLDER_PATH);

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            log.error("CV Service - initialization error - ", e.getMessage());
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public void save(MultipartFile file) throws IOException, IllegalArgumentException {
        String encoded = new String(Base64.getEncoder().encodeToString(file.getBytes()));
        CV cv = new CV(encoded);

        String extension = getFileExtension(file.getOriginalFilename());

        if (!isValidFileExtension(extension)) {
            log.error("Save CV - Bad extension - " + extension);
            throw new IllegalArgumentException("Invalid extension: " + extension);
        }

        Path destinationFile = this.rootPath.resolve(Paths.get(cv.getId() + "." + extension))
                .normalize().toAbsolutePath();

        cv.setPath(destinationFile.toString());

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        indexing(cv);
    }

    public List<CVResult> search(String keyword) {
        return cvRepository.search(keyword);
    }

    private void indexing(CV cv) throws IOException {
        String json = objectMapper.writeValueAsString(cv);
        HashMap<String, Object> content = objectMapper.readValue(json, HashMap.class);
        IndexRequest request = new IndexRequest(INDEX_NAME).id(cv.getId()).setPipeline("attachment")
                .source(content);

        highLevelClient.index(request, RequestOptions.DEFAULT);
    }

    private static String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            return filename.substring(i + 1);
        }

        return "";
    }

    private static boolean isValidFileExtension(String extension) {
        for (String ext : VALID_FILES_EXTENSION) {
            if (ext.equals(extension)) {
                return true;
            }
        }

        return false;
    }
}
