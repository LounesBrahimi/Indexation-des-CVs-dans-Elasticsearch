package com.daar.indexation.model;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cv", createIndex = true)
public class CV {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "path")
    private String path;

    @Field(type = FieldType.Text, name = "data")
    private String data;

    @Field(type = FieldType.Text, name = "attachment.content")
    private String content;

    public CV() { }

    public CV(String content) {
        this.id = UUID.randomUUID().toString();
        this.data = content;
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

    public String getContent() {
        return content;
    }
}
