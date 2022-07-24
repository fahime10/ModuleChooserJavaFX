package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Course;
import model.Schedule;
import model.Module;
import model.Name;
import model.StudentProfile;
import view.ModuleChooserRootPane;
import view.OverviewSelectionPane;
import view.ReserveModulesPane;
import view.SelectModulesPane;
import view.CreateStudentProfilePane;
import view.ModuleChooserMenuBar;

public class ModuleChooserController {

	//fields to be used throughout class
	private ModuleChooserRootPane view;
	private StudentProfile model;
	
	private CreateStudentProfilePane cspp;
	private OverviewSelectionPane osp;
	private SelectModulesPane smp;
	private ReserveModulesPane rmp;
	private ModuleChooserMenuBar mstmb;

	public ModuleChooserController(ModuleChooserRootPane view, StudentProfile model) {
		//initialise view and model fields
		this.view = view;
		this.model = model;
		
		//initialise view subcontainer fields
		cspp = view.getCreateStudentProfilePane();
		osp = view.getOverviewResultsPane();
		smp = view.getSelectModulesPane();
		rmp = view.getReserveModulesPane();
		mstmb = view.getModuleSelectionToolMenuBar(); 

		//add courses to combobox in create student profile pane using the generateAndGetCourses helper method below
		cspp.addCoursesToComboBox(generateAndGetCourses());
		
		
		//attach event handlers to view using private helper method
		this.attachEventHandlers();
		this.attachBindings();
	}

	
	//helper method - used to attach event handlers
	private void attachEventHandlers() {
		//attach an event handler to the create student profile pane
		cspp.addCreateStudentProfileHandler(new CreateStudentProfileHandler());
		
		smp.addAddTerm1Handler(new AddTerm1Handler());
		smp.addRemoveTerm1Handler(new RemoveTerm1Handler());
		smp.addAddTerm2Handler(new AddTerm2Handler());
		smp.addRemoveTerm2Handler(new RemoveTerm2Handler());
		
		smp.addResetHandler(new ResetHandler());
		smp.addSubmitHandler(new SubmitHandler());
		
		rmp.addAddReserveModuleTerm1(new AddReserveModuleTerm1Handler());
		rmp.addRemoveReserveModuleTerm1(new RemoveReserveModuleTerm1Handler());
		rmp.addConfirmReserveModulesTerm1(new ConfirmTerm1Handler());
		rmp.addAddReserveModuleTerm2(new AddReserveModuleTerm2Handler());
		rmp.addRemoveReserveModuleTerm2(new RemoveReserveModuleTerm2Handler());
		rmp.addConfirmeReserveModulesTerm2(new ConfirmTerm2Handler());
		
		osp.addSaveOverview(new SaveOverviewHandler());
		
		mstmb.addLoadHandler(new LoadMenuHandler());
		mstmb.addSaveHandler(new SaveMenuHandler());
		mstmb.addAboutHandler(new AboutMenuHandler());
		
		//attach an event handler to the menu bar that closes the application
		mstmb.addExitHandler(e -> System.exit(0));
	}
	
	private void attachBindings() {
		cspp.addCreateBtnDisableBind(cspp.emptyFields());
		smp.getResetBtn().setDisable(true);
		smp.getSubmitBtn().setDisable(true);
		osp.getSaveOverview().setDisable(true);
	}
	
