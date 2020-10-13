package com.coderpakistan.learningbank.Models;

public class SessionVocabularyModel {
    private String id,name,type;
    private boolean unlock;

    public SessionVocabularyModel(String id, String name, String type, boolean unlock) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.unlock = unlock;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isUnlock() {
        return unlock;
    }
}
