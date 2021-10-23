package com.daar.indexation.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;
import java.util.HashMap;

import com.daar.indexation.model.CV;
import com.daar.indexation.service.CVService;

@RestController
@RequestMapping("/api/v1/cv")
public class CVController {

    @Autowired
    private CVService cvService;

    @GetMapping
    public ResponseEntity<Object> search(@RequestParam("keyword") String keyword) {
        List<CV> results = cvService.search(keyword);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", HttpStatus.OK.value());
        map.put("results", results);
        map.put("length", results.size());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) {
        try {
            cvService.save(file);
        } catch (IOException e) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", "Uploading failed");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", HttpStatus.OK.value());
        map.put("message", "Uploaded successfully");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

}
