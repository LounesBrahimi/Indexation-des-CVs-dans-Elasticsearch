package com.cv.indexation.document;

public class CurriculumVitae {
    
   // private String id;
    private String content;
    /*

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public static int uniqueId = 0;

    int getUniqueId()
    {
        return uniqueId++;
    }
}