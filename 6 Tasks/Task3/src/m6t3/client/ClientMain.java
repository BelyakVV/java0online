package m6t3.client;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import m6t3.common.Student;
import m6t3.common.User;

/**
 * 	The main client window. The contents of the archive are shown here. Users
 * without administrative rights can only view the contents and change their
 * own password, while administrators also can modify the archive and other
 * users' accounts. 
 *
 * @author aabyodj
 */
public class ClientMain {
	
	private static ClientMain me;
	
	private Display display;
	private Shell shell;
	
	private Table table;

	private Button btnAdd;
	private Button btnModify;
	private Button btnDelete;
	private Button btnExit;

	/** A menu item to open a Users Management Dialog */
	private MenuItem mntmUsers;
	
	/** A connection to a server */
	private Connection connection;	
	
	/** A queue of items to be incorporated into the table */
	final Queue<Student> inQueue = new LinkedList<>();
	
	/** Whether the user logged in has administrative rights */
	private boolean admin = false;
	
	/** Whether the application is running or going to close */
	private boolean running = true;

	private boolean connectionDialogActive = false;

	private boolean loginDialogActive;

	/** The Users Management Dialog */
	private UsersWindow usersWindow = null;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		me = new ClientMain();
		try {
			me.open();
		} catch (Exception e) {
			System.err.println("Couldn't open main client window.");
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		connection  = new Connection(this, table);
		while (!shell.isDisposed()) {
			
			//Include items from queue into the table
			while (!inQueue.isEmpty()) {
				mergeStudent0(inQueue.poll());
			}
			checkTable();
			
			if (!display.readAndDispatch()) {				
				display.sleep();
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				running = false;
				connection.terminate();
			}
		});
		shell.setMinimumSize(new Point(400, 300));
		shell.setSize(400, 300);
		shell.setText("Архив");
		shell.setLayout(new FormLayout());
		
		//The piece of code below neither may be completed nor easily deleted
		//due to a bug in WindowBuilder		
		Composite statusBar = new Composite(shell, SWT.BORDER);
		FormLayout fl_statusBar = new FormLayout();
		fl_statusBar.marginWidth = 4;
		fl_statusBar.marginHeight = 2;
		statusBar.setLayout(fl_statusBar);
		FormData fd_statusBar = new FormData();
		fd_statusBar.right = new FormAttachment(100);
		fd_statusBar.bottom = new FormAttachment(100);
		fd_statusBar.left = new FormAttachment(0);
		statusBar.setLayoutData(fd_statusBar);
		Label lblSyncComplete = new Label(statusBar, SWT.NONE);
		FormData fd_lblSyncComplete = new FormData();
		fd_lblSyncComplete.right = new FormAttachment(100);
		fd_lblSyncComplete.bottom = new FormAttachment(100);
		lblSyncComplete.setLayoutData(fd_lblSyncComplete);
		lblSyncComplete.setText("завершена");		
		Label lblSyncProgress = new Label(statusBar, SWT.NONE);
		FormData fd_lblSyncProgress = new FormData();
		fd_lblSyncProgress.bottom = new FormAttachment(lblSyncComplete, 0, SWT.BOTTOM);
		fd_lblSyncProgress.right = new FormAttachment(lblSyncComplete, -6);
		lblSyncProgress.setLayoutData(fd_lblSyncProgress);
		lblSyncProgress.setText("Синхронизация с сервером");		
		ProgressBar progressBar = new ProgressBar(statusBar, SWT.NONE);
		progressBar.setVisible(false);
		FormData fd_progressBar = new FormData();
		fd_progressBar.bottom = new FormAttachment(lblSyncComplete, 0, SWT.BOTTOM);
		fd_progressBar.top = new FormAttachment(lblSyncComplete, 0, SWT.TOP);
		fd_progressBar.left = new FormAttachment(lblSyncComplete, 0, SWT.LEFT);
		fd_progressBar.right = new FormAttachment(lblSyncComplete, 0, SWT.RIGHT);
		progressBar.setLayoutData(fd_progressBar);

