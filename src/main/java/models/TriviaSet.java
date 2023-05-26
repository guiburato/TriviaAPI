package models;

import java.util.List;

public class TriviaSet {
    private final String category;
    private final String difficulty;
    private final List<Question> questions;

    public TriviaSet(String category, String difficulty, List<Question> questions) {
        this.category = category;
        this.difficulty = difficulty;
        this.questions = questions;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<Question> getQuestions() {
        return questions;
    }


}

