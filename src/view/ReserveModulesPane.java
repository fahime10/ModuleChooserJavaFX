package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Module;

public class ReserveModulesPane extends VBox {

	private Label lblUnselectedTerm1, lblUnselectedTerm2,
	lblReserveTerm1, lblReserveTerm2, lblReserveMessage1p1,
	lblReserveMessage1p2, lblReserveMessage2p1, lblReserveMessage2p2, 
	lblCredits1, lblCredits2;
	
	private ListView<Module> unselectedTerm1, reservedTerm1,
	unselectedTerm2, reservedTerm2;
	
	private ObservableList<Module> unselectedModulesTerm1, unselectedModulesTerm2,
	reservedFirstTerm, reservedSecondTerm;
	
	private Button btnAddTerm1, btnAddTerm2, btnRemoveTerm1,
	btnRemoveTerm2, btnConfirmTerm1, btnConfirmTerm2;
	
	private int credits1, credits2;
	
	private TitledPane term1, term2;
	
	private Accordion terms;
	
	public ReserveModulesPane() {
		this.setPadding(new Insets(30,30,30,30));
		
		unselectedModulesTerm1 = FXCollections.observableArrayList();
		unselectedModulesTerm2 = FXCollections.observableArrayList();
		reservedFirstTerm = FXCollections.observableArrayList();
		reservedSecondTerm = FXCollections.observableArrayList();
		
		credits1 = 0;
		credits2 = 0;
		
		terms = new Accordion();
		
		//--- Term 1 ----------------
		VBox leftTerm1 = new VBox();
		lblUnselectedTerm1 = new Label("Unselected Term 1 modules");
		unselectedTerm1 = new ListView<Module>(unselectedModulesTerm1);
		unselectedTerm1.prefWidthProperty().bind(this.widthProperty());
		leftTerm1.getChildren().addAll(lblUnselectedTerm1, unselectedTerm1);
		
		VBox rightTerm1 = new VBox();
		lblReserveTerm1 = new Label("Reserved Term 1 modules");
		reservedTerm1 = new ListView<Module>(reservedFirstTerm);
		reservedTerm1.prefWidthProperty().bind(this.widthProperty());
		rightTerm1.getChildren().addAll(lblReserveTerm1, reservedTerm1);
		
		HBox bottom = new HBox();
		bottom.setAlignment(Pos.CENTER);
		lblReserveMessage1p1 = new Label("Reserve ");
		lblCredits1 = new Label(String.valueOf(credits1));
		lblReserveMessage1p2 = new Label(" credits worth of term 1 modules");
		btnAddTerm1 = new Button("Add");
		btnAddTerm1.setPrefWidth(70);
		btnRemoveTerm1 = new Button("Remove");
		btnRemoveTerm1.setPrefWidth(70);
		btnConfirmTerm1 = new Button("Confirm");
		btnConfirmTerm1.setPrefWidth(70);
		bottom.setSpacing(20);
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().addAll(lblReserveMessage1p1, lblCredits1, lblReserveMessage1p2,
				btnAddTerm1, btnRemoveTerm1, btnConfirmTerm1);
		
		HBox row = new HBox();
		row.setSpacing(20);
		row.getChildren().addAll(leftTerm1, rightTerm1);
		
		VBox wholeTerm1 = new VBox();
		wholeTerm1.setSpacing(20);
		wholeTerm1.getChildren().addAll(row, bottom);
		HBox.setHgrow(wholeTerm1, Priority.ALWAYS);
		VBox.setVgrow(wholeTerm1, Priority.ALWAYS);
		
		term1 = new TitledPane("Term 1", wholeTerm1);
		terms.getPanes().add(term1);
		//-------------------------------------------
		
		//---- Term 2 ---------------------------------
		VBox leftTerm2 = new VBox();
		lblUnselectedTerm2 = new Label("Unselected Term 2 modules");
		unselectedTerm2 = new ListView<Module>(unselectedModulesTerm2);
		unselectedTerm2.prefWidthProperty().bind(this.widthProperty());
		leftTerm2.getChildren().addAll(lblUnselectedTerm2, unselectedTerm2);
		
		VBox rightTerm2 = new VBox();
		lblReserveTerm2 = new Label("Reserved Term 2 modules");
		reservedTerm2 = new ListView<Module>(reservedSecondTerm);
		reservedTerm2.prefWidthProperty().bind(this.widthProperty());
		rightTerm2.getChildren().addAll(lblReserveTerm2, reservedTerm2);
		
		HBox bottom2 = new HBox();
		bottom2.setAlignment(Pos.CENTER);
		lblReserveMessage2p1 = new Label("Reserve ");
		lblCredits2 = new Label(String.valueOf(45));
		lblReserveMessage2p2 = new Label(" credits worth of term 2 modules");
		btnAddTerm2 = new Button("Add");
		btnAddTerm2.setPrefWidth(70);
		btnRemoveTerm2 = new Button("Remove");
		btnRemoveTerm2.setPrefWidth(70);
		btnConfirmTerm2 = new Button("Confirm");
		btnConfirmTerm2.setPrefWidth(70);
		bottom2.setSpacing(20);
		bottom2.setAlignment(Pos.CENTER);
		bottom2.getChildren().addAll(lblReserveMessage2p1, lblCredits2, lblReserveMessage2p2, 
				btnAddTerm2, btnRemoveTerm2, btnConfirmTerm2);
		
		HBox row2 = new HBox();
		row2.setSpacing(20);
		row2.getChildren().addAll(leftTerm2, rightTerm2);
		
		VBox wholeTerm2 = new VBox();
		wholeTerm2.setSpacing(20);
		wholeTerm2.getChildren().addAll(row2, bottom2);
		HBox.setHgrow(terms, Priority.ALWAYS);
		VBox.setVgrow(terms, Priority.ALWAYS);
		
		term2 = new TitledPane("Term 2", wholeTerm2);
		HBox.setHgrow(term1, Priority.ALWAYS);
		VBox.setVgrow(term1, Priority.ALWAYS);
		HBox.setHgrow(term2, Priority.ALWAYS);
		VBox.setVgrow(term2, Priority.ALWAYS);
		
		term1.setDisable(true);
		term2.setDisable(true);
		
		terms.setPrefSize(850, 400);
		HBox.setHgrow(terms, Priority.ALWAYS);
		VBox.setVgrow(terms, Priority.ALWAYS);
		terms.getPanes().add(term2);
		//---------------------------------------------
		this.getChildren().addAll(terms);
	}
	
