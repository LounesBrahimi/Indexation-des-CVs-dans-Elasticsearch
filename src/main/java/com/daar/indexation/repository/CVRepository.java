package com.daar.indexation.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.daar.indexation.model.CVResult;

@Repository
public interface CVRepository extends ElasticsearchRepository<CVResult, String> {
    @Query("{\"match\": {\"attachment.content\": \"?0\"}}")
    List<CVResult> search(String keyword);
}
