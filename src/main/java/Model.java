import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

//Represents data for the application
public class Model extends JDialog implements ActionListener{
	// decimal format for inactive currency text field
		private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
		// decimal format for active currency text field
		private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
		// hold object start position in file
		private long currentByteStart = 0;
		private RandomFile application = new RandomFile();
		// display files in File Chooser only with extension .dat
		private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
		// hold file name and path for current file in use
		private File file;
		// holds true or false if any changes are made for text fields
		private boolean change = false;
		// holds true or false if any changes are made for file content
		boolean changesMade = false;
		private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
				searchBySurname, listAll, closeApp;
		private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
				saveChange, cancelChange;
		private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
		private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
		private static EmployeeDetails frame = new EmployeeDetails();
		// font for labels, text fields and combo boxes
		Font font1 = new Font("SansSerif", Font.BOLD, 16);
		// holds automatically generated file name
		String generatedFileName;
		// holds current Employee object
		Employee currentEmployee;
		JTextField searchByIdField, searchBySurnameField;
		// gender combo box values
		String[] gender = { "", "M", "F" };
		// department combo box values
		String[] department = { "", "Administration", "Production", "Transport", "Management" };
		// full time combo box values
		String[] fullTime = { "", "Yes", "No" };
	public Model() {
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
