package lsl.view;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import lsl.MainApp;
import lsl.model.StudentTable;
import lsl.util.ValidatorUtils;

import java.io.File;

public class RootLayoutController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleNew() {
        StudentTable studentTable = new StudentTable(mainApp);
        studentTable.init();
//        mainApp.getTabPane().getTabs().add(studentTable.getTab());
        mainApp.getStudentTable().add(studentTable);
        studentTable.setModified(true);
        mainApp.setStudentFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadStudentDataFromFile(file);
        }
    }

    @FXML
    public void handleSave() {
        if (mainApp.getTabPane().getTabs().size() == 0) {
            ValidatorUtils.alertMsg(
                    "No tab opened",
                    "No tab opened",
                    "Please open a table or create a table first."
            );
            return;
        }
        File personFile = mainApp.getStudentFilePath();
        if (personFile != null) {
            mainApp.saveStudentDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            if (!file.getPath().endsWith(".csv")) {
                file = new File(file.getPath() + ".csv");
            }
            mainApp.saveStudentDataToFile(file);
        }
    }

    @FXML
    private void handleExit() {
        if (mainApp.closeAll())
            System.exit(0);
    }

    @FXML
    private void search() {
        if (mainApp.getTabPane().getTabs().size() != 0) {
            mainApp.showSearchDialog();
        } else {
            ValidatorUtils.alertMsg(
                    "No tab chosen",
                    "No tab chosen",
                    "warning");
        }
    }
}