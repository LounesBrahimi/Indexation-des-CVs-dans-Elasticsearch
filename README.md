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
