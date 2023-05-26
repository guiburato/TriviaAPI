import models.TriviaSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

class TriviaApiTest {

    private TriviaApi triviaApi;

    @BeforeEach
    void setUp() {
        triviaApi = spy(new TriviaApi());
    }

    @Disabled //It's disabled because we don't have the expected result from API.
    @Test
    void generateHardGeographyTrueFalseTriviaSet() {
        TriviaSet triviaSet = triviaApi.generateHardGeographyTrueFalseTriviaSet(12);
        assertEquals("22", triviaSet.getCategory());
        assertEquals("hard", triviaSet.getDifficulty());
        assertEquals(12, triviaSet.getQuestions().size());
    }

    @Test
    void generateMediumComputerMultipleChoiceTriviaSet() {
        TriviaSet triviaSet = triviaApi.generateMediumComputerMultipleChoiceTriviaSet(10);
        assertEquals("18", triviaSet.getCategory());
        assertEquals("medium", triviaSet.getDifficulty());
        assertEquals(10, triviaSet.getQuestions().size());
    }

    @Test
    void generateEasyCustomSubjectMultipleChoiceTriviaSet() {
        TriviaSet triviaSet = triviaApi.generateEasyCustomSubjectMultipleChoiceTriviaSet(12, "11");
        assertEquals("11", triviaSet.getCategory());
        assertEquals("easy", triviaSet.getDifficulty());
        assertEquals(12, triviaSet.getQuestions().size());
    }



}
