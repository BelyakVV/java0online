package m6t3.client;

import static m6t3.common.Tranceiver.SYNC_USERS_REQUEST;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.TableItem;

import m6t3.common.User;

public class UsersWindow extends Dialog {

	final ClientTransmitter transmitter;
	final UsersWindow me = this;
	
	protected Object result;
	protected Shell shell;
	private Table table;
	private TableColumn tblclmnLogin;
	private TableColumn tblclmnAdmin;
	private Button btnDelete;
	private Button btnEdit;
	final Queue<User> inQueue = new LinkedList<>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UsersWindow(Shell parent, int style) {
		super(parent, style);
		setText("Управление пользователями");
		transmitter = null;
	}

	public UsersWindow(Shell parent, Connection connection) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Управление пользователями");
		transmitter = connection.transmitter;
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		transmitter.send(SYNC_USERS_REQUEST);
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			while (!inQueue.isEmpty()) {
				mergeUser(inQueue.poll());
			}
			checkTable();
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
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (SWT.CR == e.character) editUser();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editUser();
			}
		});
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
				new UserEditDialog(shell, me).open();
			}
		});
		fd_table.bottom = new FormAttachment(btnNew, -6);
		FormData fd_btnNew = new FormData();
		fd_btnNew.bottom = new FormAttachment(100, -10);
		fd_btnNew.left = new FormAttachment(0, 10);
		btnNew.setLayoutData(fd_btnNew);
		btnNew.setText("Новый");
		
		btnDelete = new Button(shell, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				User user = (User) table.getItem(index).getData();
				transmitter.send(user.suicide());
			}
		});
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
		
		btnEdit = new Button(shell, SWT.NONE);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editUser();
			}
		});
		btnEdit.setEnabled(false);
		fd_btnDelete.left = new FormAttachment(btnEdit, 6);
		FormData fd_btnEdit = new FormData();
		fd_btnEdit.bottom = new FormAttachment(btnNew, 0, SWT.BOTTOM);
		fd_btnEdit.left = new FormAttachment(btnNew, 6);
		btnEdit.setLayoutData(fd_btnEdit);
		btnEdit.setText("Изменить");
	}
	
	private void mergeUser(User srvUser) {
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			User user = (User) item.getData();
			if (user.id == srvUser.id) {
				if (srvUser.getSerial() < 0) {
					if (table.isSelected(i)) {
						if (table.getItemCount() > (i + 1)) {
							table.select(i + 1);
						} else if (i > 0) {
							table.select(i - 1);
						} 
					}
					table.remove(i);					
					return;
				} else if (srvUser.getSerial() > user.getSerial()) {
					fillTableItem(item, srvUser);
				}
				return;
			}
		}
		TableItem item = new TableItem(table, SWT.NONE);
		fillTableItem(item, srvUser);
		if (table.getItemCount() == 1) table.select(0);
	}

	private void checkTable() {
		if (table.getItemCount() > 0) {
			btnEdit.setEnabled(true);
			TableItem selectedItem = table.getSelection()[0];
			User selectedUser = (User) selectedItem.getData();
			if (selectedUser.isAdmin()) {
				btnDelete.setEnabled(!noMoreAdmins(selectedUser.id));
			} else {
				btnDelete.setEnabled(true);
			}
		} else {
			btnDelete.setEnabled(false);
			btnEdit.setEnabled(false);
		}
	}
	
	private void editUser() {
		int index = table.getSelectionIndex();
		if (index < 0) return;
		User user = (User) table.getItem(index).getData();
		new UserEditDialog(shell, me, user).open();	
	}

	private static void fillTableItem(TableItem item, User user) {
		item.setData(user);
		item.setText(0, user.getLogin());
		item.setText(1, user.isAdmin() ? "+" : "");
	}

	public boolean loginIsBusy(String login, int excludedId) {
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			User user = (User) item.getData();
			if (user.id == excludedId) continue;
			if (user.getLogin() == login) return true;
		}
		return false;
	}

	public boolean noMoreAdmins(int excludedId) {
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			User user = (User) item.getData();
			if (user.id == excludedId) continue;
			if (user.isAdmin()) return false;
		}
		return true;
	}
}
