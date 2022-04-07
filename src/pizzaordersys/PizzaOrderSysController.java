package pizzaordersys;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.PatternSyntaxException;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;           
import javax.swing.table.TableModel;    
import javax.swing.table.TableRowSorter;

public class PizzaOrderSysController  {     //implements Initializable {
   @FXML private BorderPane borderPane;
   @FXML private TextArea queryTextArea;
   @FXML private TextField filterTextField;
   @FXML private TextField queryByPhone;
   @FXML private TextField queryByLName;
   @FXML private DatePicker datepickerFrom;
   @FXML private DatePicker datepickerTo;

   // database URL, username and password
   private static final String DATABASE_URL = "jdbc:derby:pizzadb";
   private static final String USERNAME = "deitel";
   private static final String PASSWORD = "deitel";
   
   // default query retrieves all data from Authors table
   private static final String DEFAULT_QUERY = "SELECT * FROM customers";
   
   // used for configuring JTable to display and sort data
   private ResultSetTableModel tableModel;
   private TableRowSorter<TableModel> sorter;

   public void initialize() {
      queryTextArea.setText(DEFAULT_QUERY);

      // create ResultSetTableModel and display database table
      try {
         // create TableModel for results of DEFAULT_QUERY
         tableModel = new ResultSetTableModel(DATABASE_URL,            
            USERNAME, PASSWORD, DEFAULT_QUERY);                        
         
         // create JTable based on the tableModel    
         JTable resultTable = new JTable(tableModel);

         // set up row sorting for JTable
         sorter = new TableRowSorter<TableModel>(tableModel);
         resultTable.setRowSorter(sorter);             

         // configure SwingNode to display JTable, then add to borderPane
         SwingNode swingNode = new SwingNode();
         swingNode.setContent(new JScrollPane(resultTable));
         borderPane.setCenter(swingNode);
      }
      catch (SQLException sqlException) {
         displayAlert(AlertType.ERROR, "Database Error", 
            sqlException.getMessage());
         tableModel.disconnectFromDatabase(); // close connection  
         System.exit(1); // terminate application
      } 
   }

   // display an Alert dialog
   private void displayAlert(
      AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }

   // query the database and display results in JTable
   @FXML
   void submitQueryButtonPressed(ActionEvent event) {
      // perform a new query
      try {
         tableModel.setQuery(queryTextArea.getText());
      } 
      catch (SQLException sqlException) {
         displayAlert(AlertType.ERROR, "Database Error", 
            sqlException.getMessage());
         
         // try to recover from invalid user query 
         // by executing default query
         try {
            tableModel.setQuery(DEFAULT_QUERY);
            queryTextArea.setText(DEFAULT_QUERY);
         } 
         catch (SQLException sqlException2) {
            displayAlert(AlertType.ERROR, "Database Error", 
               sqlException2.getMessage());
            tableModel.disconnectFromDatabase(); // close connection  
            System.exit(1); // terminate application
         } 
      } 
   }

   // apply specified filter to results
   @FXML
   void applyFilterButtonPressed(ActionEvent event) {
      String text = filterTextField.getText();

      if (text.length() == 0) {
         sorter.setRowFilter(null);
      }
      else {
         try {
            sorter.setRowFilter(RowFilter.regexFilter(text));
         } 
         catch (PatternSyntaxException pse) {
            displayAlert(AlertType.ERROR, "Regex Error", 
               "Bad regex pattern");
         }
      }
   }

   // apply simple query to results
   @FXML
   void SimpleQueryPressed(ActionEvent event) {
       
   }
   
      // apply to Range Query results
   @FXML
   void RangeQueryPressed(ActionEvent event) {
       LocalDate ldFrom = datepickerFrom.getValue();    // Date "From"
       String dateFrom = ldFrom.toString();
       
       LocalDate ldTo = datepickerTo.getValue();        // Date "To"
       String dateTo = ldTo.toString();
       
   }

      // apply Query By Phone results
   @FXML
   void QueryByPhonePressed(ActionEvent event) {
       
   }
      // apply Query By Last Name results
   @FXML
   void QueryByLastNamePressed(ActionEvent event) {
       
   }
}
