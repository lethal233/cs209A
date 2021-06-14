package lsl.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lsl.model.Student;
import lsl.model.StudentTable;
import lsl.util.DateUtil;
import lsl.util.ValidatorUtils;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * @author Shilong Li (Lori)
 * @project CS209A_A2
 * @filename StudentEditDialogController
 * @date 2021/4/26 11:17
 */
public class StudentEditDialogController {
    @FXML
    private TextField ID;
    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<String> gender;
    @FXML
    private TextField department;
    @FXML
    private TextField gpa;
    @FXML
    private TextField creditEarned;
    @FXML
    private DatePicker birthday;

    private Student student;
    private Stage dialogStage;
    private boolean okClicked = false;
    private StudentTable studentTable;

    @FXML
    public void initialize() {
        gender.getItems().addAll("MALE", "FEMALE");
        gender.getSelectionModel().selectFirst();
        birthday.setPromptText("yyyy-mm-dd");
        birthday.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return DateUtil.format(object);
            }

            @Override
            public LocalDate fromString(String string) {
                return DateUtil.parse(string);
            }
        });
    }

    @FXML
    private void handleOk() throws ParseException {
        if (isInputValid()) {
            if (student != null && student.equals(
                    new Student(ID.getText(),
                    name.getText(),
                    gender.getValue(),
                    department.getText(),
                    Double.parseDouble(gpa.getText()),
                    Integer.parseInt(creditEarned.getText()),
                    birthday.getEditor().getText()))){
                return;
            }
            student.setID(ID.getText());
            student.setName(name.getText());
            student.setDepartment(department.getText());
            student.setGender(gender.getValue());
            student.setGpa(Double.parseDouble(gpa.getText()));
            student.setBirthday(ValidatorUtils.sdf.parse(birthday.getEditor().getText()));
            student.setCreditEarned(Integer.parseInt(creditEarned.getText()));
//            student = new Student(ID.getText(),
//                    name.getText(),
//                    gender.getValue(),
//                    department.getText(),
//                    Double.parseDouble(gpa.getText()),
//                    Integer.parseInt(creditEarned.getText()),
//                    birthday.getEditor().getText());
            okClicked = true;
            studentTable.setModified(true);
        }
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public Student getStudent() {
        return student;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setStudent(Student student) {
        this.student = student;
        ID.setText(student.getID());
        name.setText(student.getName());
        gender.setValue(student.getGender());
        department.setText(student.getDepartment());
        gpa.setText(String.valueOf(student.getGpa()));
        creditEarned.setText(String.valueOf(student.getCreditEarned()));
        birthday.setValue(DateUtil.parse(ValidatorUtils.sdf.format(student.getBirthday())));

    }

    public StudentTable getStudentTable() {
        return studentTable;
    }

    public void setStudentTable(StudentTable studentTable) {
        this.studentTable = studentTable;
    }

    private boolean isInputValid() {
        StringBuilder sb = new StringBuilder();
        if (ValidatorUtils.isEmpty(ID.getText())) {
            sb.append("Invalid ID\n");
        }
        if (ValidatorUtils.isEmpty(name.getText())) {
            sb.append("Invalid Name\n");
        }
        if (ValidatorUtils.isEmpty(department.getText())) {
            sb.append("Invalid Department\n");
        }
        if (ValidatorUtils.isEmpty(gpa.getText())) {
            sb.append("Invalid GPA\n");
        } else {
            try {
                double g = Double.parseDouble(gpa.getText());
                if (g > 4 || g < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                sb.append("No valid GPA value (must be a float between 0 and 4 (both inclusive)\n");
            }
        }
        if (ValidatorUtils.isEmpty(creditEarned.getText())) {
            sb.append("Invalid credit\n");
        } else {
            try {
                int c = Integer.parseInt(creditEarned.getText());
                if (c < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                sb.append("Invalid credit\n");
            }
        }
        if (ValidatorUtils.isEmpty(birthday.getEditor().getText())) {
            sb.append("Invalid birthday\n");
        }
        if (sb.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(sb.toString());
            alert.showAndWait();
            return false;
        }
    }
}
