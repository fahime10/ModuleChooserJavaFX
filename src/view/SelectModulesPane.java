package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Module;

public class SelectModulesPane extends HBox {

	private ListView<Module> unselectedTerm1, unselectedTerm2;
	private ObservableList<Module> firstTerm, secondTerm;
	
	private ListView<Module> selectedYearLong, selectedTerm1, selectedTerm2;
	private ObservableList<Module> yearLongModules, selectedFirstTerm, selectedSecondTerm;
	
	private Label lblUnselectedTerm1, lblUnselectedTerm2,
	lblTerm1, lblTerm2, lblCurrentTerm1, lblCurrentTerm2,
	lblYearLong, lblSelectedTerm1, lblSelectedTerm2;
	
	private TextField txtTerm1, txtTerm2;
	
	private int credits1, credits2;
	
	private Button btnAddTerm1, btnAddTerm2, btnRemoveTerm1,
	btnRemoveTerm2, btnReset, btnSubmit;
	
	public SelectModulesPane() {
		this.setPadding(new Insets(30,30,30,30));
		this.setSpacing(50);
		
		firstTerm = FXCollections.observableArrayList();
		secondTerm = FXCollections.observableArrayList();
		yearLongModules = FXCollections.observableArrayList();
		selectedFirstTerm = FXCollections.observableArrayList();
		selectedSecondTerm = FXCollections.observableArrayList();
		
		credits1 = 0;
		credits2 = 0;
		
		//-- whole left side -------------------
		VBox topLeft = new VBox();
		lblUnselectedTerm1 = new Label("Unselected Term 1 modules");
		unselectedTerm1 = new ListView<Module>(firstTerm);
		topLeft.getChildren().addAll(lblUnselectedTerm1, unselectedTerm1);
		
		HBox topLeft2 = new HBox();
		lblTerm1 = new Label("Term 1");
		btnAddTerm1 = new Button("Add");
		btnAddTerm1.setPrefWidth(70);
		btnRemoveTerm1 = new Button("Remove");
		btnRemoveTerm1.setPrefWidth(70);
		topLeft2.setSpacing(20);
		topLeft2.setAlignment(Pos.CENTER);
		topLeft2.getChildren().addAll(lblTerm1, btnAddTerm1, btnRemoveTerm1);
		
		VBox bottomLeft = new VBox();
		lblUnselectedTerm2 = new Label("Unselected Term 2 modules");
		unselectedTerm2 = new ListView<Module>(secondTerm);
		bottomLeft.getChildren().addAll(lblUnselectedTerm2, unselectedTerm2);
		
		HBox bottomLeft2 = new HBox();
		lblTerm2 = new Label("Term 2");
		btnAddTerm2 = new Button("Add");
		btnAddTerm2.setPrefWidth(70);
		btnRemoveTerm2 = new Button("Remove");
		btnRemoveTerm2.setPrefWidth(70);
		bottomLeft2.setSpacing(20);
		bottomLeft2.setAlignment(Pos.CENTER);
		bottomLeft2.getChildren().addAll(lblTerm2, btnAddTerm2, btnRemoveTerm2);
		
		HBox bottomLeft3 = new HBox();
		lblCurrentTerm1 = new Label("Current term 1 credits:");
		txtTerm1 = new TextField(String.valueOf(credits1));
		txtTerm1.setPrefWidth(60);
		txtTerm1.setPrefHeight(10);
		txtTerm1.setEditable(false);
		bottomLeft3.setSpacing(10);
		bottomLeft3.setAlignment(Pos.CENTER);
		bottomLeft3.getChildren().addAll(lblCurrentTerm1, txtTerm1);
		
		VBox leftSide = new VBox();
		btnReset = new Button("Reset");
		btnReset.setPrefWidth(70);
		HBox resetBtn = new HBox();
		resetBtn.getChildren().add(btnReset);
		resetBtn.setAlignment(Pos.CENTER_RIGHT);
		leftSide.setSpacing(20);
		leftSide.getChildren().addAll(topLeft, topLeft2, bottomLeft, bottomLeft2,  bottomLeft3, resetBtn);
		
		HBox.setHgrow(leftSide, Priority.ALWAYS);
		VBox.setVgrow(leftSide, Priority.ALWAYS);
		//---------------------------------------------------
		
		//--- whole right side ------------------------------
		VBox topRight = new VBox();
		lblYearLong = new Label("Selected Year Long modules");
		selectedYearLong = new ListView<Module>(yearLongModules);;
	    topRight.getChildren().addAll(lblYearLong, selectedYearLong);
		
	    VBox middleRight = new VBox();
		lblSelectedTerm1 = new Label("Selected term 1 modules");
		selectedTerm1 = new ListView<Module>(selectedFirstTerm);
		middleRight.getChildren().addAll(lblSelectedTerm1, selectedTerm1);
		
		VBox bottomRight = new VBox();
		lblSelectedTerm2 = new Label("Selected term 2 modules");
		selectedTerm2 = new ListView<Module>(selectedSecondTerm);
		bottomRight.getChildren().addAll(lblSelectedTerm2, selectedTerm2);
		
		HBox bottom = new HBox();
		lblCurrentTerm2 = new Label("Current term 2 credits:");
		txtTerm2 = new TextField(String.valueOf(credits2));
		txtTerm2.setPrefWidth(60);
		txtTerm2.setPrefHeight(10);
		txtTerm2.setEditable(false);
		bottom.setSpacing(10);
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().addAll(lblCurrentTerm2, txtTerm2);
		
		VBox rightSide = new VBox();
		btnSubmit = new Button("Submit");
		btnSubmit.setPrefWidth(70);
		rightSide.setSpacing(20);
		rightSide.getChildren().addAll(topRight, middleRight, bottomRight, bottom, btnSubmit);
		HBox.setHgrow(rightSide, Priority.ALWAYS);
		VBox.setVgrow(rightSide, Priority.ALWAYS);
		//------------------------------------------------
		
		this.getChildren().addAll(leftSide, rightSide);
	}
	
