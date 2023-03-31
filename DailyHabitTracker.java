import java.time.LocalDate;
import java.util.function.Function;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;


/* LINK External sources */
/* 
* [JavaFX API Documentation](https://docs.oracle.com/en/java/javase/19/)
* [TableView](https://docs.oracle.com/javafx/2/ui_controls/table-view.htm)
* [ProgressBar](https://docs.oracle.com/javafx/2/ui_controls/progress.htm)
* [DatePicker](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/date-picker.htm)
* [How to add buttons to JavaFX TableViews](https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view)
*/

public class DailyHabitTracker extends Application 

{

/* LINK Global Variables */
/* ================================================================================================== */

    private double tasksCompleted = 0.0;
    private double tasksTotal = 0.0;
    private LocalDate currDateOnPicker = LocalDate.now();
    private LocalDate currDate = LocalDate.now();
    private Map<LocalDate, ObservableList<Task>> database = new HashMap<>();


/* LINK Global Components */
/* ================================================================================================== */

    private ProgressBar pbar;
    private TableView<Task> table;
    private DatePicker calendarPicker;
    private DatePicker currDatePicker = new DatePicker(LocalDate.now());
    private TextField currDateField;

/* LINK Numeric Constants */
/* ================================================================================================== */

    private final int FIELD_DROPDOWN_WIDTH = 100;
    private final int BUTTON_WIDTH = 175;
    private final int MARGIN_SAMECELL = 15;
    private final int MARGIN_DIFFCELL = 25;
    private final double PBAR_HEIGHT = 21;
    private final double PBAR_WIDTH = 100;
    private final int CALENDAR_WIDTH = 275;
    private final int END_BUTTON_WIDTH = 200;

    private final int WINDOW_HEIGHT = 450;
    private final int WINDOW_WIDTH = 700;

    private final int COL_CHECKBOX_WIDTH = 28;
    private final int COL_TASKNAME_WIDTH = 305;
    // private final int COL_DATE_WIDTH = 95;
    private final int COL_DELETE_WIDTH = 32;

/* LINK Margins */
/* ================================================================================================== */

    private final Insets PADDING_GRIDPANE = new Insets(15, 20, 20, 20); 
    private final Insets MARGINS_TABLE = new Insets(10, 0, 0, 0); 
    private final Insets MARGINS_PBAR = new Insets(0, 0, 10, 0);   
    private final Insets MARGINS_ADD_BUTTON = new Insets(10, 0, 0, 15); 
    private final Insets MARGINS_NEW_TASKFIELD = new Insets(10, 0, 0, 0); 
    private final Insets MARGINS_CALENDAR_BOX = new Insets(0, 0, 0, 15); 
    private final Insets MARGINS_CALENDAR = new Insets(10, 0, 20, 0); 
    private final Insets MARGINS_CURR_DATE = new Insets(0, 10, 0, 5); 

/* LINK GridPane coordinate constants */
/* ================================================================================================== */

    private final GridCoords coordProgBar = new GridCoords(0, 0, 2, 1);
    private final GridCoords coordTable = new GridCoords(0, 1);
    private final GridCoords coordAddField = new GridCoords(0, 2);
    private final GridCoords coordCalendar = new GridCoords(1, 1);
    private final GridCoords coordAddButton = new GridCoords(1, 2);
    

/* SECTION Start Function */
/* ================================================================================================== */

