package models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Question {
    private String category;
    private String difficulty;
    private String question;
    private List<String> options;
    private String correctAnswer;

    public Question(String category, String difficulty, String question, List<String> options, String correctAnswer) {
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public static Question fromApiQuestion(ApiQuestion apiQuestion) {
        String category = apiQuestion.getCategory();
        String difficulty = apiQuestion.getDifficulty();
        String question = apiQuestion.getQuestion();
        String correctAnswer = apiQuestion.getCorrectAnswer();
        List<String> incorrectAnswers = Arrays.stream(apiQuestion.getIncorrectAnswers()).collect(Collectors.toList());

        incorrectAnswers.add(correctAnswer);

        return new Question(category, difficulty, question, incorrectAnswers, correctAnswer);
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
