# CV Indexation

- Brahimi Lounes
- Auclair Julien

## Prerequisite

````
elasticsearch-plugin install ingest-attachment
````

Use `logstash-config.conf` file as logstash configuration
````
logstash logstash-config.conf
````

## Usage

- Upload CV
  - `POST /api/v1/cv?file=`

- Search CVs by keyword
  - `GET /api/v1/cv?keyword=`

![elasticsearch](https://user-images.githubusercontent.com/43423295/140646742-a45a771a-a4da-4623-a81d-0335a1c7feb2.gif)
