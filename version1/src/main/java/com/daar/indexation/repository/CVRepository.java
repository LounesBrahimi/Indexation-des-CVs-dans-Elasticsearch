package com.daar.indexation.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.daar.indexation.model.CV;

@Repository
public interface CVRepository extends ElasticsearchRepository<CV, String> {
    @Query("{\"bool\": {\"must\": [{\"fuzzy\": {\"title\": \"?0\"}}]}}")
    List<CV> search(String keyword);
}
