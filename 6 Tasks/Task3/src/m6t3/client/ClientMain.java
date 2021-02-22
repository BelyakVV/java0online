package m6t3.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import m6t3.server.ServerMain;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class ClientMain {

	static final String DEFAULT_SERVER_HOST = "localhost";
	static final int DEFAULT_TIMEOUT = 3000;
	
	static ClientMain client;
	
	String serverHost = DEFAULT_SERVER_HOST;
	int serverPort = ServerMain.DEFAULT_IP_PORT;
	Socket socket = new Socket();
	int syncProgress;
	protected Shell shell;
	private Table table;
	private Button btnExit;
	private Button btnAdd;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		client = new ClientMain();
		try {
			client.open();
		} catch (Exception e) {
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
		if (!serverConnect()) {
			shell.close();
		};
//		Login loginDialog = new Login(shell, SWT.NONE);
//		loginDialog.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private boolean serverConnect() {
		while (!socket.isConnected()) {
			try {
//				System.out.println(serverHost + ':' + serverPort);
				socket.connect(new InetSocketAddress(serverHost, serverPort), DEFAULT_TIMEOUT);
			} catch (IOException e) {
//				e.printStackTrace(System.err);
				socket = new Socket();
				ConnectionDialog connDlg = new ConnectionDialog(shell, SWT.NONE);
				if (!(Boolean) connDlg.open()) {
					break;
				}
			}
		}
		return socket.isConnected();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		FormData fd_table = new FormData();
		fd_table.left = new FormAttachment(0, 10);
		fd_table.right = new FormAttachment(100, -10);
		fd_table.top = new FormAttachment(0, 5);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		btnAdd = new Button(shell, SWT.NONE);
		fd_table.bottom = new FormAttachment(btnAdd, -6);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				var newStudent = new TableItemStudent(table, SWT.NONE);
				newStudent.setText(new String[]{
						"номер", "имя"
				});
			}
		});
		FormData fd_btnAdd = new FormData();
		fd_btnAdd.left = new FormAttachment(0, 10);
		fd_btnAdd.bottom = new FormAttachment(statusBar, -6);
		btnAdd.setLayoutData(fd_btnAdd);
		btnAdd.setText("Добавить");
		
		Button btnModify = new Button(shell, SWT.NONE);
		FormData fd_btnModify = new FormData();
		fd_btnModify.top = new FormAttachment(table, 6);
		fd_btnModify.bottom = new FormAttachment(statusBar, -6);
		btnModify.setLayoutData(fd_btnModify);
		btnModify.setText("Изменить");
		
		Button btnDelete = new Button(shell, SWT.NONE);
		fd_btnModify.left = new FormAttachment(btnDelete, -158);
		fd_btnModify.right = new FormAttachment(btnDelete, -6);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.remove(table.getSelectionIndices());
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
	}
}