    public void start(Stage primaryStage) 
    {
        /* LINK Create tab & grid pane */
        /* ---------------------------------------------------------- */

        // TabPane mainPane = new TabPane();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(PADDING_GRIDPANE);

        /* 
        Tab todo = new Tab("To-do List", gridPane);
        todo.setClosable(false);
        mainPane.getTabs().add(todo);
        */

        /* LINK Create Components */
        /* ---------------------------------------------------------- */
        
        table = createTable();
        pbar = createPBar(primaryStage);
        
        Label tableTitle = createTableTitle();
        VBox calendar = createCalendar();
        HBox currDateBox = createCurrDate(calendar);
        
        
        TextField newTaskField = createTextField();
        Button addButton = createAddButton(newTaskField);
        VBox endButton = createEndButton(calendar);
        
        VBox calendarAndButton = new VBox(currDateBox, calendar, endButton);
        VBox tableAndTitle = new VBox(tableTitle, table);
        populateDatabase();

        /* LINK Add Components to Pane */
        /* ---------------------------------------------------------- */

        gridPane.add(pbar, coordProgBar.col(), coordProgBar.row(), coordProgBar.colSpan(), coordProgBar.rowSpan());
        gridPane.add(tableAndTitle, coordTable.col(), coordTable.row());
        gridPane.add(newTaskField, coordAddField.col(), coordAddField.row());
        gridPane.add(addButton, coordAddButton.col(), coordAddButton.row());
        gridPane.add(calendarAndButton, coordCalendar.col(), coordCalendar.row());
    
        /* LINK Configure Pane component margins */
        /* ---------------------------------------------------------- */

        gridPane.setMargin(pbar, MARGINS_PBAR);
        gridPane.setMargin(addButton, MARGINS_ADD_BUTTON);
        gridPane.setMargin(newTaskField,MARGINS_NEW_TASKFIELD);
        gridPane.setMargin(calendarAndButton,MARGINS_CALENDAR_BOX);

        calendarAndButton.setMargin(calendar, MARGINS_CALENDAR);
        tableAndTitle.setMargin(table, MARGINS_TABLE);
        tableAndTitle.setAlignment(Pos.CENTER);

        table.disableProperty().bind(Bindings.notEqual(calendarPicker.valueProperty(), currDatePicker.valueProperty()));

        /* LINK Configure Pane */
        /* ---------------------------------------------------------- */

        primaryStage.setTitle("Daily Habit Tracker");
        primaryStage.setScene(new Scene(gridPane, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }
/* !SECTION */

/* SECTION Functions */
/* ================================================================================================== */

/* LINK Create progress bar */
/* ================================================================================================== */

    public ProgressBar createPBar(Stage stage) 
    {
        ProgressBar pbar = new ProgressBar(0.0);
        pbar.setMinHeight(PBAR_HEIGHT);
        pbar.setMinWidth(PBAR_WIDTH);
        pbar.setStyle("-fx-accent: #00d700");

        pbar.prefWidthProperty().bind(Bindings.divide(stage.widthProperty(), 1.0));

        return pbar;
    }

/* LINK Create table title */
/* ================================================================================================== */

    public Label createTableTitle()
    {
        Label tableTitle = new Label("This Day's To-do List");

        Font font = new Font("Calibri", 18);
        tableTitle.setFont(font);
        
        return tableTitle;
    }

/* SECTION Create task table */
/* ================================================================================================== */

    public TableView<Task> createTable() 
    {
        /* LINK Table */
        /* ---------------------------------------------------------- */

        TableView<Task> taskTable = new TableView<>();
        taskTable.setEditable(true);
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        taskTable.setMaxWidth(COL_CHECKBOX_WIDTH + COL_DELETE_WIDTH + COL_TASKNAME_WIDTH);
        taskTable.setPlaceholder(new Label("Your to-do list for this day is empty."));

        /* LINK Checkbox Column */
        /* ---------------------------------------------------------- */

        TableColumn<Task, Boolean> colCompleted = new TableColumn<>();
        colCompleted.setCellValueFactory(
            new PropertyValueFactory<>("isCompleted")
        );
    
        colCompleted.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Integer idx) 
            {
                return taskTable.getItems().get(idx).isCompletedProperty();
            }
        }));

        colCompleted.setReorderable(false);
        colCompleted.setResizable(false);
        colCompleted.setMaxWidth(COL_CHECKBOX_WIDTH);
        colCompleted.setStyle( "-fx-alignment: CENTER;");

        /* 
        taskTable.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.disableProperty().bind(
                     Bindings.selectBoolean(row.itemProperty(), "isCompleted").isEqualTo(new SimpleBooleanProperty(true)));
            return row;
        });
        */
      
        /* LINK Task name column */
        /* ---------------------------------------------------------- */

        TableColumn<Task, String> colName = new TableColumn<>("Task");
        colName.setCellValueFactory(
            new PropertyValueFactory<Task, String>("name"));

        /* 
        // Line allows users to edit task name seamlessly, but may not be desirable
        colName.setCellFactory(new TextFieldCellFactory());
        */
        colName.setReorderable(false);
        colName.setResizable(false);
        colName.setPrefWidth(COL_TASKNAME_WIDTH);

        colName.setStyle( "-fx-alignment: CENTER-LEFT;");

        /* LINK Date Column */
        /* ---------------------------------------------------------- */

        /* 
        TableColumn<Task, LocalDate> colDate = new TableColumn<>("First Created");
        colDate.setCellValueFactory(
            new PropertyValueFactory<Task, LocalDate>("dateCreated"));

        colDate.setReorderable(false);
        colDate.setResizable(false);
        colDate.setPrefWidth(COL_DATE_WIDTH);
        colDate.setStyle( "-fx-alignment: CENTER;");
        */
        
        /* LINK Delete Button Column */
        /* ---------------------------------------------------------- */

        TableColumn colDelete = new TableColumn();

        colDelete.setCellFactory(ButtonTableCell.<Task>forTableColumn("X", (Task task) -> 
        {
            removeTask(taskTable, task);
            return task;
        }));    

        colDelete.setReorderable(false);
        colDelete.setResizable(false);
        colDelete.setSortable(false);
        colDelete.setMaxWidth(COL_DELETE_WIDTH);
        colDelete.setStyle( "-fx-alignment: CENTER;");

        /* LINK Finalize */
        /* ---------------------------------------------------------- */

        taskTable.getColumns().addAll(colCompleted, colName, colDelete);

        return taskTable;
    }

