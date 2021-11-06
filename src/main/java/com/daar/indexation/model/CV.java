package com.daar.indexation.model;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "cv", createIndex = true)
public class CV {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @Field(type = FieldType.Text, name = "path")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String path;

    @Field(type = FieldType.Text, name = "data")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
