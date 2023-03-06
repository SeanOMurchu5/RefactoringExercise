import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

//View represents ui of the application
public class View extends JDialog implements ActionListener {
	private Controller con;
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
			
			public View(Controller con) {
				
				this.con = con;
			}
			
			// initialize dialog container
			public Container addRecordDialog(){
				JTextField idField, ppsField, surnameField, firstNameField, salaryField;
				JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
				JButton save, cancel;
				EmployeeDetails parent;
				
				JPanel empDetails, buttonPanel;
				empDetails = new JPanel(new MigLayout());
				buttonPanel = new JPanel();
				JTextField field;

				empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

				empDetails.add(new JLabel("ID:"), "growx, pushx");
				empDetails.add(idField = new JTextField(20), "growx, pushx, wrap");
				idField.setEditable(false);
				

				empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
				empDetails.add(ppsField = new JTextField(20), "growx, pushx, wrap");

				empDetails.add(new JLabel("Surname:"), "growx, pushx");
				empDetails.add(surnameField = new JTextField(20), "growx, pushx, wrap");

				empDetails.add(new JLabel("First Name:"), "growx, pushx");
				empDetails.add(firstNameField = new JTextField(20), "growx, pushx, wrap");

				empDetails.add(new JLabel("Gender:"), "growx, pushx");
				empDetails.add(genderCombo = new JComboBox<String>(this.gender), "growx, pushx, wrap");

				empDetails.add(new JLabel("Department:"), "growx, pushx");
				empDetails.add(departmentCombo = new JComboBox<String>(this.department), "growx, pushx, wrap");

				empDetails.add(new JLabel("Salary:"), "growx, pushx");
				empDetails.add(salaryField = new JTextField(20), "growx, pushx, wrap");

				empDetails.add(new JLabel("Full Time:"), "growx, pushx");
				empDetails.add(fullTimeCombo = new JComboBox<String>(this.fullTime), "growx, pushx, wrap");

				buttonPanel.add(save = new JButton("Save"));
				save.addActionListener(this);
				save.requestFocus();
				buttonPanel.add(cancel = new JButton("Cancel"));
				cancel.addActionListener(this);

				empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");
				// loop through all panel components and add fonts and listeners
				for (int i = 0; i < empDetails.getComponentCount(); i++) {
					empDetails.getComponent(i).setFont(this.font1);
					if (empDetails.getComponent(i) instanceof JComboBox) {
						empDetails.getComponent(i).setBackground(Color.WHITE);
					}// end if
					else if(empDetails.getComponent(i) instanceof JTextField){
						field = (JTextField) empDetails.getComponent(i);
						if(field == ppsField)
							field.setDocument(new JTextFieldLimit(9));
						else
						field.setDocument(new JTextFieldLimit(20));
					}// end else if
				}// end for
				idField.setText(Integer.toString(this.getNextFreeId()));
				return empDetails;
			}
			
			//Search By ID dialog
			// initialize search container
			public Container searchPane() {
				EmployeeDetails parent;
				JButton search, cancel;
				JTextField searchField;
				JPanel searchPanel = new JPanel(new GridLayout(3, 1));
				JPanel textPanel = new JPanel();
				JPanel buttonPanel = new JPanel();
				JLabel searchLabel;

				searchPanel.add(new JLabel("Search by ID"));

				textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				textPanel.add(searchLabel = new JLabel("Enter ID:"));
				searchLabel.setFont(this.font1);
				textPanel.add(searchField = new JTextField(20));
				searchField.setFont(this.font1);
				searchField.setDocument(new JTextFieldLimit(20));
				
				buttonPanel.add(search = new JButton("Search"));
				search.addActionListener(this);
				search.requestFocus();
				
				buttonPanel.add(cancel = new JButton("Cancel"));
				cancel.addActionListener(this);

				searchPanel.add(textPanel);
				searchPanel.add(buttonPanel);

				return searchPanel;
			}// end searchPane
			
			//Create employee summary dialog
			// initialise container
			public Container summaryPane() {
				Vector<Object> allEmployees;
				JButton back;
				JPanel summaryDialog = new JPanel(new MigLayout());
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				JTable employeeTable;
				DefaultTableModel tableModel;
				// column center alignment
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				// column left alignment 
				DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
				Vector<String> header = new Vector<String>();
				// header names
				String[] headerName = { "ID", "PPS Number", "Surname", "First Name", "Gender", "Department", "Salary",
						"Full Time" };
				// column widths
				int[] colWidth = { 15, 100, 120, 120, 50, 120, 80, 80 };
				centerRenderer.setHorizontalAlignment(JLabel.CENTER);
				leftRenderer.setHorizontalAlignment(JLabel.LEFT);
				// add headers
				for (int i = 0; i < headerName.length; i++) {
					header.addElement(headerName[i]);
				}// end for
				// construct table and choose table model for each column
				tableModel = new DefaultTableModel(allEmployees, header) {
					public Class getColumnClass(int c) {
						switch (c) {
						case 0:
							return Integer.class;
						case 4:
							return Character.class;
						case 6:
							return Double.class;
						case 7:
							return Boolean.class;
						default:
							return String.class;
						}// end switch
					}// end getColumnClass
				};

				employeeTable = new JTable(tableModel);
				// add header names to table
				for (int i = 0; i < employeeTable.getColumnCount(); i++) {
					employeeTable.getColumn(headerName[i]).setMinWidth(colWidth[i]);
				}// end for
				// set alignments
				employeeTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
				employeeTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
				employeeTable.getColumnModel().getColumn(6).setCellRenderer(new DecimalFormatRenderer());

				employeeTable.setEnabled(false);
				employeeTable.setPreferredScrollableViewportSize(new Dimension(800, (15 * employeeTable.getRowCount() + 15)));
				employeeTable.setAutoCreateRowSorter(true);
				JScrollPane scrollPane = new JScrollPane(employeeTable);

				buttonPanel.add(back = new JButton("Back"));
				back.addActionListener(this);
				back.setToolTipText("Return to main screen");
				
				summaryDialog.add(buttonPanel,"growx, pushx, wrap");
				summaryDialog.add(scrollPane,"growx, pushx, wrap");
				scrollPane.setBorder(BorderFactory.createTitledBorder("Employee Details"));
				
				return summaryDialog;
			}// end summaryPane

			// format for salary column
			static class DecimalFormatRenderer extends DefaultTableCellRenderer {
				 private static final DecimalFormat format = new DecimalFormat(
				 "\u20ac ###,###,##0.00" );

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {

					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					 JLabel label = (JLabel) c;
					 label.setHorizontalAlignment(JLabel.RIGHT);
					 // format salary column
					value = format.format((Number) value);

					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}// end getTableCellRendererComponent
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
}