/* !SECTION */

/* LINK Create current date HBox */
/* ================================================================================================== */

public HBox createCurrDate(VBox calendar) {
    currDateField = new TextField(currDate.toString());
    currDateField.setEditable(false);

    Label currDateLabel = new Label("Today is:");
    HBox currDateBox = new HBox(currDateLabel, currDateField);

    currDateBox.setMargin(currDateLabel, MARGINS_CURR_DATE);
    currDateBox.setAlignment(Pos.CENTER_LEFT);

    return currDateBox;
}

/* LINK Create calendar */
/* ================================================================================================== */

    public VBox createCalendar() {
        calendarPicker = new DatePicker();
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isAfter(currDate))
                            {
                                setDisable(true);
                                setStyle("-fx-background-color: #dddddd;");
                            }

                            // Hardcoded date cell coloring for demonstration purposes

                            if (item.isEqual(LocalDate.now().minusDays(5)))
                            {
                                setStyle("-fx-background-color: #F8696B;");
                            }

                            if (item.isEqual(LocalDate.now().minusDays(4)))
                            {
                                setStyle("-fx-background-color: #FBAA77;");
                            }

                            if (item.isEqual(LocalDate.now().minusDays(3)))
                            {
                                setStyle("-fx-background-color: #FFEB84;");
                            }

                            if (item.isEqual(LocalDate.now().minusDays(2)))
                            {
                                setStyle("-fx-background-color: #B1D580;");
                            }

                            if (item.isEqual(LocalDate.now().minusDays(1)))
                            {
                                setStyle("-fx-background-color: #63BE7B;");
                            }
                            // Disable and style a date cell when it is before the current date
                            // if (item.isBefore(
                            //         checkInDatePicker.getValue().plusDays(1))
                            //     ) {
                            //         setDisable(true);
                            //         setStyle("-fx-background-color: #ffc0cb;");
                            // }

                            // // Tooltip for hovering over a date
                            // long p = ChronoUnit.DAYS.between(
                            //         checkInDatePicker.getValue(), item
                            // );
                            // setTooltip(new Tooltip(
                            //     "You're about to stay for " + p + " days")
                            // );
                    }
                };
            }
        };
        calendarPicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                currDateOnPicker = newValue;

                // !IMPORTANT Update table data on date change
                database.put(oldValue, table.getItems());

                if (database.get(newValue) == null) 
                    table.setItems(FXCollections.observableArrayList());
                else 
                    table.setItems(database.get(newValue));
                updatePBar();
            }
        });

        calendarPicker.setDayCellFactory(dayCellFactory);
        calendarPicker.setValue(LocalDate.now());

        DatePickerSkin calendar = new DatePickerSkin(calendarPicker);
        Node calendarNode = calendar.getPopupContent();
        calendarNode.setStyle("-fx-background-color: transparent;");

        VBox calendarBox = new VBox(calendarNode);
        calendarBox.setAlignment(Pos.TOP_LEFT);
        calendarBox.setMaxWidth(CALENDAR_WIDTH);

        return calendarBox;
    }

/* LINK Create text field to add new tasks*/
/* ================================================================================================== */

    public TextField createTextField() 
    {
        TextField newTaskField = new TextField();
        newTaskField.setPrefSize(150, 25);
        newTaskField.disableProperty().bind(Bindings.notEqual(calendarPicker.valueProperty(), currDatePicker.valueProperty()));
        
        return newTaskField;
    }

