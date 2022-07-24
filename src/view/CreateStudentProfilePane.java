package view;

import java.time.LocalDate;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Course;
import model.Name;

public class CreateStudentProfilePane extends GridPane {

	private ComboBox<Course> cboCourses;
	private DatePicker inputDate;
	private TextField txtFirstName, txtSurname,  txtPnumber, txtEmail;
	private Button btnCreateProfile;
	private Label lblErrorMessage;
	
	public CreateStudentProfilePane() {
		//styling
		this.setVgap(15);
		this.setHgap(20);
		this.setAlignment(Pos.CENTER);

		ColumnConstraints column0 = new ColumnConstraints();
		column0.setHalignment(HPos.RIGHT);

		this.getColumnConstraints().addAll(column0);
		
		//create labels
		lblErrorMessage = new Label("");
		lblErrorMessage.setTextFill(Color.FIREBRICK);
		Label lblTitle = new Label("Select course: ");
		Label lblPnumber = new Label("Input P number: ");
		Label lblFirstName = new Label("Input first name: ");
		Label lblSurname = new Label("Input surname: ");
		Label lblEmail = new Label("Input email: ");
		Label lblDate = new Label("Input date: ");
		
		//initialise combobox
		cboCourses = new ComboBox<Course>(); //this is populated via method towards end of class
		
		//setup text fields
		txtFirstName = new TextField();
		txtSurname = new TextField();
		txtPnumber = new TextField();
		txtEmail = new TextField();
		
		inputDate = new DatePicker();
		
		//initialise create profile button
		btnCreateProfile = new Button("Create Profile");

		//add controls and labels to container
		this.add(lblErrorMessage, 0, 0, 2, 1);
		
		this.add(lblTitle, 0, 1);
		this.add(cboCourses, 1, 1);

		this.add(lblPnumber, 0, 2);
		this.add(txtPnumber, 1, 2);
		
		this.add(lblFirstName, 0, 3);
		this.add(txtFirstName, 1, 3);

		this.add(lblSurname, 0, 4);
		this.add(txtSurname, 1, 4);
		
		this.add(lblEmail, 0, 5);
		this.add(txtEmail, 1, 5);
		
		this.add(lblDate, 0, 6);
		this.add(inputDate, 1, 6);
			
		this.add(new HBox(), 0, 7);
		this.add(btnCreateProfile, 1, 7);
	}
	
	//method to allow the controller to add courses to the combobox
	public void addCoursesToComboBox(Course[] courses) {
		cboCourses.getItems().addAll(courses);
		cboCourses.getSelectionModel().select(0); //select first course by default
	}
	
	//methods to retrieve the form selection/input
	public Course getSelectedCourse() {
		return cboCourses.getSelectionModel().getSelectedItem();
	}
	
	public void setSelectedCourse(Course course) {
		cboCourses.getSelectionModel().select(course);
	}
	
	public String getStudentPnumber() {
		return txtPnumber.getText();
	}
	
	public TextField getStudentPNumber() {
		return txtPnumber;
	}
	
	public Name getStudentName() {
		return new Name(txtFirstName.getText(), txtSurname.getText());
	}
	
	public TextField getStudentFirstName() {
		return txtFirstName;
	}
	
	public TextField getStudentSurname() {
		return txtSurname;
	}

	public String getStudentEmail() {
		return txtEmail.getText();
	}
	
	public TextField getEmail() {
		return txtEmail;
	}
	
	public LocalDate getStudentDate() {
		return inputDate.getValue();
	}
	
	public DatePicker getDate() {
		return inputDate;
	}
	
	public void setDate(LocalDate date) {
		inputDate.setValue(date);
	}
	
	//method to retrieve the error message label
	public Label getErrorMessage() {
		return lblErrorMessage;
	}
	
	//method to attach the create student profile button event handler
	public void addCreateStudentProfileHandler(EventHandler<ActionEvent> handler) {
		btnCreateProfile.setOnAction(handler);
	}
	
	public void addCreateBtnDisableBind(BooleanBinding empty) {
		btnCreateProfile.disableProperty().bind(empty);
	}

	public BooleanBinding emptyFields() {
		return txtFirstName.textProperty().isEmpty().or(txtSurname.textProperty().isEmpty().or(
				txtPnumber.textProperty().isEmpty().or(txtEmail.textProperty().isEmpty()
						.or(inputDate.valueProperty().isNull()))));
	}

}