		//The main table
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (SWT.CR == e.character) editStudent();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editStudent();
			}
		});
		FormData fd_table = new FormData();
		fd_table.left = new FormAttachment(0, 10);
		fd_table.right = new FormAttachment(100, -10);
		fd_table.top = new FormAttachment(0, 5);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(100);
		tblclmnNumber.setText("Номер");
		
		TableColumn tblclmnFullName = new TableColumn(table, SWT.NONE);
		tblclmnFullName.setWidth(100);
		tblclmnFullName.setText("Фамилия, имя, отчество");
		
		//Add a new item into the archive
		btnAdd = new Button(shell, SWT.NONE);
		fd_table.bottom = new FormAttachment(btnAdd, -6);
		btnAdd.setEnabled(false);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new StudentEditDialog(shell, connection).open();
			}
		});
		FormData fd_btnAdd = new FormData();
		fd_btnAdd.left = new FormAttachment(0, 10);
		fd_btnAdd.bottom = new FormAttachment(statusBar, -6);
		btnAdd.setLayoutData(fd_btnAdd);
		btnAdd.setText("Добавить");
		
		//Modify a selected item
		btnModify = new Button(shell, SWT.NONE);
		btnModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editStudent();
			}
		});
		btnModify.setEnabled(false);
		FormData fd_btnModify = new FormData();
		fd_btnModify.top = new FormAttachment(table, 6);
		fd_btnModify.bottom = new FormAttachment(statusBar, -6);
		btnModify.setLayoutData(fd_btnModify);
		btnModify.setText("Изменить");
		
		//Delete a selected item
		btnDelete = new Button(shell, SWT.NONE);
		btnDelete.setEnabled(false);
		fd_btnModify.left = new FormAttachment(btnDelete, -158);
		fd_btnModify.right = new FormAttachment(btnDelete, -6);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				Student student = (Student) table.getItem(index).getData();
				connection.transmitter.send(student.suicide());
			}
		});
		FormData fd_btnDelete = new FormData();
		fd_btnDelete.left = new FormAttachment(btnAdd, 94);
		fd_btnDelete.bottom = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Удалить");
		
		btnExit = new Button(shell, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}			
		});
		FormData fd_btnExit = new FormData();
		fd_btnExit.right = new FormAttachment(100, -10);
		fd_btnExit.bottom = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		btnExit.setLayoutData(fd_btnExit);
		btnExit.setText("Выход");		
		
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		MenuItem menuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuItem.setText("Меню");
		
		Menu menu = new Menu(menuItem);
		menuItem.setMenu(menu);

		MenuItem mntmChangePass = new MenuItem(menu, SWT.NONE);
		mntmChangePass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ChangePassDialog(shell, connection).open();
			}
		});
		mntmChangePass.setText("Изменить свой пароль");
		
		mntmUsers = new MenuItem(menu, SWT.NONE);
		mntmUsers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				usersWindow = new UsersWindow(shell, connection);
				usersWindow.open();
				usersWindow = null;
			}
		});
		mntmUsers.setEnabled(false);
		mntmUsers.setText("Управление пользователями");
		
		MenuItem mntmExit = new MenuItem(menu, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		mntmExit.setText("Выход");
	}	
	
	/** Edit currently selected item */
	private void editStudent() {
		if (!admin) return;
		int index = table.getSelectionIndex();
		if (index < 0) return;
		Student student = (Student) table.getItem(index).getData();
		new StudentEditDialog(shell, connection, student).open();	
	}

	/**
	 *  Include an item (actually a student's account) into the table
	 * @param srvStudent Student's account to be included
	 */
	public void mergeStudent(Student srvStudent) {
		inQueue.add(srvStudent);
	}
	
	/**
	 *  Actually include a student's account into the table.
	 * @param srvStudent Student's account to be included
	 */
	private void mergeStudent0(Student srvStudent) {
		
		//First check whether this account is already included
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			Student student = (Student) item.getData();
			if (student.id == srvStudent.id) {	
				
				//It is included. Now decide whether update it or delete
				if (srvStudent.getSerial() < 0) {
					
					//Delete it from the table. If it is selected, then move the marker away
					if (table.isSelected(i)) {
						if (table.getItemCount() > (i + 1)) {
							table.select(i + 1);
						} else if (i > 0) {
							table.select(i - 1);
						} else { //Or disable buttons if cannot move
							btnDelete.setEnabled(false);
							btnModify.setEnabled(false);
						}
					}
					table.remove(i);
					return;
					
				//Update existing item only if the one which has arrived is newer than that in the table
				} else if (srvStudent.getSerial() > student.getSerial()) {
					fillTableItem(item, srvStudent);
				}
				return;
			}
		}
		
		//There is no sutable item in the table. Will create one. 
		TableItem item = new TableItem(table, SWT.NONE);
		fillTableItem(item, srvStudent);
		if (table.getItemCount() == 1) table.select(0);
	}

	/**
	 *  Attach a student account to a table row.
	 * @param item 
	 * @param student
	 */
	private static void fillTableItem(TableItem item, Student student) {
		item.setData(student);
		item.setText(0, student.getNumber());
		item.setText(1, student.getFullName());
	}

	/**
	 * Check the state of the table and enable or disable buttons if needed
	 */
	private void checkTable() {
		if (!admin) return;	//Only administrators can modify the archive
		if (table.getItemCount() > 0) {
			btnModify.setEnabled(true);
			btnDelete.setEnabled(true);
		} else {
			btnModify.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

	/**
	 *  Enable or disable modifying controls according to user's rights.
	 * @param admin Whether the user is administrator
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
		display.asyncExec(()-> {			
			btnAdd.setEnabled(admin);
			mntmUsers.setEnabled(admin);
			if (admin) {
				checkTable();
			} else {
				btnModify.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
	}

	/**
	 * Whether the application is running or going to close.
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	public Display getDisplay() {
		return display;
	}

	/**
	 *  Show the Network Connection Settings Dialog. Close the application if 
	 * the Cancel button is hit.
	 */
	public void showReconnDlg() {
		if (connectionDialogActive) return;
		connectionDialogActive = true;
		display.syncExec(()-> {
			ConnectionDialog dialog = new ConnectionDialog(shell, connection);
			dialog.open();
			if (!(boolean) dialog.result) shell.close();
		});
		connectionDialogActive = false;
	}

	/**
	 * Whether the user logged in has administrative rights.
	 * @return
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 *  Show the Login dialog. Close the application if the Cancel button 
	 * is hit.
	 */
	public void showLoginDialog() {
		if (loginDialogActive) return;
		loginDialogActive = true;
		display.syncExec(()-> {
			LoginDialog dialog = new LoginDialog(shell, connection);
			dialog.open();
			if (!(boolean) dialog.result) shell.close();
		});
		loginDialogActive = false;
	}

	/**
	 * Set the login name to be shown in the window.
	 * @param login
	 */
	public void setLogin(String login) {
		display.syncExec(()-> shell.setText("Архив (" + login + ')'));
	}

	/**
	 *  Include a user's account into the Users Managtement Window table, if it
	 * is open.
	 * @param srvUser
	 */
	public void mergeUser(User srvUser) {
		if (!admin || null == usersWindow) return;
		usersWindow.inQueue.add(srvUser);
	}

	/**
	 *  Get an instance of network connection object.
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}	
}