/* SECTION Create add button */
/* ================================================================================================== */

    public Button createAddButton(TextField addField) 
    {
        Button addButton = new Button("Add New Task");
       
        addButton.setMinWidth(100);
        

        /* LINK Button Handler */
        /* ---------------------------------------------------------- */

        addButton.setOnAction(evt -> {
            String textInput = addField.getText();
            if (textInput != null && !textInput.isEmpty()) {
                Task task = new Task(textInput, false, currDateOnPicker);
                incrementPBar(0, 1);
                // Add event handler to task
                task.isCompletedProperty().addListener((obs, prev, curr) -> {
                    if (curr) {
                        incrementPBar(1, 0);
                    } else if (!curr) {
                        incrementPBar(-1, 0);
                    }
                });
                table.getItems().add(task);
                addField.setText(null);
            }
        });

        addButton.disableProperty().bind(Bindings.or(
            Bindings.notEqual(calendarPicker.valueProperty(), currDatePicker.valueProperty()), 
            Bindings.isEmpty(addField.textProperty())
        ));
        
        return addButton;

        /* !SECTION */
    }

/* SECTION Create end day button */
/* ================================================================================================== */

public VBox createEndButton(VBox calendar) 
{
    Button endButton = new Button("End the Current Day");
   
    endButton.setMinWidth(100);
    

    /* LINK Button Handler */
    /* ---------------------------------------------------------- */

    endButton.setOnAction(evt -> {
        currDateOnPicker = currDateOnPicker.plusDays(1);
        currDate = currDate.plusDays(1);

        calendarPicker.setValue(currDateOnPicker);
        currDatePicker.setValue(currDate);

        currDateField.setText(currDate.toString());

        ObservableList<Task> prevDayTasks = database.get(currDate.minusDays(1));
        if (prevDayTasks != null) {
            for (Task prevTask : prevDayTasks) {
                Task taskClone = new Task(prevTask);
                taskClone.isCompletedProperty().addListener((obs, prev, curr) -> {
                    if (curr) {
                        incrementPBar(1, 0);
                    } else if (!curr) {
                        incrementPBar(-1, 0);
                    }
                });

                table.getItems().add(taskClone);
            }
        }

        resetPBar();
    });

    endButton.disableProperty().bind(Bindings.notEqual(calendarPicker.valueProperty(), currDatePicker.valueProperty()));
    endButton.prefWidthProperty().bind(Bindings.divide(calendar.widthProperty(), 1.0));
    VBox box = new VBox(endButton);
    box.setAlignment(Pos.TOP_LEFT);

    return box;

    /* !SECTION */
}

/* LINK Delete Task Button */
/* ================================================================================================== */

    public void removeTask(TableView<Task> table, Task toRemove) {
        table.getItems().remove(toRemove);
        if (toRemove.getCompleted()) {
            incrementPBar(-1, -1);
        } else {
            incrementPBar(0, -1);
        }
    }

/* LINK Update ProgressBar */
/* ================================================================================================== */

    public void incrementPBar(int updateCompleted, int updateTotal) {
        tasksCompleted += updateCompleted;
        tasksTotal += updateTotal;
        pbar.setProgress(tasksCompleted / tasksTotal);
    }

    public void updatePBar() {
        tasksCompleted = 0;
        for (Task task : table.getItems()) {
            if (task.getCompleted())
                tasksCompleted++;
        }
        tasksTotal = table.getItems().size();
        pbar.setProgress(tasksCompleted / tasksTotal);
    }

    public void resetPBar() {
        tasksCompleted = 0;
        tasksTotal = table.getItems().size();
        pbar.setProgress(0);
    }

