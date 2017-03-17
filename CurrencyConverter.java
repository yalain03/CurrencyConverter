/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package currencyconverter;

import java.io.File;
import javafx.concurrent.Task;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.Formatter;

/**
 *
 * @author yves0_000
 */
public class CurrencyConverter extends Application {
  // creating controls
  ComboBox from = new ComboBox();
  ComboBox to = new ComboBox();
  TextField fromAmount = new TextField();
  Button calculate = new Button("Convert");
  Label fromLabel = new Label("From : ");
  Label toLabel = new Label("To : ");
  Label amountLabel = new Label("Amount : ");
  Label rateLabel = new Label("Rate");
  Button resultLabel = new Button("Result");
  public SingleSelectionModel genreSelectionModel;
  Task task;
  
  @Override
  public void start(Stage stage) throws Exception{    
    // assigning ids to controls
    fromLabel.setId("fromLabel");
    toLabel.setId("toLabel");
    amountLabel.setId("amountLabel");
    fromAmount.setId("from-amount");
    from.setId("from");
    to.setId("to");
    calculate.setId("calculate");
    rateLabel.setId("rate");
    resultLabel.setId("result");   
    Double rate;  
    
    // making dropdown lists for currencies
    from.getItems().addAll(
      "USD",
      "EUR",
      "JPY",
      "CNY",
      "INR"
    );
    from.getSelectionModel().selectFirst();
    to.getItems().addAll(
      "EUR",
      "USD",
      "JPY",
      "CNY",
      "INR"
    );
    to.getSelectionModel().selectFirst();
    
    // putting currencies list in choice boxes
    from.setPrefWidth(80);
    from.setPrefHeight(40);
    to.setPrefWidth(80);
    to.setPrefHeight(40);
    resultLabel.setPrefWidth(150);
    
    // first group
    HBox hbox1 = new HBox(fromLabel, from);
    hbox1.setSpacing(20);
    HBox hbox2 = new HBox(toLabel, to);
    hbox2.setSpacing(20);
    HBox hbox3 = new HBox(hbox1, hbox2, rateLabel);
    hbox3.setSpacing(110);
    hbox3.setLayoutX(60);
    hbox3.setLayoutY(120);
    
    // second group
    HBox hbox4 = new HBox(amountLabel, fromAmount);
    hbox4.setSpacing(40);
    HBox hbox5 = new HBox(hbox4, calculate);
    hbox5.setSpacing(135);
    hbox5.setLayoutX(60);
    hbox5.setLayoutY(240);
    
    // positioning the button
    resultLabel.setLayoutX(240);
    resultLabel.setLayoutY(350);
    
    //*************************************************
    
    // creating a task
    task = new Task<Void>() {
      @Override 
      public Void call() {
        while (stage.isShowing()) {
          updateMessage(Double.toString(findExchangeRate(
            from.getValue().toString(), to.getValue().toString())));
          try {
            Thread.sleep(1000);            
          } catch(InterruptedException e) {}
        }
        return null;
      }
    };   
    
    //***************************************************
    
    // event handler for calculate button
    calculate.setOnAction((event) -> {
      Double amount = Double.parseDouble(fromAmount.getText());
        Double rt = Double.parseDouble(rateLabel.getText());
        Double result = amount * rt;
        resultLabel.setText(String.format("%.2f", result));
    });
    
    //***************************************************
    
    //Group group = new Group(fromLabel, toLabel, fromAmount, calculate, resultLabel, from, to);
    Group group = new Group(hbox3, hbox5, resultLabel);
    
    Scene scene = new Scene(group, 700, 500);
    // adding css to the program
    scene.getStylesheets().add(getClass().getResource("default.css").toExternalForm());
    
    stage.setScene(scene);
    stage.setTitle("Currency Converter");
    stage.show();  
    Executors.newSingleThreadExecutor().execute(task);
    rateLabel.textProperty().bind(task.messageProperty());
  } // end function start

  private static Double findExchangeRate(String from, String to) {
    try {
            //Yahoo Finance API
      if (!(from.isEmpty() || to.isEmpty())) {
            URL url = new URL("http://finance.yahoo.com/d/quotes.csv?f=l1&s="+ from + to + "=X");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            if (line.length() > 0) {
                return Double.parseDouble(line);
            }
            reader.close();
      }
      else return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
  }
  
  public static void main(String[] args) {
    launch(args);
  } // end function main  
} // end class CurrencyConverter
