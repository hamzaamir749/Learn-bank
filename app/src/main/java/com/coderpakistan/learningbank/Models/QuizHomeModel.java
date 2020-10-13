package com.coderpakistan.learningbank.Models;

public class QuizHomeModel {
    private String id, name;
    private boolean unlock;

    public QuizHomeModel(String id, String name, boolean unlock) {
        this.id = id;
        this.name = name;
        this.unlock = unlock;
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
