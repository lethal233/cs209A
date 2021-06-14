package lsl.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lsl.MainApp;
import lsl.model.Student;
import lsl.util.ValidatorUtils;

import java.text.ParseException;


/**
 * @author Shilong Li (Lori)
 * @project CS209A_A2
 * @filename StudentOverviewController
 * @date 2021/4/15 21:48
 */
public class StudentOverviewController {
    @FXML
    TabPane tabPane;
    @FXML
    private Label IDLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label departmentLabel;
    @FXML
    private Label GPALabel;
    @FXML
    private Label creditEarnedLabel;
    @FXML
    private Label birthdayLabel;

    private MainApp mainApp;

    public StudentOverviewController() {
    }

    @FXML
    public void initialize() {
        showStudentDetails(null);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

//    public Tab addNewTab() {
//        TableView<Student> a = createNewTable();
//        Tab b = new Tab("StudentInfo - " + tableCnt++);
//        b.setContent(a);
//        tabPane.getTabs().add(b);
//        return b;
////        studentTable.getStudentTable().setItems(a);
//    }

//    private TableView<Student> createNewTable() {
//        TableView<Student> a = new TableView<>();
//        TableColumn<Student, String> IDColumn = new TableColumn<>();
//        TableColumn<Student, String> nameColumn = new TableColumn<>();
//        TableColumn<Student, String> departmentColumn = new TableColumn<>();
//        TableColumn<Student, String> GPAColumn = new TableColumn<>();
//        TableColumn<Student, String> creditEarnedColumn = new TableColumn<>();
//        IDColumn.setPrefWidth(90);
//        nameColumn.setPrefWidth(90);
//        departmentColumn.setPrefWidth(140);
//        GPAColumn.setPrefWidth(100);
//        creditEarnedColumn.setPrefWidth(120);
//        IDColumn.setText("ID");
//        nameColumn.setText("Name");
//        departmentColumn.setText("Department");
//        GPAColumn.setText("GPA");
//        creditEarnedColumn.setText("Credit Earned");
//        IDColumn.setCellValueFactory(cellData -> cellData.getValue().IDProperty());
//        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
//        GPAColumn.setCellValueFactory(cellData -> cellData.getValue().gpaProperty().asString());
//        creditEarnedColumn.setCellValueFactory(cellData -> cellData.getValue().creditEarnedProperty().asString());
//        a.getColumns().addAll(IDColumn, nameColumn, departmentColumn, GPAColumn, creditEarnedColumn);
//        showStudentDetails(null);
//        a.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showStudentDetails(newValue)));
//        return a;
//    }

    @FXML
    private void handleDeleteStudent() {
        if (hasTab()) return;
        TableView<?> selectedTableView = mainApp.getStudentTable().get(tabPane.getSelectionModel().getSelectedIndex()).getStudentTable();
        int selectedIndex = selectedTableView.getSelectionModel().getSelectedIndex();
//        int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            selectedTableView.getItems().remove(selectedIndex);
            mainApp.getStudentTable().get(tabPane.getSelectionModel().getSelectedIndex()).setModified(true);
        } else {
            ValidatorUtils.alertMsg(
                    "No Selection",
                    "No Person Selected",
                    "Please select a person in the table.");
        }
    }

    private boolean hasTab() {
        if (tabPane.getTabs().size() == 0) {
            ValidatorUtils.alertMsg(
                    "No table",
                    "No table",
                    "Please create a table or open a table first"
            );
            return true;
        }
        return false;
    }

    @FXML
    private void handleNewStudent() throws ParseException {
        if (hasTab()) return;
        Student tempStudent = new Student();
        boolean okClicked = mainApp.showStudentEditDialog(tempStudent);
        if (okClicked) {
            mainApp.addNewStudent(tempStudent);
        }
    }

    @FXML
    private void handleEditStudent() {
        if (hasTab()) return;
        Student selectedStudent = mainApp.getStudentTable().get(tabPane.getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = mainApp.showStudentEditDialog(selectedStudent);
            if (okClicked) {
                showStudentDetails(selectedStudent);
            }
        } else {
            ValidatorUtils.alertMsg(
                    "No Selection",
                    "No Person Selected",
                    "Please select a person in the table."
            );
        }
    }

    public void showStudentDetails(Student student) {
        if (student != null) {
            IDLabel.setText(student.getID());
            nameLabel.setText(student.getName());
            genderLabel.setText(student.getGender());
            departmentLabel.setText(student.getDepartment());
            GPALabel.setText(String.valueOf(student.getGpa()));
            creditEarnedLabel.setText(String.valueOf(student.getCreditEarned()));
            birthdayLabel.setText(ValidatorUtils.sdf.format(student.getBirthday()));
        } else {
            IDLabel.setText("");
            nameLabel.setText("");
            genderLabel.setText("");
            departmentLabel.setText("");
            GPALabel.setText("");
            creditEarnedLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
