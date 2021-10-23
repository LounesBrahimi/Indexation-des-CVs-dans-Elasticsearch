package com.daar.indexation.service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import com.daar.indexation.repository.CVRepository;

@Service
public class CVServiceImpl implements CVService {
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
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public void save(MultipartFile file) throws IOException {
        Path destinationFile = this.rootPath.resolve(Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        String encoded = new String(Base64.getEncoder().encodeToString(file.getBytes()));

        CV cv = new CV(encoded);
        cv.setPath(destinationFile.toString());

        indexing(cv);
    }

    public List<CV> search(String keyword) {
        return cvRepository.search(keyword);
    }

    private void indexing(CV cv) throws IOException {
        String json = objectMapper.writeValueAsString(cv);
        HashMap<String, Object> content = objectMapper.readValue(json, HashMap.class);
        IndexRequest request = new IndexRequest(INDEX_NAME).id(cv.getId()).setPipeline("attachment")
                .source(content);

        highLevelClient.index(request, RequestOptions.DEFAULT);
    }
}
