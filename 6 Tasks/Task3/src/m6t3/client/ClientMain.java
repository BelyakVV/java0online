package m6t3.client;

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

public class ClientMain {
	
	static ClientMain client;

	Connection connection;	
	
	boolean running = true;
	
	int syncProgress;
	Shell shell;
	Table table;
//	private volatile int checksum = 0;
	private Button btnAdd;
	private Button btnModify;
	private Button btnExit;

	private Button btnDelete;

	private boolean admin = false;

	private MenuItem mntmUsers;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		client = new ClientMain();
		try {
			client.open();
		} catch (Exception e) {
			System.err.println("Couldn't open main client window.");
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		connection  = new Connection(this);
		while (!shell.isDisposed()) {
			while (!connection.receiver.studentsQueue.isEmpty()) {
				Student student = connection.receiver.studentsQueue.poll();
				if (null == student) {
					System.err.println("Attempt to merge null into students table");
				} else {
					mergeStudent(student);
				}
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
				connection.disconnect();
			}
		});
		shell.setMinimumSize(new Point(400, 300));
		shell.setSize(400, 300);
		shell.setText("Архив");
		shell.setLayout(new FormLayout());
		
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

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
//		table.addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusGained(FocusEvent e) {
//				checkTable();
//			}
//		});
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
		
		btnAdd = new Button(shell, SWT.NONE);
		fd_table.bottom = new FormAttachment(btnAdd, -6);
		btnAdd.setEnabled(false);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new StudentEditDialog(client).open();
			}
		});
		FormData fd_btnAdd = new FormData();
		fd_btnAdd.left = new FormAttachment(0, 10);
		fd_btnAdd.bottom = new FormAttachment(statusBar, -6);
		btnAdd.setLayoutData(fd_btnAdd);
		btnAdd.setText("Добавить");
		
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
		
		btnDelete = new Button(shell, SWT.NONE);
		btnDelete.setEnabled(false);
		fd_btnModify.left = new FormAttachment(btnDelete, -158);
		fd_btnModify.right = new FormAttachment(btnDelete, -6);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				Student student = (Student) table.getItem(index).getData();
				connection.outQueue.add(student.suicide());
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
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(100);
		tblclmnNumber.setText("Номер");
		
		TableColumn tblclmnFullName = new TableColumn(table, SWT.NONE);
		tblclmnFullName.setWidth(100);
		tblclmnFullName.setText("Фамилия, имя, отчество");
		
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
				new ChangePassDialog(client).open();
			}
		});
		mntmChangePass.setText("Изменить свой пароль");
		
		mntmUsers = new MenuItem(menu, SWT.NONE);
		mntmUsers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				new UsersWindow(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL).open();
				new UsersWindow(client).open();
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
	
	private void editStudent() {
		if (!admin) return;
		int index = table.getSelectionIndex();
		if (index < 0) return;
		Student student = (Student) table.getItem(index).getData();
		new StudentEditDialog(client, student).open();		
//		table.setFocus();
	}
	
//	public int getChecksum() {
//		return checksum;
//	}

	public void mergeStudent(Student srvStudent) {
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			Student student = (Student) item.getData();
//			System.out.println("Student " + i + ": " + student.getSerial());
			if (student.id == srvStudent.id) {
				if (srvStudent.getSerial() < 0) {
					if (table.isSelected(i)) {
						if (table.getItemCount() > (i + 1)) {
							table.select(i + 1);
						} else if (i > 0) {
							table.select(i - 1);
						} else {
							btnDelete.setEnabled(false);
							btnModify.setEnabled(false);
						}
					}
//					System.out.print("Before: " + Integer.toHexString(checksum) + ", student: " + Integer.toHexString(student.hashCode()));
//					checksum += -student.hashCode();
//					System.out.println(", after: " + Integer.toHexString(checksum));
					table.remove(i);
					return;
				} else if (srvStudent.getSerial() > student.getSerial()) {
//					checksum += srvStudent.hashCode() - student.hashCode();
					fillTableItem(item, srvStudent);
				}
				return;
			}
		}
		TableItem item = new TableItem(table, SWT.NONE);
//		System.out.print("Before: " + Integer.toHexString(checksum) + ", student: " + Integer.toHexString(srvStudent.hashCode()));
//		checksum += srvStudent.hashCode();
//		System.out.print(", after: " + Integer.toHexString(checksum));
		fillTableItem(item, srvStudent);
//		System.out.println(" , saved: " + Integer.toHexString(((Student) item.getData()).hashCode()));
		if (table.getItemCount() == 1) table.select(0);
	}

	private static void fillTableItem(TableItem item, Student student) {
		item.setData(student);
		item.setText(0, student.getNumber());
		item.setText(1, student.getFullName());
	}

	public void mergeStudents(Queue<Student> srvStudents) {
		table.setRedraw(false);
		while (!srvStudents.isEmpty()) {
			mergeStudent(srvStudents.poll());
		}
		table.setRedraw(true);
		checkTable();
	}

	private void checkTable() {
		if (!admin) return;
		if (table.getItemCount() > 0) {
			btnModify.setEnabled(true);
			btnDelete.setEnabled(true);
		} else {
			btnModify.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
		btnAdd.setEnabled(admin);
		mntmUsers.setEnabled(admin);
	}
}