	public Accordion getAccordion() {
		return terms;
	}
	
	public TitledPane getTerm1() {
		return term1;
	}
	
	public TitledPane getTerm2() {
		return term2;
	}
	
	public ObservableList<Module> getNotSelectedModulesTerm1() {
		return unselectedModulesTerm1;
	}
	
	public ObservableList<Module> getNotSelectedModulesTerm2() {
		return unselectedModulesTerm2;
	}
	
	public ObservableList<Module> getReservedModulesTerm1() {
		return reservedFirstTerm;
	}
	
	public ObservableList<Module> getReservedModulesTerm2() {
		return reservedSecondTerm;
	}
	
	public ListView<Module> getNotSelectedTerm1() {
		return unselectedTerm1;
	}
	
	public ListView<Module> getNotSelectedTerm2() {
		return unselectedTerm2;
	}
	
	public ListView<Module> getReservedTerm1() {
		return reservedTerm1;
	}
	
	public ListView<Module> getReservedTerm2() {
		return reservedTerm2;
	}

	public void extractUnselectedModulesTerm1(ArrayList<Module> modulesInTerm1) {
		unselectedModulesTerm1.addAll(modulesInTerm1);
	}

	public void extractUnselectedModulesTerm2(ArrayList<Module> modulesInTerm2) {
		unselectedModulesTerm2.addAll(modulesInTerm2);
	}
	
	public void addAddReserveModuleTerm1(EventHandler<ActionEvent> handler) {
		btnAddTerm1.setOnAction(handler);
	}
	
	public void addRemoveReserveModuleTerm1(EventHandler<ActionEvent> handler) {
		btnRemoveTerm1.setOnAction(handler);
	}
	
	public void addAddReserveModuleTerm2(EventHandler<ActionEvent> handler) {
		btnAddTerm2.setOnAction(handler);
	}
	
	public void addRemoveReserveModuleTerm2(EventHandler<ActionEvent> handler) {
		btnRemoveTerm2.setOnAction(handler);
	}
	
	public void addConfirmReserveModulesTerm1(EventHandler<ActionEvent> handler) {
		btnConfirmTerm1.setOnAction(handler);
	}
	
	public void addConfirmeReserveModulesTerm2(EventHandler<ActionEvent> handler) {
		btnConfirmTerm2.setOnAction(handler);
	}
	
	public Module getUnchosenReservedModuleTerm1() {
		return unselectedTerm1.getSelectionModel().getSelectedItem();
	}
	
	public Module getUnchosenReservedModuleTerm2() {
		return unselectedTerm2.getSelectionModel().getSelectedItem();
	}
	
	public Module getChosenReservedModuleTerm1() {
		return reservedTerm1.getSelectionModel().getSelectedItem();
	}
	
	public Module getChosenReservedModuleTerm2() {
		return reservedTerm2.getSelectionModel().getSelectedItem();
	}
	
	public Label getLblCredits1() {
		return lblCredits1;
	}
	
	public Label getLblCredits2() {
		return lblCredits2;
	}
	
	public int getCredits1() {
		return credits1;
	}
	
	public int getCredits2() {
		return credits2;
	}
	
	public void setCredits1(int credits) {
		credits1 = credits;
	}
	
	public void setCredits2(int credits) {
		credits2 = credits;
	}
	
	public void increment1() {
		credits1 += 15;
	}
	
	public void increment2() {
		credits2 += 15;
	}
	
	public void decrement1() {
		credits1 -= 15;
	}
	
	public void decrement2() {
		credits2 -= 15;
	}
}
