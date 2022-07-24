package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OverviewSelectionPane extends VBox {

	private TextArea profile, selectedModules, reservedModules;
	
	private Button btnSaveOverview;
	
	public OverviewSelectionPane() {
		this.setPadding(new Insets(50,50,50,50));
		
		profile = new TextArea("Profile will appear here");
		selectedModules = new TextArea("Selected modules will appear here");
		reservedModules = new TextArea("Reserved modules will appear here");
		profile.setEditable(false);
		selectedModules.setEditable(false);
		reservedModules.setEditable(false);
		
		profile.setPrefSize(2000, 150);
		selectedModules.setPrefWidth(2000);
		reservedModules.setPrefWidth(2000);
		
		HBox bottom = new HBox();
		bottom.getChildren().addAll(selectedModules, reservedModules);
		
		bottom.setSpacing(100);
		
		btnSaveOverview = new Button("Save Overview");
		HBox btn = new HBox();
		btn.getChildren().add(btnSaveOverview);
		btn.setAlignment(Pos.CENTER);
		
		VBox.setVgrow(bottom, Priority.ALWAYS);
		HBox.setHgrow(bottom, Priority.ALWAYS);
		
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		
		this.setSpacing(40);
		this.getChildren().addAll(profile, bottom, btn);
	}
	
	public String getProfile() {
		return profile.getText();
	}
	
	public void setProfile(String fullProfile) {
		profile.setText(fullProfile);
	}
	
	public String getSelectedModules() {
		return selectedModules.getText();
	}
	
	public void setSelectedModules(String selected) {
		selectedModules.setText(selected);
	}
	
	public String getReservedModule() {
		return reservedModules.getText();
	}
	
	public void setReservedModules(String reserved) {
		reservedModules.setText(reserved);
	}
	
	public Button getSaveOverview() {
		return btnSaveOverview;
	}
	
	public void addSaveOverview(EventHandler<ActionEvent> handler) {
		btnSaveOverview.setOnAction(handler);
	}
}
