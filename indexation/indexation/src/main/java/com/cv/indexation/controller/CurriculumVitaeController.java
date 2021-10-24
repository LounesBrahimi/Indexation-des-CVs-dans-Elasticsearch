package com.cv.indexation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import com.cv.indexation.document.CurriculumVitae;
import com.cv.indexation.service.CurriculumVitaeService;
import com.cv.indexation.search.SearchRequestDTO;


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

}