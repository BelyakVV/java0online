package m6t3.client;

import static m6t3.common.Tranciever.NEW_USER;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import m6t3.common.User;

public class UsersWindow extends Dialog {

	ClientMain client;
	
	protected Object result;
	protected Shell shell;
	private Table table;
	private TableColumn tblclmnLogin;
	private TableColumn tblclmnAdmin;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UsersWindow(Shell parent, int style) {
		super(parent, style);
		setText("Управление пользователями");
	}

	public UsersWindow(ClientMain client) {
		this(client.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.client = client;
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(338, 219);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0);
		fd_table.left = new FormAttachment(0);
		fd_table.right = new FormAttachment(0, 333);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tblclmnLogin = new TableColumn(table, SWT.NONE);
		tblclmnLogin.setWidth(100);
		tblclmnLogin.setText("Логин");
		
		tblclmnAdmin = new TableColumn(table, SWT.NONE);
		tblclmnAdmin.setWidth(100);
		tblclmnAdmin.setText("Админ");
		
		Button btnNew = new Button(shell, SWT.NONE);
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new UserEditDialog(client).open();
			}
		});
		fd_table.bottom = new FormAttachment(btnNew, -6);
		FormData fd_btnNew = new FormData();
		fd_btnNew.bottom = new FormAttachment(100, -10);
		fd_btnNew.left = new FormAttachment(0, 10);
		btnNew.setLayoutData(fd_btnNew);
		btnNew.setText("Новый");
		
		Button btnDelete = new Button(shell, SWT.NONE);
		btnDelete.setEnabled(false);
		FormData fd_btnDelete = new FormData();
		fd_btnDelete.bottom = new FormAttachment(btnNew, 0, SWT.BOTTOM);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Удалить");
		
		Button btnDone = new Button(shell, SWT.NONE);
		btnDone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		FormData fd_btnDone = new FormData();
		fd_btnDone.bottom = new FormAttachment(btnNew, 0, SWT.BOTTOM);
		fd_btnDone.right = new FormAttachment(100, -10);
		btnDone.setLayoutData(fd_btnDone);
		btnDone.setText("Готово");
		
		Button btnEdit = new Button(shell, SWT.NONE);
		btnEdit.setEnabled(false);
		fd_btnDelete.left = new FormAttachment(btnEdit, 6);
		FormData fd_btnEdit = new FormData();
		fd_btnEdit.bottom = new FormAttachment(btnNew, 0, SWT.BOTTOM);
		fd_btnEdit.left = new FormAttachment(btnNew, 6);
		btnEdit.setLayoutData(fd_btnEdit);
		btnEdit.setText("Изменить");
	}
}
