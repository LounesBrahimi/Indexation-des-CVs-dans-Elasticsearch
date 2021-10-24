package com.cv.indexation.service;

import com.cv.indexation.document.CurriculumVitae;
import com.cv.indexation.helper.Indices;
import com.cv.indexation.search.SearchRequestDTO;
import com.cv.indexation.search.util.SearchUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CurriculumVitaeService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(CurriculumVitaeService.class);

    private final RestHighLevelClient client;

    @Autowired
    public CurriculumVitaeService(RestHighLevelClient client) {
        this.client = client;
    }

    public List<CurriculumVitae> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.CV_INDEX,
                dto
        );
        return searchInternal(request);
    }

    public List<CurriculumVitae> getAllVehiclesCreatedSince(final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.CV_INDEX,
                "created",
                date
        );

        return searchInternal(request);
    }

    public List<CurriculumVitae> searchCreatedSince(final SearchRequestDTO dto, final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.CV_INDEX,
                dto,
                date
        );
        return searchInternal(request);
    }

    private List<CurriculumVitae> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            final List<CurriculumVitae> cv = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                cv.add(
                        MAPPER.readValue(hit.getSourceAsString(), CurriculumVitae.class)
                );
            }
            return cv;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Boolean index(final CurriculumVitae cv) {
        try {
            final String cvAsString = MAPPER.writeValueAsString(cv);

            final IndexRequest request = new IndexRequest(Indices.CV_INDEX);
            
          //  request.id(cv.getId());
            request.source(cvAsString, XContentType.JSON);

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            
            return response != null && response.status().equals(RestStatus.OK);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }
    
    public CurriculumVitae getById(final String cvId) {
        try {
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.CV_INDEX, cvId),
                    RequestOptions.DEFAULT
            );
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), CurriculumVitae.class);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
