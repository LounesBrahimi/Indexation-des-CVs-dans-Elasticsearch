package com.daar.indexation.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import com.daar.indexation.model.CVResult;

public interface CVService {
    void init();

    void save(MultipartFile file) throws IOException, IllegalArgumentException;

    List<CVResult> search(String keyword);
}
