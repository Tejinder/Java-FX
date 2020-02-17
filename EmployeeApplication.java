import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
 
public class EmployeeApplication extends Application {
 
 
	
	 private TableView<Employee> table = new TableView<Employee>();
	    private ObservableList<Employee> data =
	            FXCollections.observableArrayList(JdbcDerbyConnection.getAllEmployees());
	    final HBox hb = new HBox();
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Employee Details");
        stage.setWidth(450);
        stage.setHeight(550);
 
        final Label label = new Label("Employee Details");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory =
             new Callback<TableColumn, TableCell>() {
                 public TableCell call(TableColumn p) {
                    return new EditingCell();
                 }
             };
 
       
        
        
        TableColumn employeeIdCol = new TableColumn("Id");
        employeeIdCol.setMinWidth(100);
        employeeIdCol.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("id"));
 
        
      /*  employeeIdCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Employee, String>>() {
                @Override
                public void handle(CellEditEvent<Employee, String> t) {
                    ((Employee) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setId(t.getNewValue());
                }
             }
        );
 
 */
        TableColumn employeeNameCol = new TableColumn("Name");
        employeeNameCol.setMinWidth(100);
        employeeNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
        employeeNameCol.setCellFactory(cellFactory);
        employeeNameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Employee, String>>() {
                @Override
                public void handle(CellEditEvent<Employee, String> t) {
                    ((Employee) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    JdbcDerbyConnection.updateEmployeeName(t.getNewValue(), t.getRowValue().getId());
                    System.out.println("Id -->"+ t.getRowValue().getId());
                }
            }
        );
 
        TableColumn employeeDeptCol = new TableColumn("Department");
        employeeDeptCol.setMinWidth(200);
        employeeDeptCol.setCellValueFactory(
            new PropertyValueFactory<Employee, String>("department"));
        employeeDeptCol.setCellFactory(cellFactory);
        employeeDeptCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Employee, String>>() {
                @Override
                public void handle(CellEditEvent<Employee, String> t) {
                    ((Employee) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setDepartment(t.getNewValue());
                    
                    JdbcDerbyConnection.updateEmployeeDepartment(t.getNewValue(), t.getRowValue().getId());
                }
            }
        );
        
        TableColumn employeePositionCol = new TableColumn("Position");
        employeePositionCol.setMinWidth(200);
        employeePositionCol.setCellValueFactory(
            new PropertyValueFactory<Employee, String>("position"));
        employeePositionCol.setCellFactory(cellFactory);
        employeePositionCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Employee, String>>() {
                @Override
                public void handle(CellEditEvent<Employee, String> t) {
                    ((Employee) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setPosition(t.getNewValue());
                    
                    JdbcDerbyConnection.updateEmployeePosition(t.getNewValue(), t.getRowValue().getId());
                }
            }
        );
        
        table.setItems(data);
        table.getColumns().addAll(employeeIdCol, employeeNameCol, employeeDeptCol, employeePositionCol);
 
        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(employeeNameCol.getPrefWidth());
        final TextField addDepartment = new TextField();
        addDepartment.setMaxWidth(employeeDeptCol.getPrefWidth());
        addDepartment.setPromptText("Department");
        final TextField addPosition = new TextField();
        addPosition.setMaxWidth(employeePositionCol.getPrefWidth());
        addPosition.setPromptText("Position");
 
        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	JdbcDerbyConnection.insertEmployee(addName.getText(), addDepartment.getText(), addPosition.getText());
            	data.clear();
            	data.addAll(FXCollections.observableArrayList(JdbcDerbyConnection.getAllEmployees()));
            	addName.clear();
            	addDepartment.clear();
            	addPosition.clear();
            }
        });
 
        hb.getChildren().addAll(addName, addDepartment, addPosition, addButton);
        hb.setSpacing(3);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    
 
    class EditingCell extends TableCell<Employee, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}