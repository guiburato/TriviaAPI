import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ApiResponse;
import models.Question;
import models.TriviaSet;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TriviaApi {

    private static final String TOKEN_URL = "https://opentdb.com/api_token.php?command=request";
    private static final String API_URL = "https://opentdb.com/api.php";
    private static final String CSV_FILE_PATH = "./csv/trivia.csv";

    private String sessionToken;

    public static void main(String[] args) {
        TriviaApi triviaApi = new TriviaApi();
        TriviaSet hardGeographySet = triviaApi.generateHardGeographyTrueFalseTriviaSet(12);
        TriviaSet mediumComputerSet = triviaApi.generateMediumComputerMultipleChoiceTriviaSet(10);
        TriviaSet easyCustomSubjectSet = triviaApi.generateEasyCustomSubjectMultipleChoiceTriviaSet(12, "11");

        triviaApi.writeTriviaSetsToCsv(hardGeographySet, mediumComputerSet, easyCustomSubjectSet);
    }

    public TriviaApi() {
        try {
            sessionToken = generateSessionToken();
        } catch (IOException e) {
            System.out.println("Failed to generate session token: " + e.getMessage());
        }
    }

    public TriviaSet generateHardGeographyTrueFalseTriviaSet(int amount) {
        return generateTriviaSet("22", "hard", "boolean", amount);
    }

    public TriviaSet generateMediumComputerMultipleChoiceTriviaSet(int amount) {
        return generateTriviaSet("18", "medium", "multiple", amount);
    }

    public TriviaSet generateEasyCustomSubjectMultipleChoiceTriviaSet(int amount, String subject) {
        return generateTriviaSet(subject, "easy", "multiple", amount);
    }

    private TriviaSet generateTriviaSet(String category, String difficulty, String type, int amount) {
        OkHttpClient client = new OkHttpClient();
        String url = buildApiUrl(category, difficulty, type, amount);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                ApiResponse apiResponse = gson.fromJson(response.body().string(), ApiResponse.class);

                List<Question> questions = apiResponse.getResults().stream()
                        .map(Question::fromApiQuestion)
                        .collect(Collectors.toList());
                return new TriviaSet(category, difficulty, questions);

            } else {
                System.out.println("Trivia API request failed with code: " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Trivia API request failed: " + e.getMessage());
        }

        return new TriviaSet(category, difficulty, new ArrayList<>());
    }

    public String buildApiUrl(String category, String difficulty, String type, int amount) {
        return API_URL +
                "?amount=" + amount +
                "&category=" + category +
                "&difficulty=" + difficulty +
                "&type=" + type +
                "&encode=url3986" +
                "&token=" + sessionToken;
    }

    private String generateSessionToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new GsonBuilder().create();
            ApiResponse apiResponse = gson.fromJson(response.body().string(), ApiResponse.class);
            return apiResponse.getToken();
        }
    }

    private String decodeText(String text) {
        try {
            return URLDecoder.decode(text, StandardCharsets.UTF_8.toString());

        } catch (Exception e) {
            System.out.println("Failed to decode text: " + e.getMessage());
            return text;
        }
    }

    private void writeTriviaSetsToCsv(TriviaSet... triviaSets) {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (TriviaSet triviaSet : triviaSets) {
                Sheet sheet = workbook.createSheet(decodeText(decodeText(triviaSet.getDifficulty())));

                // Header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Question");
                headerRow.createCell(1).setCellValue("Options");
                headerRow.createCell(2).setCellValue("Correct Answer");

                List<Question> questions = triviaSet.getQuestions();
                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    Row row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(decodeText(question.getQuestion()));
                    row.createCell(1).setCellValue(String.join(", ", question.getOptions().stream().map(this::decodeText).collect(Collectors.toList())));
                    row.createCell(2).setCellValue(decodeText(question.getCorrectAnswer()));
                }
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(CSV_FILE_PATH)) {
                workbook.write(fileOutputStream);
                System.out.println("Trivia sets written to CSV file: " + CSV_FILE_PATH);
            } catch (IOException e) {
                System.out.println("Failed to write trivia sets to CSV: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Failed to create workbook: " + e.getMessage());
        }
    }
}
