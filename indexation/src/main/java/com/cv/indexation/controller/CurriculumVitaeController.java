package com.cv.indexation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.cv.indexation.document.CurriculumVitae;
import com.cv.indexation.service.CurriculumVitaeService;
import com.cv.indexation.search.SearchRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import java.util.Base64;

@RestController
@RequestMapping("/api/cv")
public class CurriculumVitaeController {
    private final CurriculumVitaeService service;
    
    @Autowired
    public CurriculumVitaeController(CurriculumVitaeService service) {
        this.service = service;
    }

    @PostMapping
    public void index(@RequestBody final CurriculumVitae curriculumVitae) {
        service.index(curriculumVitae);
    }

    @GetMapping("/{id}")
    public CurriculumVitae getById(@PathVariable final String id) {
        return service.getById(id);
    }

    @PostMapping("/search")
    public List<CurriculumVitae> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }
    

     private final Logger logger = LoggerFactory.getLogger(CurriculumVitaeController.class);
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        logger.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        try {
        	indexFile(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
    
    //save file
    private void indexFile(List<MultipartFile> files) throws IOException {
    	CurriculumVitae curriculumVitae = new CurriculumVitae();
        String encoded = new String(Base64.getEncoder().encodeToString(files.get(0).getBytes()));
        //byte[] contentInBytes = Base64.getDecoder().decode(encoded);
    //	curriculumVitae.setContent(new String(contentInBytes, "UTF-8"));
    	curriculumVitae.setContent(new String(encoded));
    	service.index(curriculumVitae);
    }
}