package m6t3.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ClientMain {

	protected Shell shell;
	private Table table;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ClientMain window = new ClientMain();
			window.open();
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
		Login login = new Login(shell, SWT.NONE);
		login.open();
		while (!shell.isDisposed()) {
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
		shell.setMinimumSize(new Point(400, 300));
		shell.setSize(436, 291);
		shell.setText("Архив");
		shell.setLayout(new FormLayout());
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 5);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				var newStudent = new TableItemStudent(table, SWT.NONE);
				newStudent.setText(new String[]{
						"номер", "имя"
				});
			}
		});
		fd_table.bottom = new FormAttachment(100, -48);
		FormData fd_btnAdd = new FormData();
		fd_btnAdd.bottom = new FormAttachment(100, -10);
		fd_btnAdd.left = new FormAttachment(table, 0, SWT.LEFT);
		btnAdd.setLayoutData(fd_btnAdd);
		btnAdd.setText("Добавить");
		
		Button btnModify = new Button(shell, SWT.NONE);
		FormData fd_btnModify = new FormData();
		fd_btnModify.left = new FormAttachment(btnAdd, 6);
		fd_btnModify.top = new FormAttachment(table, 6);
		fd_btnModify.bottom = new FormAttachment(100, -10);
		btnModify.setLayoutData(fd_btnModify);
		btnModify.setText("Изменить");
		
		Button btnDelete = new Button(shell, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.remove(table.getSelectionIndices());
			}
		});
		fd_btnModify.right = new FormAttachment(100, -253);
		FormData fd_btnDelete = new FormData();
		fd_btnDelete.bottom = new FormAttachment(btnAdd, 0, SWT.BOTTOM);
		fd_btnDelete.left = new FormAttachment(btnModify, 6);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Удалить");
		
		Button btnExit = new Button(shell, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		fd_table.right = new FormAttachment(btnExit, 0, SWT.RIGHT);
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(100);
		tblclmnNumber.setText("Номер");
		
		TableColumn tblclmnFullName = new TableColumn(table, SWT.NONE);
		tblclmnFullName.setWidth(100);
		tblclmnFullName.setText("Фамилия, имя, отчество");
		
		FormData fd_btnExit = new FormData();
		fd_btnExit.bottom = new FormAttachment(100, -10);
		fd_btnExit.right = new FormAttachment(100, -10);
		btnExit.setLayoutData(fd_btnExit);
		btnExit.setText("Выход");

	}
}