	//event handler (currently empty), which can be used for creating a profile
	private class CreateStudentProfileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			String name = cspp.getStudentFirstName().getText() + cspp.getStudentSurname().getText();
			boolean check = true;
			for(int i = 0; i < name.length(); i++) {
				if(!((name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z'))) {
					check = false;
				}
			}
			if(check && cspp.getStudentEmail().contains("@") && (cspp.getStudentPnumber().startsWith("P") || cspp.getStudentPnumber().startsWith("p"))) {
				
			model.setStudentCourse(cspp.getSelectedCourse());
			String fullName = cspp.getStudentName().getFirstName() + " " + cspp.getStudentName().getFamilyName();
			String[] names = fullName.split(" ");
			model.setStudentName(new Name(names[0].substring(0,1).toUpperCase() + names[0].substring(1), 
					names[1].substring(0,1).toUpperCase() + names[1].substring(1)));
			model.setStudentEmail(cspp.getStudentEmail());
			model.setStudentPnumber(cspp.getStudentPnumber());
			model.setSubmissionDate(cspp.getStudentDate());
			
			osp.setProfile("Student name: " + model.getStudentName().getFullName() + "\nStudent P Number: " + model.getStudentPnumber() +
						   "\nStudent Email: " + model.getStudentEmail() + "\nDate submitted: " + model.getSubmissionDate() + 
						   "\nCourse: " + model.getStudentCourse());
			
			if(cspp.getSelectedCourse().getCourseName().equals("Computer Science")) {
				smp.addCompFirstModules(compFirstTermModules());
				smp.addCompSecondModules(compSecondTermModules());
				smp.addYearLongCompModules(yearLongComp());
				smp.addMandatoryModulesTerm1(compModulesMandatoryTerm1());
				smp.addMandatoryModulesTerm2(compModulesMandatoryTerm2());
			} else if(cspp.getSelectedCourse().getCourseName().equals("Software Engineering")) {
				smp.addSoftFirstModules(softFirstTermModules());
				smp.addSoftSecondModules(softSecondTermModules());
				smp.addYearLongSoftModules(yearLongSoft());
				smp.addMandatorySoftModulesTerm1(softModulesMandatoryTerm1());
				smp.addMandatorySoftModulesTerm2(softModulesMandatoryTerm2());
			}
			smp.getResetBtn().setDisable(false);
			smp.getSubmitBtn().setDisable(false);
			view.getCreateStudentProfilePane().setVisible(false);
			view.changeTab(1);
			
		} else {
			String error = "Please ensure you have the correct input!\n";
			
			if(!cspp.getStudentPnumber().startsWith("P") || !cspp.getStudentPnumber().startsWith("p")) {
				error += "Student P number must start with P or p, followed by the number.\n";
				} 
			
			if(!cspp.getStudentEmail().contains("@")) {
				error += "Student email must have the @ character.\n";
			}
			
			cspp.getErrorMessage().setText(error);
		}
		}
	}

