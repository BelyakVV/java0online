package m6t3.client;

import static m6t3.client.StudentEditDialog.CANNOT_BE_EMPTY;
import static m6t3.client.StudentEditDialog.SELECT_ALL_TEXT;
import static m6t3.client.StudentEditDialog.TRAVERSE_OR_EXIT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class LoginDialog extends Dialog {

	protected Object result = false;
	protected Shell shell;
	private Text txtLogin;
	private Text txtPass;
	
	final Connection connection;
	
	public static Color defBgrdColor;
	public static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public LoginDialog(Shell parent, int style) {
		super(parent, style);
		connection = null;
	}
	
	LoginDialog(Shell parent, Connection connection) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.connection = connection; 
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
		shell.setSize(250, 151);
		shell.setText("Авторизация");
		shell.setLayout(new FormLayout());
		
		Group group = new Group(shell, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.bottom = new FormAttachment(0, 79);
		fd_group.right = new FormAttachment(0, 235);
		group.setLayoutData(fd_group);
		
		Label lblLogin = new Label(group, SWT.NONE);
		lblLogin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLogin.setText("Логин");
		
		txtLogin = new Text(group, SWT.BORDER);
		txtLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtLogin.setText(connection.getLogin());
		txtLogin.addFocusListener(CANNOT_BE_EMPTY);
		txtLogin.addFocusListener(SELECT_ALL_TEXT);
		txtLogin.addKeyListener(TRAVERSE_OR_EXIT);
		defBgrdColor = txtLogin.getBackground();
		
		Label lblPass = new Label(group, SWT.NONE);
		lblPass.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPass.setText("Пароль");
		
		txtPass = new Text(group, SWT.BORDER | SWT.PASSWORD);
		txtPass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtPass.addFocusListener(CANNOT_BE_EMPTY);
		txtPass.addFocusListener(SELECT_ALL_TEXT);
		txtPass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (SWT.CR == e.character) {
					submit();
				} else if (e.character == SWT.ESC) {
					((Control) e.widget).getShell().close();
				}
			}
		});
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submit();
			}
		});
		FormData fd_btnOk = new FormData();
		fd_btnOk.top = new FormAttachment(group, 6);
		fd_btnOk.right = new FormAttachment(100, -136);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("Ок");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();			
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(group, 6);
		fd_btnCancel.right = new FormAttachment(100, -65);
		fd_btnCancel.left = new FormAttachment(btnOk, 6);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Отмена");

	}

	private void submit() {
		if (txtLogin.getCharCount() < 1) return;
		if (txtPass.getCharCount() < 1) {
			txtPass.setBackground(RED);
			return;
		}
		connection.setLogin(txtLogin.getText());
		connection.setPassword(txtPass.getTextChars());
		result = true;
		shell.close();
	}
}