	public Button getResetBtn() {
		return btnReset;
	}
	
	public Button getSubmitBtn() {
		return btnSubmit;
	}
	
	public ObservableList<Module> getYearLongModules() {
		return yearLongModules;
	}
	
	public ObservableList<Module> getUnselectedTerm1() {
		return firstTerm;
	}
	
	public ObservableList<Module> getUnselectedTerm2() {
		return secondTerm;
	}
	
	public ObservableList<Module> getSelectedTerm1() {
		return selectedFirstTerm;
	}
	
	public ObservableList<Module> getSelectedTerm2() {
		return selectedSecondTerm;
	}
	
	public ListView<Module> getYearLong() {
		return selectedYearLong;
	}
	
	public ListView<Module> getUnselectedModules1() {
		return unselectedTerm1;
	}
	
	public ListView<Module> getUnselectedModules2() {
		return unselectedTerm2;
	}
	
	public ListView<Module> getSelectedModules1() {
		return selectedTerm1;
	}
	
	public ListView<Module> getSelectedModules2() {
		return selectedTerm2;
	}
	
	public Module getSelectedModuleTerm1() {
		return unselectedTerm1.getSelectionModel().getSelectedItem();
	}
	
	public Module getSelectedModulesTerm2() {
		return unselectedTerm2.getSelectionModel().getSelectedItem();
	}
	
	public Module getChosenModuleTerm1() {
		return selectedTerm1.getSelectionModel().getSelectedItem();
	}
	
	public Module getChosenModuleTerm2() {
		return selectedTerm2.getSelectionModel().getSelectedItem();
	}
	
	public String getTxtFirstTerm() {
		return txtTerm1.getText();
	}
	
	public void setTxtFirstTerm(String string) {
		txtTerm1.setText(string);
	}
	
	public void addMandatoryModulesTerm1(ArrayList<Module> compModulesMandatory) {
		for(Module module: compModulesMandatory) {
			selectedFirstTerm.add(module);
		}
	}
	
	public void addMandatoryModulesTerm2(ArrayList<Module> compModulesMandatory) {
		for(Module module: compModulesMandatory) {
			selectedSecondTerm.add(module);
		}
	}
	
	public void addCompFirstModules(ArrayList<Module> compModules) {
		for(Module module: compModules) {
			firstTerm.add(module);
		}
	}
	
	public void addCompSecondModules(ArrayList<Module> compModules) {
		for(Module module: compModules) {
			secondTerm.add(module);
		}
	}
	
	public void addMandatorySoftModulesTerm1(ArrayList<Module> softModulesMandatory) {
		for(Module module: softModulesMandatory) {
			selectedFirstTerm.add(module);
		}
	}
	
	public void addMandatorySoftModulesTerm2(ArrayList<Module> softModulesMandatory) {
		for(Module module: softModulesMandatory) {
			selectedSecondTerm.add(module);
		}
	}
	
	public void addSoftFirstModules(ArrayList<Module> softModules) {
		for(Module module: softModules) {
		firstTerm.add(module);
		}
	}
	
	public void addSoftSecondModules(ArrayList<Module> softModules) {
		for(Module module: softModules) {
		secondTerm.add(module);
		}
	}
	
	public void addYearLongCompModules(ArrayList<Module> modules) {
		for(Module module: modules) {
		yearLongModules.add(module);
		}
	}

	public void addYearLongSoftModules(ArrayList<Module> modules) {
		for(Module module: modules) {
			yearLongModules.add(module);
			}
		}
	
	public void addAddTerm1Handler(EventHandler<ActionEvent> handler) {
		btnAddTerm1.setOnAction(handler);
	}
	
	public void addRemoveTerm1Handler(EventHandler<ActionEvent> handler) {
		btnRemoveTerm1.setOnAction(handler);
	}
	
	public void addAddTerm2Handler(EventHandler<ActionEvent> handler) {
		btnAddTerm2.setOnAction(handler);
	}
	
	public void addRemoveTerm2Handler(EventHandler<ActionEvent> handler) {
		btnRemoveTerm2.setOnAction(handler);
	}
	
	public void addResetHandler(EventHandler<ActionEvent> handler) {
		btnReset.setOnAction(handler);
	}
	
	public void addSubmitHandler(EventHandler<ActionEvent> handler) {
		btnSubmit.setOnAction(handler);
	}
	
	public void updateTxtField1(int amount) {
		txtTerm1.setText(String.valueOf(amount));
	}
	
	public void increment1() {
		credits1 += 15;
	}
	
	public void decrement1() {
		credits1 -= 15;
	}
	
	public int getCredits1() {
		return credits1;
	}
	
	public void incrementByTerm1(int amount) {
		credits1 += amount;
	}
	
	public void updateTxtField2(int amount) {
		txtTerm2.setText(String.valueOf(amount));
	}
	
	public void increment2() {
		credits2 += 15;
	}
	
	public void decrement2() {
		credits2 -= 15;
	}
	
	public int getCredits2() {
		return credits2;
	}
	
	public void incrementByTerm2(int amount) {
		credits2 += amount;
	}
	
	public void setCredits1(int amount) {
		credits1 = amount;
	}
	
	public void setCredits2(int amount) {
		credits2 = amount;
	}
}