	//helper method - generates course and module data and returns courses within an array
	private Course[] generateAndGetCourses() {
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, Schedule.TERM_1);
		Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, Schedule.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, Schedule.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, Schedule.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, Schedule.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, Schedule.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, Schedule.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, Schedule.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, Schedule.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, Schedule.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, Schedule.TERM_1);
		Module ctec3911 = new Module("CTEC3911", "Mobile Application Development", 15, false, Schedule.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, Schedule.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, Schedule.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, Schedule.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, Schedule.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, Schedule.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, Schedule.TERM_2);


		Course compSci = new Course("Computer Science");
		compSci.addModuleToCourse(imat3423);
		compSci.addModuleToCourse(ctec3451);
		compSci.addModuleToCourse(ctec3902_CompSci);
		compSci.addModuleToCourse(ctec3110);
		compSci.addModuleToCourse(ctec3605);
		compSci.addModuleToCourse(ctec3606);
		compSci.addModuleToCourse(ctec3410);
		compSci.addModuleToCourse(ctec3904);
		compSci.addModuleToCourse(ctec3905);
		compSci.addModuleToCourse(ctec3906);
		compSci.addModuleToCourse(ctec3911);
		compSci.addModuleToCourse(imat3410);
		compSci.addModuleToCourse(imat3406);
		compSci.addModuleToCourse(imat3611);
		compSci.addModuleToCourse(imat3613);
		compSci.addModuleToCourse(imat3614);
		compSci.addModuleToCourse(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModuleToCourse(imat3423);
		softEng.addModuleToCourse(ctec3451);
		softEng.addModuleToCourse(ctec3902_SoftEng);
		softEng.addModuleToCourse(ctec3110);
		softEng.addModuleToCourse(ctec3605);
		softEng.addModuleToCourse(ctec3606);
		softEng.addModuleToCourse(ctec3410);
		softEng.addModuleToCourse(ctec3904);
		softEng.addModuleToCourse(ctec3905);
		softEng.addModuleToCourse(ctec3906);
		softEng.addModuleToCourse(ctec3911);
		softEng.addModuleToCourse(imat3410);
		softEng.addModuleToCourse(imat3406);
		softEng.addModuleToCourse(imat3611);
		softEng.addModuleToCourse(imat3613);
		softEng.addModuleToCourse(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;

		return courses;
	}
	
	public void alertDialogShow(AlertType type, String title, String header, String text) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	//Methods for select modules
	public ArrayList<Module> compModulesMandatoryTerm1() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("computerScienceModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_1) && module.isMandatory()) {
				modules.add(module);
				smp.incrementByTerm1(module.getModuleCredits());
				smp.updateTxtField1(smp.getCredits1());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> compModulesMandatoryTerm2() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("computerScienceModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_2) && module.isMandatory()) {
				modules.add(module);
				smp.incrementByTerm2(module.getModuleCredits());
				smp.updateTxtField2(smp.getCredits2());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> compFirstTermModules() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("computerScienceModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_1) && !module.isMandatory()) {
				modules.add(module);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> compSecondTermModules() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("computerScienceModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_2) && !module.isMandatory()) {
				modules.add(module);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> softModulesMandatoryTerm1() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("softwareEngineeringModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_1) && module.isMandatory()) {
				modules.add(module);
				smp.incrementByTerm1(module.getModuleCredits());
				smp.updateTxtField1(smp.getCredits1());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> softModulesMandatoryTerm2() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("softwareEngineeringModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_2) && module.isMandatory()) {
				modules.add(module);
				smp.incrementByTerm2(module.getModuleCredits());
				smp.updateTxtField2(smp.getCredits2());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> softFirstTermModules() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("softwareEngineeringModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_1) && !module.isMandatory()) {
				modules.add(module);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> softSecondTermModules() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("softwareEngineeringModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.TERM_2) && !module.isMandatory()) {
				modules.add(module);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> yearLongComp() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("computerScienceModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.YEAR_LONG) && module.isMandatory()) {
				modules.add(module);
				smp.incrementByTerm1(module.getModuleCredits() / 2);
				smp.incrementByTerm2(module.getModuleCredits() / 2);
				smp.updateTxtField1(smp.getCredits1());
				smp.updateTxtField2(smp.getCredits2());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	public ArrayList<Module> yearLongSoft() {
		Scanner input;
		ArrayList<Module> modules = new ArrayList<Module>();
		String arr[];
		String line;
		try {
			input = new Scanner(new File("softwareEngineeringModules.txt"));
			while(input.hasNextLine()) {
				line = input.nextLine();
				arr = line.split(", ");
				Schedule schedule = Schedule.valueOf(arr[4].substring(9));
				Module module = new Module(arr[0], arr[1], Integer.parseInt(arr[2]), Boolean.valueOf(arr[3]), schedule);
				if(module.getDelivery().equals(Schedule.YEAR_LONG) && module.isMandatory()) {
				modules.add(module);
				smp.incrementByTerm1(module.getModuleCredits() / 2);
				smp.incrementByTerm2(module.getModuleCredits() / 2);
				smp.updateTxtField1(smp.getCredits1());
				smp.updateTxtField2(smp.getCredits2());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		return modules;
}
	
	private class AddTerm1Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(smp.getCredits1() < 60) {
			Module choice = smp.getSelectedModuleTerm1();
			if(choice != null) {
			smp.getUnselectedTerm1().remove(choice);
			smp.getSelectedTerm1().add(choice);
			smp.increment1();
			smp.updateTxtField1(smp.getCredits1());
			}
		}
	}
}
	
	private class RemoveTerm1Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(smp.getCredits1() > 0) {
				Module choice = smp.getChosenModuleTerm1();
				if(choice != null) {
				if(!choice.isMandatory()) {
				smp.getUnselectedTerm1().add(choice);
				smp.getSelectedTerm1().remove(choice);
				smp.decrement1();
				smp.updateTxtField1(smp.getCredits1());
			}
				}
			}
		}
	}
	
	private class AddTerm2Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(smp.getCredits2() < 60) {
				Module choice = smp.getSelectedModulesTerm2();
				if(choice != null) {
				smp.getUnselectedTerm2().remove(choice);
				smp.getSelectedTerm2().add(choice);
				smp.increment2();
				smp.updateTxtField2(smp.getCredits2());
			}
			}
		}
	}
	
	private class RemoveTerm2Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(smp.getCredits2() > 0) {
				Module choice = smp.getChosenModuleTerm2();
				if(choice != null) {
				if(!choice.isMandatory()) {
					smp.getUnselectedTerm2().add(choice);
					smp.getSelectedTerm2().remove(choice);
					smp.decrement2();
					smp.updateTxtField2(smp.getCredits2());
				}
			}
		}
	}
	}
	
	private class ResetHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {	
			smp.getYearLongModules().clear();
			smp.getUnselectedTerm1().clear();
			smp.getUnselectedTerm2().clear();
			smp.getSelectedTerm1().clear();
			smp.getSelectedTerm2().clear();
			
			smp.getYearLong().getSelectionModel().clearSelection();
			smp.getUnselectedModules1().getSelectionModel().clearSelection();
			smp.getUnselectedModules2().getSelectionModel().clearSelection();
			smp.getSelectedModules1().getSelectionModel().clearSelection();
			smp.getSelectedModules2().getSelectionModel().clearSelection();
			
			smp.updateTxtField1(0);
			smp.updateTxtField2(0);
			smp.setCredits1(0);
			smp.setCredits2(0);
			
			if(model.getStudentCourse().getCourseName().equals("Computer Science")) {
				smp.addCompFirstModules(compFirstTermModules());
				smp.addCompSecondModules(compSecondTermModules());
				smp.addYearLongCompModules(yearLongComp());
				smp.addMandatoryModulesTerm1(compModulesMandatoryTerm1());
				smp.addMandatoryModulesTerm2(compModulesMandatoryTerm2());
			} else if(model.getStudentCourse().getCourseName().equals("Software Engineering")) {
				smp.addSoftFirstModules(softFirstTermModules());
				smp.addSoftSecondModules(softSecondTermModules());
				smp.addYearLongSoftModules(yearLongSoft());
				smp.addMandatorySoftModulesTerm1(softModulesMandatoryTerm1());
				smp.addMandatorySoftModulesTerm2(softModulesMandatoryTerm2());
			}
		}
	}
	
	private class SubmitHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(smp.getCredits1() == 60 && smp.getCredits2() == 60) {
				String selectedModules = "";
				
				for(Module modules: smp.getSelectedTerm1()) {
					selectedModules += modules.formattedString() + "\n\n";
					model.addSelectedModule(modules);
				}
				selectedModules += "\n";
				
				for(Module modules: smp.getSelectedTerm2()) {
					selectedModules += modules.formattedString() + "\n\n";
					model.addSelectedModule(modules);
				}
				
			osp.setSelectedModules("Selected Modules:\n========\n" + selectedModules);
			
			rmp.extractUnselectedModulesTerm1(getModulesInTerm1());
			rmp.extractUnselectedModulesTerm2(getModulesInTerm2());
			
			rmp.getTerm1().setDisable(false);
			
			if(model.getStudentCourse().getCourseName().equals("Computer Science")) {
			rmp.getLblCredits1().setText(String.valueOf(30));
			rmp.getLblCredits2().setText(String.valueOf(45));
			} else if(model.getStudentCourse().getCourseName().equals("Software Engineering")) {
				rmp.getLblCredits1().setText(String.valueOf(30));
				rmp.getLblCredits2().setText(String.valueOf(30));
			}
			view.getSelectModulesPane().setVisible(false);
			view.changeTab(2);
		} else {
			alertDialogShow(AlertType.INFORMATION, "Not enough credits!", null, "Please select up to 60 credits per term.");
		}
		}
	}
	//-------------------------------------------------------------
	//Methods for the reserved modules
	public ArrayList<Module> getModulesInTerm1() {
		ArrayList<Module> modules = new ArrayList<Module>();
		for(Module module: smp.getUnselectedTerm1()) {
			modules.add(module);
		}
		return modules;
	}
	
	public ArrayList<Module> getModulesInTerm2() {
		ArrayList<Module> modules = new ArrayList<Module>();
		for(Module module: smp.getUnselectedTerm2()) {
			modules.add(module);
		}
		return modules;
	}
	
	private class AddReserveModuleTerm1Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(rmp.getCredits1() < 30) {
				Module choice = rmp.getUnchosenReservedModuleTerm1();
				if(choice != null) {
				rmp.getNotSelectedModulesTerm1().remove(choice);
				rmp.getReservedModulesTerm1().add(choice);
				rmp.increment1();
				}
			}
		}
	}
	
	private class RemoveReserveModuleTerm1Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(rmp.getCredits1() > 0) {
				Module choice = rmp.getChosenReservedModuleTerm1();
				if(choice != null) {
				rmp.getNotSelectedModulesTerm1().add(choice);
				rmp.getReservedModulesTerm1().remove(choice);
				rmp.decrement1();
				}
			}
		}
	}
	
	private class ConfirmTerm1Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(rmp.getCredits1() == 30) {
				rmp.getTerm1().setDisable(true);
				rmp.getTerm2().setDisable(false);
				view.changePane();
			}
		}
	}
	
	private class AddReserveModuleTerm2Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(rmp.getCredits2() < 45 && model.getStudentCourse().getCourseName().equals("Computer Science")) {
				Module choice = rmp.getUnchosenReservedModuleTerm2();
				if(choice != null) {
				rmp.getNotSelectedModulesTerm2().remove(choice);
				rmp.getReservedModulesTerm2().add(choice);
				rmp.increment2();
				}
			} else if(rmp.getCredits2() < 30 && model.getStudentCourse().getCourseName().equals("Software Engineering")) {
				Module choice = rmp.getUnchosenReservedModuleTerm2();
				if(choice != null) {
				rmp.getNotSelectedModulesTerm2().remove(choice);
				rmp.getReservedModulesTerm2().add(choice);
				rmp.increment2();
				}
			}
		}
	}
	
	private class RemoveReserveModuleTerm2Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(rmp.getCredits2() > 0) {
				Module choice = rmp.getChosenReservedModuleTerm2();
				if(choice != null) {
				rmp.getNotSelectedModulesTerm2().add(choice);
				rmp.getReservedModulesTerm2().remove(choice);
				rmp.decrement2();
				}
			}
		}
	}
	
	private class ConfirmTerm2Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if(rmp.getCredits2() == 45 && model.getStudentCourse().getCourseName().equals("Computer Science")) {
				rmp.getTerm2().setDisable(true);
				
				String reserved = "";
				for(Module modules: rmp.getReservedModulesTerm1()) {
					reserved += modules.formattedString() + "\n\n";
					model.addReservedModule(modules);
				}
				
				reserved += "\n";
				
				for(Module modules: rmp.getReservedModulesTerm2()) {
					reserved += modules.formattedString() + "\n\n";
					model.addReservedModule(modules);
				}
				
				osp.setReservedModules("Reserved modules: \n========\n" + reserved);
				view.getReserveModulesPane().setVisible(false);
				view.changeTab(3);
				osp.getSaveOverview().setDisable(false);
			}
			else if(rmp.getCredits2() == 30 && model.getStudentCourse().getCourseName().equals("Software Engineering")) {
				rmp.getTerm2().setDisable(true);
				
				String reserved = "";
				
				for(Module modules: rmp.getReservedModulesTerm1()) {
					reserved += modules.formattedString() + "\n\n";
					model.addReservedModule(modules);
				}
				
				reserved += "\n";
				
				for(Module modules: rmp.getReservedModulesTerm2()) {
					reserved += modules.formattedString() + "\n\n";
					model.addReservedModule(modules);
				}
				
				osp.setReservedModules("Reserved modules: \n========\n" + reserved);
				view.getReserveModulesPane().setVisible(false);
				view.changeTab(3);
				osp.getSaveOverview().setDisable(false);
			}
		}
	}
	//---------------------------------------
	//Methods for the save overview pane
	private class SaveOverviewHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			try {
				PrintWriter saveDetails = new PrintWriter(new File("studentDetails.txt"));
				
				saveDetails.println(osp.getProfile() + "\n\n" + osp.getSelectedModules() +
									"\n" + osp.getReservedModule());
				
				saveDetails.close();
				
				alertDialogShow(AlertType.INFORMATION, "Details saved successfully!", "Student data has been saved in a text file.", 
						"Exiting the application now...");
				
				System.exit(0);
				
			} catch (FileNotFoundException e1) {
				System.out.println("Error");
			}
		}
	}
	//--------------------------------------------------
	//Methods for the menu
	private class SaveMenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("studentProfile.dat"));) {
				oos.writeObject(model);
				oos.flush();
				
				alertDialogShow(AlertType.INFORMATION, "Information saved successfully!", "Student profile has been successfully saved.", 
						"Please note that you must have finished the tab to save \n"
						+ "details and pressed either 'Confirm' or 'Submit'.");
			} 
			catch(IOException ioe) {
				System.out.println("Error");
			}
		}
	}
	
	private class LoadMenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("studentProfile.dat"));) {
				cspp.getStudentFirstName().clear();
				cspp.getStudentSurname().clear();
				cspp.getStudentPNumber().clear();
				cspp.getEmail().clear();
				cspp.getDate().getEditor().clear();
				
				smp.getYearLongModules().clear();
				smp.getUnselectedTerm1().clear();
				smp.getUnselectedTerm2().clear();
				smp.getSelectedTerm1().clear();
				smp.getSelectedTerm2().clear();
				
				smp.getYearLong().getSelectionModel().clearSelection();
				smp.getUnselectedModules1().getSelectionModel().clearSelection();
				smp.getUnselectedModules2().getSelectionModel().clearSelection();
				smp.getSelectedModules1().getSelectionModel().clearSelection();
				smp.getSelectedModules2().getSelectionModel().clearSelection();
				smp.updateTxtField1(0);
				smp.updateTxtField2(0);
				smp.setCredits1(0);
				smp.setCredits2(0);
				
				rmp.getNotSelectedModulesTerm1().clear();
				rmp.getNotSelectedModulesTerm2().clear();
				rmp.getReservedModulesTerm1().clear();
				rmp.getReservedModulesTerm2().clear();
				
				rmp.getNotSelectedTerm1().getSelectionModel().clearSelection();
				rmp.getNotSelectedTerm2().getSelectionModel().clearSelection();
				rmp.getReservedTerm1().getSelectionModel().clearSelection();
				rmp.getReservedTerm2().getSelectionModel().clearSelection();
				rmp.setCredits1(0);
				rmp.setCredits2(0);
				
				osp.setProfile("Profile will appear here");
				osp.setSelectedModules("Selected modules will appear here");
				osp.setReservedModules("Reserved modules will appear here");
				
				model = (StudentProfile) ois.readObject();
				ois.close();
				
				alertDialogShow(AlertType.INFORMATION, "Student profile loaded successfully!", null, "Student profile has been loaded.");
				
			} catch (IOException ioe) {
				System.out.println("Error");
			} catch (ClassNotFoundException cnf) {
				System.out.println("Error");
			}
			
			if(!model.getAllReservedModules().isEmpty()) {
				String reservedModules = "";
				for(Module modules: model.getAllReservedModules()) {
					reservedModules += modules.formattedString() + "\n\n";
				}
				osp.setReservedModules("Reserved Modules:\n======\n" + reservedModules);
				view.getReserveModulesPane().setVisible(false);
				
					String selectedModules = "";
					for(Module modules: model.getAllSelectedModules()) {
						selectedModules += modules.formattedString() + "\n\n";
					osp.setSelectedModules("Selected Modules:\n=======\n" + selectedModules);
					view.getSelectModulesPane().setVisible(false);
					
				}

					osp.setProfile("Student name: " + model.getStudentName().getFullName() + 
							   	   "\nStudent P Number: " + model.getStudentPnumber() + "\nStudent Email: " + 
							   	   model.getStudentEmail() + "\nDate submitted: " + model.getSubmissionDate() + 
							   	   "\nCourse: " + model.getStudentCourse());
					view.getCreateStudentProfilePane().setVisible(false);
					view.changeTab(3);
					
			} else if(!model.getAllSelectedModules().isEmpty()) {
				String selectedModules = "";
				for(Module modules: model.getAllSelectedModules()) {
				selectedModules += modules.formattedString() + "\n\n";
				osp.setSelectedModules("Selected Modules:\n=======\n" + selectedModules);
				view.getSelectModulesPane().setVisible(false);
				}

				osp.setProfile("Student name: " + model.getStudentName().getFullName() + 
						   	   "\nStudent P Number: " + model.getStudentPnumber() + "\nStudent Email: " + 
						   	   model.getStudentEmail() + "\nDate submitted: " + model.getSubmissionDate() + 
						   	   "\nCourse: " + model.getStudentCourse());
				view.getCreateStudentProfilePane().setVisible(false);
				view.getReserveModulesPane().setVisible(true);
				
				rmp.getTerm1().setDisable(false);
				rmp.getTerm2().setDisable(true);
				
				if(model.getStudentCourse().getCourseName().equals("Computer Science")) {
					for(Module module: compFirstTermModules()) {
						rmp.getNotSelectedModulesTerm1().add(module);
						rmp.getNotSelectedModulesTerm1().removeAll(model.getAllSelectedModules());
					}
					
					for(Module module: compSecondTermModules()) {
						rmp.getNotSelectedModulesTerm2().add(module);
						rmp.getNotSelectedModulesTerm2().removeAll(model.getAllSelectedModules());
					}
					rmp.getLblCredits1().setText(String.valueOf(30));
					rmp.getLblCredits2().setText(String.valueOf(45));
					}
				
				if(model.getStudentCourse().getCourseName().equals("Software Engineering")) {
					for(Module module: softFirstTermModules()) {
						rmp.getNotSelectedModulesTerm1().add(module);
						rmp.getNotSelectedModulesTerm1().removeAll(model.getAllSelectedModules());
					}
					
					for(Module module: softSecondTermModules()) {
						rmp.getNotSelectedModulesTerm2().add(module);
						rmp.getNotSelectedModulesTerm2().removeAll(model.getAllSelectedModules());
					}
					rmp.getLblCredits1().setText(String.valueOf(30));
					rmp.getLblCredits2().setText(String.valueOf(30));
					}
					view.changeTab(2);
				
			} else if(!model.getStudentName().getFirstName().equals("") && !model.getStudentName().getFamilyName().equals("") && 
					  !model.getStudentEmail().equals("") && !model.getStudentPnumber().equals("") && !model.getSubmissionDate().equals(null)) {
				if(model.getStudentCourse().getCourseName().equals("Computer Science")) {
					smp.addCompFirstModules(compFirstTermModules());
					smp.addCompSecondModules(compSecondTermModules());
					smp.addYearLongCompModules(yearLongComp());
					smp.addMandatoryModulesTerm1(compModulesMandatoryTerm1());
					smp.addMandatoryModulesTerm2(compModulesMandatoryTerm2());
					
				} else if(model.getStudentCourse().getCourseName().equals("Software Engineering")) {
					smp.addSoftFirstModules(softFirstTermModules());
					smp.addSoftSecondModules(softSecondTermModules());
					smp.addYearLongSoftModules(yearLongSoft());
					smp.addMandatorySoftModulesTerm1(softModulesMandatoryTerm1());
					smp.addMandatorySoftModulesTerm2(softModulesMandatoryTerm2());
				}
				
				osp.setProfile("Student name: " + model.getStudentName().getFullName() + 
					   	   "\nStudent P Number: " + model.getStudentPnumber() + "\nStudent Email: " + 
					   	   model.getStudentEmail() + "\nDate submitted: " + model.getSubmissionDate() + 
					   	   "\nCourse: " + model.getStudentCourse());
				view.getCreateStudentProfilePane().setVisible(false);
				view.getSelectModulesPane().setVisible(true);
				view.getReserveModulesPane().setVisible(true);
				smp.getResetBtn().setDisable(false);
				smp.getSubmitBtn().setDisable(false);
				rmp.getTerm1().setDisable(true);
				rmp.getTerm2().setDisable(true);
				
				view.changeTab(1);
			} else {
				view.changeTab(0);
				view.getCreateStudentProfilePane().setVisible(true);
				view.getSelectModulesPane().setVisible(true);
				view.getReserveModulesPane().setVisible(true);
				view.getOverviewResultsPane().getSaveOverview().setDisable(true);
			}
		}
	}
	
	private class AboutMenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			alertDialogShow(AlertType.INFORMATION, "About", "Student Final Year Module Booking", "Use this application to input your details "
										+ "and select your\nfinal year modules and reserve modules, then press"
										+ "\nSave Overview once everything has been completed.");
		}
	}
}
	
