package lsl.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lsl.MainApp;
import lsl.model.Student;
import lsl.util.ValidatorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author Shilong Li (Lori)
 * @project CS209A_A2
 * @filename SearchController
 * @date 2021/4/26 10:49
 */
public class SearchController {
    @FXML
    ChoiceBox<String> searchModeChoiceBox;
    @FXML
    TextField textField;
    List<Integer> idx = new ArrayList<>();
    int current = Integer.MIN_VALUE;
    MainApp mainApp;
    Stage dialogStage;

    @FXML
    public void initialize(){
        searchModeChoiceBox.getItems().addAll("Fuzzy", "ALL");
        searchModeChoiceBox.getSelectionModel().selectFirst();
        searchModeChoiceBox.selectionModelProperty().addListener(((observable, oldValue, newValue) -> search()));
        textField.textProperty().addListener(((observable, oldValue, newValue) -> search()));
    }
    @FXML
    public void next(){
        if (current != Integer.MIN_VALUE && idx.size() != 0 && searchModeChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
            current = (current + 1) % idx.size();
            mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().select(idx.get(current));
        }
    }
    private void search(){
        mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().clearSelection();
        current = Integer.MIN_VALUE;
        idx.clear();
        if (mainApp.getTabPane().getTabs().size() == 0) {
            return;
        }
        ObservableList<Student> a = mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudents();
        String b = textField.getText();
        if (ValidatorUtils.isEmpty(b)) {
            return;
        }
        if (searchModeChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
            mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            //FUZZY
            for (int i = 0; i < a.size(); i++) {
                int cnt = a.get(i).hasContains(b);
                for (int j = 0; j < cnt; j++) {
                    idx.add(i);
                }
            }
        } else {
            mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (int i = 0; i < a.size(); i++) {
                int cnt = a.get(i).hasEquals(b);
                for (int j = 0; j < cnt; j++) {
                    idx.add(i);
                }
            }
        }
        if (idx.size() == 0) {
            current = Integer.MIN_VALUE;
        } else {
            current = 0;
            if (searchModeChoiceBox.getSelectionModel().getSelectedIndex() == 0) {
                mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().select(idx.get(0));
            } else {
                mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                for (Integer integer : idx) {
                    mainApp.getStudentTable().get(mainApp.getTabPane().getSelectionModel().getSelectedIndex()).getStudentTable().getSelectionModel().select(integer);
                }
            }
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