/* LINK Prepopulate Database with hardcoded data for demonstration */
/* ================================================================================================== */
    private void populateDatabase() {
        ObservableList<Task> lowCompletion = FXCollections.observableArrayList();
        lowCompletion.add(new Task("Do 1 Leetcode Problem", false));
        lowCompletion.add(new Task("Run for 15 minutes", false));
        lowCompletion.add(new Task("Practice aim training for 10 minutes", false));
        lowCompletion.add(new Task("Watch 20 minutes of the lecture videos", false));
        database.put(LocalDate.now().minusDays(5), lowCompletion);

        ObservableList<Task> mediumCompletion = FXCollections.observableArrayList();
        mediumCompletion.add(new Task("Do 1 Leetcode Problem", false));
        mediumCompletion.add(new Task("Run for 15 minutes", true));
        mediumCompletion.add(new Task("Practice aim training for 10 minutes", false));
        database.put(LocalDate.now().minusDays(4), mediumCompletion);

        ObservableList<Task> highCompletion1 = FXCollections.observableArrayList();
        highCompletion1.add(new Task("Do 1 Leetcode Problem", true));
        highCompletion1.add(new Task("Run for 15 minutes", true));
        highCompletion1.add(new Task("Watch 20 minutes of the lecture videos", false));
        highCompletion1.add(new Task("Practice aim training for 10 minutes", false));
        database.put(LocalDate.now().minusDays(3), highCompletion1);

        ObservableList<Task> highCompletion2 = FXCollections.observableArrayList();
        highCompletion2.add(new Task("Do 1 Leetcode Problem", true));
        highCompletion2.add(new Task("Run for 15 minutes", true));
        highCompletion2.add(new Task("Watch 20 minutes of the lecture videos", false));
        database.put(LocalDate.now().minusDays(2), highCompletion2);

        ObservableList<Task> highCompletion3 = FXCollections.observableArrayList();
        highCompletion3.add(new Task("Do 1 Leetcode Problem", true));
        highCompletion3.add(new Task("Run for 15 minutes", true));
        highCompletion3.add(new Task("Watch 20 minutes of the lecture videos", true));
        database.put(LocalDate.now().minusDays(1), highCompletion3);

    }


/* !SECTION */

/* SECTION Subclasses */
/* ================================================================================================== */

    /* LINK Tasks */
    /* ---------------------------------------------------------- */

    public static class Task 
    {
        private StringProperty name;
        private BooleanProperty isCompleted;
        //private ObjectProperty<LocalDate> dateCreated;

        public Task(String name, boolean completed, LocalDate date) 
        {
            
            this.name = new SimpleStringProperty(name);
            this.isCompleted = new SimpleBooleanProperty(completed);
            //this.dateCreated = new SimpleObjectProperty<LocalDate>(date);
        }

        public Task(String name, boolean completed) 
        {
            this(name, completed, LocalDate.now());
        }

        public Task(Task toClone) {
            this.name = toClone.name;
            this.isCompleted = new SimpleBooleanProperty(false);
        }

        public StringProperty  nameProperty  () { return name;   }
        public BooleanProperty isCompletedProperty() { return isCompleted; }
        //public ObjectProperty<LocalDate> dateCreatedProperty() { return dateCreated; }

        public String getName() { return this.nameProperty().get(); }

        public void setName(String name) { this.name.set(name); }

        public boolean getCompleted() { return this.isCompletedProperty().get(); }

        public void setCompleted(final boolean on) { this.isCompleted.set(on); }

        //public LocalDate getDate() { return this.dateCreatedProperty().get(); }

        @Override
        public String toString() { return getName(); }

    }

    /* LINK GridPane Coordiantes */
    /* ---------------------------------------------------------- */

    static class GridCoords 
    {
        private final int col;
        private final int row;
        private final int colSpan;
        private final int rowSpan;

        public GridCoords(int col, int row) 
        {
            this.col = col;
            this.row = row;
            this.colSpan = 1;
            this.rowSpan = 1;
        }

        public GridCoords(int col, int row, int colSpan, int rowSpan) 
        {
            this.col = col;
            this.row = row;
            this.colSpan = colSpan;
            this.rowSpan = rowSpan;
        }

        public int col() { return col; }
        public int row() { return row; }
        public int colSpan() { return colSpan; }
        public int rowSpan() { return rowSpan; }

    }

    /* LINK Button table cells */
    /* Creates table cells with buttons - compatible with event handlers */
    /* ---------------------------------------------------------- */
    /* Source: https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view */

    static class ButtonTableCell<S> extends TableCell<S, Button> {

        private final Button actionButton;

        public ButtonTableCell(String label, Function< S, S> function) {
            this.getStyleClass().add("action-button-table-cell");

            this.actionButton = new Button(label);
            this.actionButton.setOnAction((ActionEvent e) -> {
                function.apply(getCurrentItem());
            });
            this.actionButton.setMaxWidth(10);
            this.actionButton.setFont(new Font("Calibri", 11));
        }

        public S getCurrentItem() {
            return (S) getTableView().getItems().get(getIndex());
        }

        public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function< S, S> function) {
            return param -> new ButtonTableCell<>(label, function);
        }

        @Override
        public void updateItem(Button item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
            } else {                
                setGraphic(actionButton);
            }
        }
    }

  /* !SECTION */
}