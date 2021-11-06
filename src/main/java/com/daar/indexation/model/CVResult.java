package com.daar.indexation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cv", createIndex = true)
public class CVResult {
    @Id
    @JsonIgnore
    private String id;

    @Field(type = FieldType.Text, name = "path")
    @JsonIgnore
    private String path;

    @Field(type = FieldType.Text, name = "attachment.content")
    private String content;

    public CVResult() { }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
