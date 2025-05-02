package storemanagement.example.group_15.infrastructure.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateHelper {

    // Format đầu ra chuẩn
    private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate normalizeDate(String input) {
        String[] formats = {
                "dd/MM/yyyy",
                "yyyy/MM/dd",
                "yyyy-MM-dd"
        };

        for (String pattern : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException("Không nhận diện được định dạng ngày: " + input);
    }

}
