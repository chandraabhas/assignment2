/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mininet.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mininet.controller.MiniNetController;
import com.mininet.exceptions.NoAvailableException;
import com.mininet.exceptions.NoParentException;
import com.mininet.exceptions.NoSuchAgeException;
import com.mininet.exceptions.NotToBeClassmatesException;
import com.mininet.exceptions.NotToBeColleaguesException;
import com.mininet.exceptions.NotToBeCoupledException;
import com.mininet.exceptions.NotToBeFriendsException;
import com.mininet.exceptions.TooYoungException;
import com.mininet.modal.AccountType;
import com.mininet.modal.Adult;
import com.mininet.modal.Child;
import com.mininet.modal.Person;
import com.mininet.modal.Sex;
import com.mininet.modal.State;
import com.mininet.modal.YoungChild;


/**
 *
 * @author abhishek
 */
public class FrontView extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * controller object for the app
	 */
	private MiniNetController controller;
	/**
	 * Creates new form FrontView
	 */
	public FrontView() {
		initComponents();
		//set output foreground to red
		output.setForeground(Color.red);
		list1.setModel(new DefaultListModel<>());
		list2.setModel(new DefaultListModel<>());

		// add listeners to Input panel
		addInPutPanelListeners();

		// add listeners to Delete panel
		addDeleteListListeners();

		// add listeners to Setting Relations panel
		addSettingRelationListeners();
		controller=new MiniNetController();
		if(controller.getPersons()==null)
			output.setText("No database connection or people.txt file found.");
		else if(controller.getPersons().isEmpty())
			output.setText("No datafound in neither connection nor people.txt");
		else 
			initializePersons();
	}

	/**
	 * adding all the listeners to Adding relations
	 */
	private void addSettingRelationListeners() {

		/**
		 * Action to perform when set Parent is clicked 
		 */
		Parent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p1=list1.getSelectedValue();
				Person p2=list2.getSelectedValue();
				if(p1==null || p2==null)
				{
					output.setText("Please select relation from in list 1 and to in list2");
					return;
				}
				output.setText(controller.setParent(p1,p2));

			}
		});
		/**
		 * Action to perform when set friend is clicked 
		 */
		friend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p1=list1.getSelectedValue();
				Person p2=list2.getSelectedValue();
				if(p1==null || p2==null)
				{
					output.setText("Please select relation from in list 1 and to in list2");
					return;
				}
				try {
					output.setText(controller.setFriend(p1,p2));
				} catch (TooYoungException e1) {
					output.setText(e1.getMessage());
				} catch (NotToBeFriendsException e1) {
					output.setText(e1.getMessage());
				}

			}
		});
		/**
		 * Action to perform when set Colleague is clicked 
		 */
		colleague.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p1=list1.getSelectedValue();
				Person p2=list2.getSelectedValue();
				if(p1==null || p2==null)
				{
					output.setText("Please select relation from in list 1 and to in list2");
					return;
				}
				try {
					output.setText(controller.setColleague(p1,p2));
				} catch (NotToBeColleaguesException e1) {
					output.setText(e1.getMessage());
				}
			}
		});

		/**
		 * Action to perform when set Classmate is clicked 
		 */
		classmate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p1=list1.getSelectedValue();
				Person p2=list2.getSelectedValue();
				if(p1==null || p2==null)
				{
					output.setText("Please select relation from in list 1 and to in list2");
					return;
				}
				try {
					output.setText(controller.setClassmate(p1,p2));
				} catch (NotToBeClassmatesException e1) {
					output.setText(e1.getMessage());				}
			}
		});

		/**
		 * Action to perform when set Couple is clicked 
		 */
		couple.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p1=list1.getSelectedValue();
				Person p2=list2.getSelectedValue();
				if(p1==null || p2==null)
				{
					output.setText("Please select relation from in list 1 and to in list2");
					return;
				}
				try {
					output.setText(controller.setCouple(p1,p2));
				} catch (NotToBeCoupledException | NoAvailableException e1) {
					output.setText(e1.getMessage());
				}
			}
		});

	}

	/**
	 * Add listeners to delete panel
	 */
	private void addDeleteListListeners() {
		/**
		 * Action to perform when Delete Person is clicked
		 */
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p=list1.getSelectedValue();
				if(p==null)
				{
					output.setText("Please select a person in list1 before deleting.");
					return;
				}
				try {
					output.setText(controller.deletePerson(p));
				} catch (NoParentException e1) {
					output.setText(e1.getMessage());
				}
				initializePersons();

			}
		});

		/**
		 * Action to perform when Display Person is clicked
		 */
		display.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p=list1.getSelectedValue();
				if(p==null)
				{
					output.setText("Please select a person from list 1 to display");
					return;
				}
				selectPerson(p);

			}


		});

		/**
		 * Action to perform when Show Childrens is clicked
		 */
		children.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p=list1.getSelectedValue();
				if(p==null)
				{
					output.setText("Please select a person from list 1 to get his/her Children");
					return;
				}
				output.setText(controller.getChildren(p));

			}


		});
		/**
		 * Action to perform when Show Parents is clicked
		 */

		showParents.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p=list1.getSelectedValue();
				if(p==null)
				{
					output.setText("Please select a person from list 1 to get his/her Parents");
					return;
				}
				output.setText(controller.getParents(p));

			}


		});

		/**
		 * Action to perform when Show all relations is clicked
		 */
		showAllRelations.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person p=list1.getSelectedValue();
				if(p==null)
				{
					output.setText("Please select a person from list 1 to get his/her relations");
					return;
				}
				output.setText(controller.getRelations(p));

			}


		});





	}

	/**
	 * Display the details of person
	 * @param p person whose details you want to display in Input panel
	 */
	private void selectPerson(Person p) {
		if(p instanceof Adult)
			AcType.setSelectedItem(AccountType.Adult);
		else if(p instanceof YoungChild)
			AcType.setSelectedItem(AccountType.YoungChild);
		else if(p instanceof Child)
			AcType.setSelectedItem(AccountType.Child);
		sex.setSelectedItem(p.getSex());
		state.setSelectedItem(p.getState());
		name.setText(p.getName());
		age.setText(p.getAge()+"");
		status.setText(p.getStatus());
		image.setText(p.getImage());
		//set image icon according to image string
		image.setIcon(new ImageIcon(new ImageIcon(p.getImage())
				.getImage().getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT))); // NOI18N

	}
	private void addInPutPanelListeners() {


		/**
		 * listener to handle click on Reset Button
		 * All Text Boxes are cleared
		 * And list are set to index 0
		 * image label is also cleared
		 */
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetControls();
			}
		});

		/**
		 * action to perform when image is clicked
		 */
		image.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)  
			{  
				setImage();
			} 
		});
		/**
		 * action to perform when Exit button is clicked
		 */
		exitApp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.persistData();
				System.exit(0);
			}
		});

		/**
		 * action to perform when Add Record Button is clicked
		 */
		add.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				addPerson();
			}
		});

		/**
		 * when window is closed, save data
		 */
		addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				controller.persistData();
			}
		});



	}

	/**
	 * add the person to the database if a connection is present 
	 * or add the person to temporary list
	 */
	private void addPerson() {
		try
		{
			AccountType sacType=(AccountType) AcType.getSelectedItem();
			String sName=name.getText();
			Sex sSex=(Sex) sex.getSelectedItem();
			String sAge=age.getText().trim();
			State sState=(State) state.getSelectedItem();
			String sImage=image.getText().trim();
			String sts=status.getText();
			/**
			 * if account type is Child/Young Child
			 */
			if(sacType==AccountType.Child ||sacType==AccountType.YoungChild)
			{
				//if Account type is Child
				if(AcType.getSelectedItem()==AccountType.Child)
				{
					int dialogResult = JOptionPane.showConfirmDialog (null, "You have Selected Child. Please make sure that you have selected Parents in two lists.","Warning",JOptionPane.OK_CANCEL_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
					}
					else
						return;
				}
				//if Account type is Young Child
				else if(AcType.getSelectedItem()==AccountType.YoungChild)
				{
					int dialogResult = JOptionPane.showConfirmDialog (null, "You have Selected YoungChild. Please make sure that you have selected Parents in two lists.","Warning",JOptionPane.OK_CANCEL_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
					}	
					else
						return;
				}
				Person parent1=list1.getSelectedValue();
				Person parent2=list2.getSelectedValue();
				/**
				 * if persons are not selected in both lists
				 */
				if(parent1==null || parent2==null)
				{
					output.setText("Please select parents of Child/Younger Child \nin the two lists given at the bottom of the page");
					return;
				}
				output.setText(controller.addPerson(sacType,sName,sSex,sAge,sState,sImage,parent1,parent2,sts));


			}
			else
				output.setText(controller.addPerson(sacType,sName,sSex,sAge,sState,sImage,sts));
			initializePersons();
		}
		catch(Exception e)
		{

		} catch (NoSuchAgeException e) {
			output.setText(e.getMessage());
			return;
		}

	}


	/**
	 * Open File Chooser for the user to choose an image file
	 * set the chosen file as 
	 */
	private void setImage() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			image.setBorder(null);
			File selectedFile = fileChooser.getSelectedFile();
			image.setText(selectedFile.getAbsolutePath());
			image.setIcon(new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath())
					.getImage().getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT))); // NOI18N
		}
	}
	/**
	 * All Text Boxes are cleared
	 * And list are set to index 0
	 * image label is also cleared
	 */
	private void resetControls()
	{
		AcType.setSelectedIndex(0);
		sex.setSelectedIndex(0);
		state.setSelectedIndex(0);
		name.setText("");
		age.setText("");
		status.setText("Status");
		list1.setSelectedIndex(-1);
		list2.setSelectedIndex(-1);
		status.setText("Status");
		image.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		image.setIcon(null);
		image.setText("Click to Select Image");
	}

	/**
	 * initialize the two list models 
	 * with the data in persons arrayList
	 */
	private void initializePersons() {

		/**
		 * Updating list1 of persons
		 */
		//get ListModel of list1
		DefaultListModel<Person> model = (DefaultListModel<Person>) list1.getModel();
		//clear all the model elements
		model.clear();
		//add all the persons in list to model
		for(Person p :controller.getPersons())
			model.addElement(p);
		/**
		 * Updating list2 of persons
		 */
		//get ListModel of list2
		model = (DefaultListModel<Person>) list2.getModel();

		//clear all the model elements
		model.clear();
		//add all the persons in list to model
		for(Person p :controller.getPersons())
			model.addElement(p);

	}


	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {

		children2 = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		AcType = new javax.swing.JComboBox<AccountType>();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		sex = new javax.swing.JComboBox<Sex>();
		name = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		age = new javax.swing.JTextField();
		state = new javax.swing.JComboBox<State>();
		image = new javax.swing.JLabel();
		add = new javax.swing.JButton();
		reset = new javax.swing.JButton();
		exitApp = new javax.swing.JButton();
		jScrollPane4 = new javax.swing.JScrollPane();
		status = new javax.swing.JTextArea();
		jLabel1 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		list1 = new javax.swing.JList<Person>();
		jPanel4 = new javax.swing.JPanel();
		jLabel9 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		list2 = new javax.swing.JList<Person>();
		delete = new javax.swing.JButton();
		display = new javax.swing.JButton();
		children = new javax.swing.JButton();
		showParents = new javax.swing.JButton();
		showAllRelations = new javax.swing.JButton();
		jLabel10 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		friend = new javax.swing.JButton();
		Parent = new javax.swing.JButton();
		couple = new javax.swing.JButton();
		classmate = new javax.swing.JButton();
		colleague = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		jLabel11 = new javax.swing.JLabel();
		jScrollPane3 = new javax.swing.JScrollPane();
		output = new javax.swing.JTextPane();

		children2.setText("Show Parents");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		AcType.setModel(new javax.swing.DefaultComboBoxModel<AccountType>(new AccountType[]
				{ AccountType.Adult, AccountType.Child, AccountType.YoungChild }));

		jLabel2.setText("Account Type");

		jLabel3.setText("Name");

		jLabel4.setText("Sex");

		jLabel7.setText("Age");
		jLabel7.setToolTipText("");

		sex.setModel(new javax.swing.DefaultComboBoxModel<Sex>(new Sex[] { Sex.Male, Sex.Female }));

		jLabel5.setText("State");



		state.setModel(new javax.swing.DefaultComboBoxModel<State>(new State[] { State.ACT, State.NSW, 
				State.NT, State.QLD, State.SA, State.TAS, State.VIC, State.WA}));

		image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		image.setText("Click to Select Image");
		image.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
		image.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		add.setText("Add Record");

		reset.setText("Reset");

		exitApp.setText("Exit Application");

		status.setColumns(20);
		status.setRows(5);
		status.setText("Status");
		jScrollPane4.setViewportView(status);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane4)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGap(10, 10, 10)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(2, 2, 2)
														.addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(AcType, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGap(2, 2, 2)
														.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																.addComponent(age)
																.addComponent(sex, 0, 203, Short.MAX_VALUE))))
										.addGap(20, 20, 20)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
										.addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(68, 68, 68))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
										.addComponent(add)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(reset)
										.addGap(18, 18, 18)
										.addComponent(exitApp)
										.addGap(22, 22, 22))))
				);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGap(30, 30, 30)
										.addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(add)
												.addComponent(reset)
												.addComponent(exitApp)))
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addContainerGap()
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(AcType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel2))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel3)
												.addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel4)
												.addComponent(sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(age, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel7))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel5)
												.addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addGap(29, 29, 29))
				);

		jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("MiniNet");

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE)
				);
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 438, Short.MAX_VALUE)
				);

		jLabel6.setText("Records Currently in System(List1)");

		jScrollPane1.setViewportView(list1);

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
								.addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap())
				);
		jPanel3Layout.setVerticalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel6)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane1)
						.addContainerGap())
				);

		jLabel9.setText("Records Currently in System(List2)");

		jScrollPane2.setViewportView(list2);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jScrollPane2)
								.addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
						.addContainerGap())
				);
		jPanel4Layout.setVerticalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel9)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane2)
						.addContainerGap())
				);

		delete.setText("Delete Selected");

		display.setText("Display Selected");

		children.setText("Show Children");

		showParents.setText("Show Parents");

		showAllRelations.setText("Show All Relations");

		jLabel10.setText("Set Relationship  between ");

		jLabel8.setText("List 1 Selected and List 2 Selected ");

		friend.setText("Set Friend");

		Parent.setText("Set Parent");

		couple.setText("Set Couple");

		classmate.setText("Set Classmate");

		colleague.setText("Set Colleague");

		jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		jLabel11.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
		jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel11.setText("Output");

		jScrollPane3.setViewportView(output);

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup()
						.addGap(26, 26, 26)
						.addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(33, Short.MAX_VALUE))
				.addGroup(jPanel5Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane3)
						.addContainerGap())
				);
		jPanel5Layout.setVerticalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup()
						.addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane3)
						.addContainerGap())
				);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addGap(45, 45, 45)
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 992, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup()
														.addContainerGap()
														.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																		.addComponent(display, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(children, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(showParents, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(showAllRelations, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																.addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(18, 18, 18)
														.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
																		.addComponent(colleague, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(classmate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(couple, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(Parent, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(friend, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))))
												.addGroup(layout.createSequentialGroup()
														.addGap(25, 25, 25)
														.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(18, 18, 18)
														.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 302, Short.MAX_VALUE)
										.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addGap(10, 10, 10)
										.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(layout.createSequentialGroup()
										.addGap(18, 18, 18)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup()
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
												.addGroup(layout.createSequentialGroup()
														.addGap(86, 86, 86)
														.addComponent(delete)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(display)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(children)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(showParents)
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(showAllRelations)
														.addGap(0, 0, Short.MAX_VALUE))
												.addGroup(layout.createSequentialGroup()
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addGroup(layout.createSequentialGroup()
																		.addGap(14, 14, 14)
																		.addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(jLabel8)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(friend)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(Parent)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(couple)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(classmate)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(colleague)
																		.addContainerGap(50, Short.MAX_VALUE))))))))
				);

		pack();
	}// </editor-fold>                        



	/**
	 * @param args the command line arguments
	 */

	// Variables declaration - do not modify                     
	private javax.swing.JComboBox<AccountType> AcType;
	private javax.swing.JButton Parent;
	private javax.swing.JButton add;
	private javax.swing.JTextField age;
	private javax.swing.JButton children;
	private javax.swing.JButton showParents;
	private javax.swing.JButton children2;
	private javax.swing.JButton showAllRelations;
	private javax.swing.JButton classmate;
	private javax.swing.JButton colleague;
	private javax.swing.JButton couple;
	private javax.swing.JButton delete;
	private javax.swing.JButton display;
	private javax.swing.JButton friend;
	private javax.swing.JLabel image;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JList<Person> list1;
	private javax.swing.JList<Person> list2;
	private javax.swing.JTextField name;
	private javax.swing.JTextPane output;
	private javax.swing.JButton reset;
	private javax.swing.JButton exitApp;
	private javax.swing.JComboBox<Sex> sex;
	private javax.swing.JComboBox<State> state;
	private javax.swing.JTextArea status;
	// End of variables declaration                   
}
