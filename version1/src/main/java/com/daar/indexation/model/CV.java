package com.daar.indexation.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cv", createIndex = true)
public class CV {
    @Id
    private final String id;

    @Field(type = FieldType.Text, name = "path")
    private String path;

    @Field(type = FieldType.Text, name = "data")
    private String data;

    public CV(String contentString) {
        this.id = UUID.randomUUID().toString();
        this.data = contentString;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
