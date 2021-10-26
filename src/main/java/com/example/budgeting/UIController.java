package com.example.budgeting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;


public class UIController implements Initializable {

    //prepare all UI items
    @FXML
    private ChoiceBox<String> inputAccount;

    @FXML
    private ChoiceBox<String> inputTransaction;

    @FXML
    private ChoiceBox<String> inputCategory;

    @FXML
    private ChoiceBox<String> inputToAccount;

    @FXML
    private DatePicker inputDate;

    @FXML
    private TextField inputAmount;

    @FXML
    private TextField inputNote;

    @FXML
    private TextField inputNewAccountName;

    @FXML
    private ChoiceBox<String> inputNewAccountType;

    @FXML
    private ChoiceBox<Account> inputRemoveAccount;

    @FXML
    private ChoiceBox<Account> inputAccountStatistics;

    @FXML
    private TableView<Transaction> myTable;

    @FXML
    private TableColumn<Transaction, String> transactionID;

    @FXML
    private TableColumn<Transaction, Account> transactionAccount;

    @FXML
    private TableColumn<Transaction, String> transactionType;

    @FXML
    private TableColumn<Transaction, Date> transactionDate;

    @FXML
    private TableColumn<Transaction, Double> transactionAmount;

    @FXML
    private TableColumn<Transaction, String> transactionCategory;

    @FXML
    private TableColumn<Transaction, String> transactionNote;

    //to store account info
    private HashMap<String, Account> accountList = new HashMap<String, Account>();

    //to show transactions in the table
    private ObservableList<Transaction> transactionList;


