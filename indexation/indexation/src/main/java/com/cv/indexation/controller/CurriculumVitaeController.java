package com.cv.indexation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import com.cv.indexation.document.CurriculumVitae;
import com.cv.indexation.service.CurriculumVitaeService;
import com.cv.indexation.service.helper.CurriculumVitaeDummyDataService;
import com.cv.indexation.search.SearchRequestDTO;


@RestController
@RequestMapping("/api/cv")
public class CurriculumVitaeController {
    private final CurriculumVitaeService service;
    private final CurriculumVitaeDummyDataService dummyDataService;

    @Autowired
    public CurriculumVitaeController(CurriculumVitaeService service, CurriculumVitaeDummyDataService dummyDataService) {
        this.service = service;
        this.dummyDataService = dummyDataService;
    }

    @PostMapping
    public void index(@RequestBody final CurriculumVitae curriculumVitae) {
        service.index(curriculumVitae);
    }

 //   @PostMapping("/insertdummydata")
 //   public void insertDummyData() {
  //      dummyDataService.insertDummyData();
  //  }

    @GetMapping("/{id}")
    public CurriculumVitae getById(@PathVariable final String id) {
        return service.getById(id);
    }

    @PostMapping("/search")
    public List<CurriculumVitae> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }

    @PostMapping("/searchcreatedsince/{date}")
    public List<CurriculumVitae> searchCreatedSince(
            @RequestBody final SearchRequestDTO dto,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            final Date date) {
        return service.searchCreatedSince(dto, date);
    }
}