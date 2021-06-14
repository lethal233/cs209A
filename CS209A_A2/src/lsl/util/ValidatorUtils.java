package lsl.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * @author Shilong Li (Lori)
 * @project CS209A_A2
 * @filename ValidatorUtils
 * @date 2021/4/26 11:48
 */
public class ValidatorUtils {
    public static final String[]
            HEADERS = {"ID", "Name", "Gender", "Department", "GPA", "Credit Earned", "Birthday"};
    public static final String[]
            DISPLAY = {"ID", "Name", "Department", "GPA", "Credit Earned"};
    public static SimpleDateFormat
            sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isEmpty(String input) {
        return input == null || input.trim().length() == 0;
    }

    public static void alertMsg(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Optional<ButtonType> alertConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static void alertError(String t, String h, String c) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(t);
        alert.setHeaderText(h);
        alert.setContentText(c);
        alert.showAndWait();
    }
}