    public void setInitialValues(){
        Account saving = new Account("Saving", "WELLS FARGO Debit");
        Account payroll = new Account("Payroll", "BOA Platinum");
        Account creditCard = new Account("Credit Card", "CITI Rewards+");

        accountList.put(saving.toString(), saving);
        accountList.put(payroll.toString(), payroll);
        accountList.put(creditCard.toString(), creditCard);

        try {
            new Expense("2021-09-09",17.5, saving, "Relaxation","Movie Ticket");
            new Expense("2021-09-10",20.5, saving, "Food", "Water");
            new Expense("2021-09-14",4.5, saving,"Food","Chips" );
            new Income("2021-09-11",2000, payroll,"Salary");
            new Transfer("2021-09-12",1000, payroll, saving, "Retirement");
            new Expense("2021-09-15",800, creditCard,"Utilities","iPhone13" );
            new Transfer("2021-09-27",900, payroll, creditCard, "Monthly Payment");

        } catch (ParseException e) {}

        ArrayList<Transaction> listAll = new ArrayList<Transaction>();
        listAll.addAll(payroll.getTransactionList());
        listAll.addAll(saving.getTransactionList());
        listAll.addAll(creditCard.getTransactionList());

        this.transactionList = FXCollections.observableArrayList(listAll);

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setInitialValues();

        try {
            transactionID.setCellValueFactory(new PropertyValueFactory<Transaction, String>("id"));
            transactionAccount.setCellValueFactory(new PropertyValueFactory<Transaction, Account>("account"));
            transactionType.setCellValueFactory(new PropertyValueFactory<Transaction, String>("type"));
            transactionDate.setCellValueFactory(new PropertyValueFactory<Transaction, Date>("date"));
            transactionDate.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
            transactionAmount.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));
            transactionCategory.setCellValueFactory(new PropertyValueFactory<Transaction, String>("category"));
            transactionNote.setCellValueFactory(new PropertyValueFactory<Transaction, String>("note"));

        } catch (IllegalStateException e) {}


        myTable.setItems(this.transactionList);


        inputAccount.setOnAction(this::transactionOption);
        inputTransaction.setOnAction(this::categoryOption);
        this.clearForm();

    }

    public void transactionOption(ActionEvent event){
        inputTransaction.getItems().clear();
        inputCategory.getItems().clear();

        //ensure user cannot select value in wrong fields
        if (!inputAccount.getItems().isEmpty()){
            if (accountList.get(inputAccount.getValue()).getType() == "Saving"){
                String[] transactionType = {"Expense", "Transfer"};
                inputTransaction.setValue("Expense");
                inputTransaction.getItems().addAll(transactionType);
            }else if (accountList.get(inputAccount.getValue()).getType() == "Payroll"){
                String[] transactionType = {"Expense", "Income", "Transfer"};
                inputTransaction.setValue("Expense");
                inputTransaction.getItems().addAll(transactionType);
            }else{
                String[] transactionType = {"Expense"};
                inputTransaction.setValue("Expense");
                inputTransaction.getItems().addAll(transactionType);
            }
        }

    }

    public void categoryOption(ActionEvent event) {

        inputCategory.getItems().clear();
        if (inputTransaction.getValue() == "Expense") {
            //set default list of category
            String[] categories = {"Food", "Clothes", "Service", "House", "Relaxation", "Transportation", "Utilities", "Others"};
            inputCategory.setValue("Food");
            inputCategory.getItems().addAll(categories);
        }

        //ensure user cannot select value in wrong fields
        if (inputTransaction.getValue() != "Transfer") {
            inputToAccount.getItems().clear();
        } else {
            if (accountList.get(inputAccount.getValue()).getType() == "Saving"){
                inputToAccount.getItems().clear();

                ArrayList<String> toAccount = new ArrayList<String>();

                for (Account account: accountList.values()) {
                    if (account.toString() != inputAccount.getValue()){
                        toAccount.add(account.toString());
                    }
                }

                inputToAccount.getItems().addAll(toAccount);
            } else if (accountList.get(inputAccount.getValue()).getType() == "Payroll"){
                inputToAccount.getItems().clear();
                ArrayList<String> toAccount = new ArrayList<String>();

                for (Account account: accountList.values()) {
                    if (account.toString() != inputAccount.getValue()){
                        toAccount.add(account.toString());
                    }
                }
                inputToAccount.setValue(toAccount.get(0));
                inputToAccount.getItems().addAll(toAccount);
            }
        }

    }

    public void addTransaction(ActionEvent event) throws ParseException {
        try{
            if (inputTransaction.getValue() == "Expense"){
                new Expense(inputDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Math.abs(Double.parseDouble(inputAmount.getText())),accountList.get(inputAccount.getValue()),inputCategory.getValue(), inputNote.getText());

            } else if (inputTransaction.getValue() == "Transfer"){
                new Transfer(inputDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Math.abs(Double.parseDouble(inputAmount.getText())),accountList.get(inputAccount.getValue()),accountList.get(inputToAccount.getValue()), inputNote.getText());

            }else{
                new Income(inputDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Math.abs(Double.parseDouble(inputAmount.getText())),accountList.get(inputAccount.getValue()), inputNote.getText());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please input correct information!");
            alert.showAndWait();

        }


        this.updateTable();
        this.clearForm();
    }

    public void clearForm(){
        inputAccount.getItems().clear();
        inputTransaction.getItems().clear();
        inputCategory.getItems().clear();
        inputToAccount.getItems().clear();
        inputDate.setValue(null);
        inputAmount.setText("");
        inputNote.setText("");

        // set default value for account type
        inputNewAccountType.getItems().clear();
        String[] newAccountType = {"Saving", "Payroll", "Credit Card"};
        inputNewAccountType.getItems().addAll(newAccountType);
        inputNewAccountType.setValue("Saving");
        inputNewAccountName.clear();

        inputAccount.getItems().addAll(accountList.keySet());

        inputRemoveAccount.getItems().clear();
        inputRemoveAccount.getItems().addAll(accountList.values());

        inputAccountStatistics.getItems().clear();
        inputAccountStatistics.getItems().addAll(accountList.values());

        myTable.getSortOrder().add(transactionDate);

    }

    public void updateTable(){
        ArrayList<Transaction> listAll = new ArrayList<Transaction>();

        for (Account account: this.accountList.values()) {
            listAll.addAll(account.getTransactionList());
        }

        this.transactionList = FXCollections.observableArrayList(listAll);
        myTable.setItems(this.transactionList);
    }

    public void removeTransaction(ActionEvent event){
        if (myTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please select a transaction.");
            alert.showAndWait();
        }else {
            Transaction selectedTransaction = myTable.getSelectionModel().getSelectedItem();
            selectedTransaction.removeTransaction();
            updateTable();
            clearForm();

        }

    }


    public void addAccount(ActionEvent event){
        if (accountList.containsKey(inputNewAccountName.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            alert.setHeaderText("The name is used. Please try another name.");
            alert.showAndWait();
            clearForm();
            return;
        }else if (inputNewAccountName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            alert.setHeaderText("The name cannot be empty.");
            alert.showAndWait();
            clearForm();
            return;
        }

        Account account = new Account(inputNewAccountType.getValue(),inputNewAccountName.getText());

        accountList.put(account.toString(),account);


        clearForm();
    }

    public void removeAccount(ActionEvent event){
        if (inputRemoveAccount.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please input correct information!");
            alert.showAndWait();
            return;
        }

        for (Transaction transaction:inputRemoveAccount.getValue().getTransactionList()) {
            if (transaction instanceof Transfer){
                transaction.removeTransaction();
            }
        }

        accountList.remove(inputRemoveAccount.getValue().toString());


        updateTable();
        clearForm();

    }

    public void getAccountStatistics(ActionEvent event){
        if (inputAccountStatistics.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please input correct information!");
            alert.showAndWait();
            return;
        }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Statistics");
        alert.setHeaderText("Account: " + inputAccountStatistics.getValue().toString() + " | Type: " + inputAccountStatistics.getValue().getType());
        alert.setContentText(inputAccountStatistics.getValue().getStatistics());
        alert.showAndWait();

        clearForm();

    }

}















