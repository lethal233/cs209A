package lsl.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lsl.MainApp;
import lsl.util.ValidatorUtils;
import lsl.view.StudentOverviewController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Shilong Li (Lori)
 * @project CS209A_A2
 * @filename StudentTable
 * @date 2021/4/15 21:13
 */
public class StudentTable {
    private static int cnt = 0;
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private boolean modified = false;
    private MainApp mainApp;
    private Tab tab;
    private String fileName = null;

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> IDColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> departmentColumn;
    @FXML
    private TableColumn<Student, Double> GPAColumn;
    @FXML
    private TableColumn<Student, Integer> creditColumn;

    public StudentTable() {
    }

    public StudentTable(MainApp mainApp) {
        this(new ArrayList<>(), false, mainApp);
    }

    public StudentTable(List<Student> students, boolean modified, MainApp mainApp) {
        this.students.addAll(students);
        this.modified = modified;
        this.mainApp = mainApp;
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void setStudents(ObservableList<Student> students) {
        this.students = students;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    @SuppressWarnings("unchecked")
    public void init() {
        tab = new Tab("Tab - " + ++cnt);
        tab.setClosable(true);

        studentTable = new TableView<>();
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        studentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        IDColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Name");
        departmentColumn = new TableColumn<>("Department");
        GPAColumn = new TableColumn<>("GPA");
        creditColumn = new TableColumn<>("Credit Earned");
        mainApp.getTabPane().getTabs().add(tab);
        mainApp.getTabPane().getSelectionModel().selectLast();

        IDColumn.setPrefWidth(90);
        nameColumn.setPrefWidth(90);
        departmentColumn.setPrefWidth(140);
        GPAColumn.setPrefWidth(100);
        creditColumn.setPrefWidth(120);

        IDColumn.setCellValueFactory(cellData -> cellData.getValue().IDProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        GPAColumn.setCellValueFactory(cellData -> cellData.getValue().gpaProperty().asObject());
        creditColumn.setCellValueFactory(cellData -> cellData.getValue().creditEarnedProperty().asObject());
        studentTable.getColumns().addAll(IDColumn, nameColumn, departmentColumn, GPAColumn, creditColumn);

        studentTable.setItems(students);
        tab.setContent(studentTable);
        tab.setOnSelectionChanged(event -> {
            if (mainApp.getTabPane().getTabs().size() != 0) {
                try {
                    mainApp.getStudentOverviewController().showStudentDetails(
                            mainApp.getStudentTable().get(
                                    mainApp.getTabPane().getSelectionModel().getSelectedIndex())
                                    .getStudentTable().getSelectionModel().getSelectedItem());
                    String tmp = mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getFileName();
                    if (tmp != null) {
                        mainApp.getPrimaryStage().setTitle("StudentInfo -" + tmp);
                    } else {
                        mainApp.getPrimaryStage().setTitle("StudentInfo -" + mainApp.getTabPane().getSelectionModel().getSelectedItem().getText());
                    }
                } catch (IndexOutOfBoundsException ignored) {
                    mainApp.getPrimaryStage().setTitle("StudentInfo");
                }
            }
        });
        tab.setOnCloseRequest(event -> {
            if (modified) {
                Optional<ButtonType> re = ValidatorUtils.alertConfirmation("Not saved yet", "Modification not saved yet", "Do you want to save it?");
                if (re.isPresent() && re.get() == ButtonType.OK) {
                    mainApp.getRootLayoutController().handleSave();
                }
            }
            mainApp.getStudentTable().remove(this);
            mainApp.getStudentOverviewController().showStudentDetails(null);
            mainApp.getTabPane().getSelectionModel().selectLast();
//            mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().select();
        });
        tab.setOnClosed(event -> {
            try {
                String tmp = mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getFileName();
                if (tmp != null) {
                    mainApp.getPrimaryStage().setTitle("StudentInfo -" + tmp);
                } else {
                    mainApp.getPrimaryStage().setTitle("StudentInfo -" + mainApp.getTabPane().getSelectionModel().getSelectedItem().getText());
                }
            } catch (Exception ignored) {
                mainApp.getPrimaryStage().setTitle("StudentInfo");
            }
        });


        mainApp.getStudentOverviewController().showStudentDetails(null);
        studentTable.getSelectionModel().selectedItemProperty()
                .addListener(
                        ((observable, oldValue, newValue)
                                -> mainApp.getStudentOverviewController()
                                .showStudentDetails(newValue)));
    }

    public TableView<Student> getStudentTable() {
        return studentTable;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
