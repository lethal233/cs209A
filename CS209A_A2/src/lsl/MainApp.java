package lsl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lsl.model.Student;
import lsl.model.StudentTable;
import lsl.util.ValidatorUtils;
import lsl.view.RootLayoutController;
import lsl.view.SearchController;
import lsl.view.StudentEditDialogController;
import lsl.view.StudentOverviewController;
import org.apache.commons.csv.*;


public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController rootLayoutController;
    private StudentOverviewController studentOverviewController;
    private TabPane tabPane;
    private List<StudentTable> studentTable = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StudentInfo");
        initRootLayout();
        showStudentOverview();
        this.primaryStage.setOnCloseRequest(event -> {
            if (!closeAll()) {
                event.consume();
            }
        });
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStudentOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StudentOverview.fxml"));
            AnchorPane personOverview = loader.load();

            rootLayout.setCenter(personOverview);

            studentOverviewController = loader.getController();
            studentOverviewController.setMainApp(this);
            tabPane = studentOverviewController.getTabPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadStudentDataFromFile(File file) {
        try {
            Reader in = new FileReader(file.getAbsolutePath());
            CSVParser parser = CSVFormat.RFC4180.withHeader(ValidatorUtils.HEADERS)
                    .withFirstRecordAsHeader().withQuoteMode(QuoteMode.ALL).parse(in);

            List<Student> newStudentList = new ArrayList<>();
            List<CSVRecord> records = parser.getRecords();
            for (CSVRecord record : records) {
                newStudentList.add(new Student(record));
            }
            StudentTable studentTable = new StudentTable(newStudentList, false, this);
            this.studentTable.add(studentTable);
            studentTable.init();
            studentTable.getTab().setText(file.getName());

            tabPane.getSelectionModel().selectLast();

            setStudentFilePath(file);
//            this.studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).setFileName(file.getAbsolutePath());
        } catch (Exception e) {
            ValidatorUtils.alertError(
                    "Error",
                    "Could not load data",
                    "Could not load data from file:\n" + file.getPath()
            );
        }
    }

    public void showSearchDialog() {
        if (tabPane.getTabs().size() != 0) {
            try {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/Search.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Student");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                SearchController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setMainApp(this);

                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            ValidatorUtils.alertMsg(
                    "No Tab chosen",
                    "Open a table or create a table",
                    "Table does not exist");
        }
    }

    public boolean showStudentEditDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StudentEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StudentEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);

            controller.setStudentTable(studentTable.get(tabPane.getSelectionModel().getSelectedIndex()));
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveStudentDataToFile(File file) {
        try {
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            CSVPrinter printer =
                    new CSVPrinter(fw, CSVFormat.RFC4180.withHeader(ValidatorUtils.HEADERS)
                            .withQuoteMode(QuoteMode.ALL));
            for (Student s : studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).getStudents()) {
                printer.printRecord(
                        s.getID(),
                        s.getName(),
                        s.getGender(),
                        s.getDepartment(),
                        s.getGpa(),
                        s.getCreditEarned(),
                        ValidatorUtils.sdf.format(s.getBirthday()));
            }
            fw.close();
            setStudentFilePath(file);
//            this.studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).setFileName(file.getAbsolutePath());
            studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).setModified(false);
        } catch (Exception e) { // catches ANY exception
            ValidatorUtils.alertMsg(
                    "Error",
                    "Could not save data",
                    "Could not save data to file:\n" + file.getPath());
        }
    }

    public void setStudentFilePath(File file) {
//        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
//            prefs.put("filePath -"
//                            + tabPane.getSelectionModel().getSelectedItem().getText(),
//                    file.getPath());
            this.studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).setFileName(file.getAbsolutePath());
            primaryStage.setTitle("StudentInfo - " + file.getName());
        } else {
            primaryStage.setTitle("StudentInfo -" + tabPane.getSelectionModel().getSelectedItem().getText());
        }
    }

    public void addNewStudent(Student student) {
        studentTable.get
                (tabPane.getSelectionModel().getSelectedIndex())
                .getStudents().add(student);
    }

    public File getStudentFilePath() {
//        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
//        String filePath = prefs.get("filePath -"
//                + tabPane.getSelectionModel().getSelectedItem().getText(), null);
        String filePath = studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).getFileName();
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }

    public void setRootLayoutController(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }

    public StudentOverviewController getStudentOverviewController() {
        return studentOverviewController;
    }

    public void setStudentOverviewController(StudentOverviewController studentOverviewController) {
        this.studentOverviewController = studentOverviewController;
    }

    public List<StudentTable> getStudentTable() {
        return studentTable;
    }

    public void setStudentTable(List<StudentTable> studentTable) {
        this.studentTable = studentTable;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public boolean closeAll() {
        while (tabPane.getTabs().size() > 0) {
            tabPane.getSelectionModel().select(0);
            if (closeOneTable(studentTable.get(0))) {
                tabPane.getTabs().remove(0);
                studentTable.remove(0);
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean closeOneTable(StudentTable studentTable) {
        if (!studentTable.isModified()) {
            return true;
        } else {
            Optional<ButtonType> a = ValidatorUtils.alertConfirmation(
                    "Not saved yet",
                    "Not saved yet",
                    "save " + this.studentTable.get(tabPane.getSelectionModel().getSelectedIndex()).getTab().getText() + "?");
            if (a.isPresent()) {
                if (a.get() == ButtonType.OK) {
                    rootLayoutController.handleSave();
                }
                return true;
            } else {
                return false;
            }
        }
    }
}