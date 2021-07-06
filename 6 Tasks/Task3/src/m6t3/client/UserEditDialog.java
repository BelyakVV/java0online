package m6t3.client;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import m6t3.common.Student;
import m6t3.common.User;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class UserEditDialog extends Dialog {

	private User user;
	private final ClientMain client;
	private final UsersWindow usersWindow;

	private Object result;
	private Shell shell;
	private Text txtLogin;
	private Text txtPass;
	private Text txtPassAgain;

	private static Color defBgrdColor;
	private static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
	private Button btnAdmin;
	private boolean loginIsValid;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UserEditDialog(Shell parent, int style) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
//		setText("Новый пользователь");
		client = null;
		usersWindow = null;
		loginIsValid = false;
	}
	
	public UserEditDialog(UsersWindow usersWindow) {
		super(usersWindow.client.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		client = usersWindow.client;
		this.usersWindow = usersWindow;
		user = new User();
		loginIsValid = false;
		setText("Новый пользователь");
	}

	public UserEditDialog(UsersWindow usersWindow, User user) {
		super(usersWindow.client.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		client = usersWindow.client;
		this.usersWindow = usersWindow;
		this.user = user;
		loginIsValid = true;
		setText("Изменить пользователя");
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
		shell.setSize(384, 207);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		FormData fd_composite = new FormData();
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(100, -10);
		composite.setLayoutData(fd_composite);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submit();				
			}
		});
		fd_composite.bottom = new FormAttachment(100, -42);
		
		Label lblLogin = new Label(composite, SWT.NONE);
		lblLogin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLogin.setText("Логин");
		
		txtLogin = new Text(composite, SWT.BORDER);
		txtLogin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				loginIsValid = useLogin();
				if (loginIsValid) {
					txtLogin.setBackground(defBgrdColor);
				} else {
					txtLogin.setBackground(RED);
				}
			}
		});
		txtLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtLogin.setText(user.login);
		
		Label lblPass = new Label(composite, SWT.NONE);
		lblPass.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPass.setText("Пароль");
		
		txtPass = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtPass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassAgain = new Label(composite, SWT.NONE);
		lblPassAgain.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassAgain.setText("Подтверждение пароля");
		
		txtPassAgain = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtPassAgain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		defBgrdColor = txtPassAgain.getBackground();
		
		Label lblAdmin = new Label(composite, SWT.NONE);
		lblAdmin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAdmin.setText("Администратор");
		
		btnAdmin = new Button(composite, SWT.CHECK);
		btnAdmin.setSelection(user.admin);
		
		FormData fd_btnOk = new FormData();
		fd_btnOk.top = new FormAttachment(composite, 6);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		fd_btnOk.right = new FormAttachment(btnCancel, -37);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.left = new FormAttachment(0, 202);
		fd_btnCancel.top = new FormAttachment(composite, 6);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Отмена");

	}

	private boolean useLogin() {
		String login = txtLogin.getText();
		if (usersWindow.loginIsBusy(login, user.id)) return false;
		return user.setLogin(login);
	}

	private void submit() {
		//TODO: check data being sent
		if (!loginIsValid) return;
		if (!usePassword()) return;
		user.admin = btnAdmin.getSelection();
		client.connection.outQueue.add(user);
		shell.close();
	}

	private boolean usePassword() {
		char[] password = txtPass.getTextChars();
		if ((password.length < 1) || !Arrays.equals(password, txtPassAgain.getTextChars())) return false;
		try {
			user.setPassword(password);
		} catch (Exception e) {
//		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			System.err.println("Password: " + Arrays.toString(password));
			e.printStackTrace();
			return false;
		}
		return true;
	}

//	/**
//	 * Actually update the user on the server or return false if it is impossible
//	 * TODO: pull this method out of this class
//	 * @param user User to be updated or created (if user.id == INVALID_ID)
//	 * @return true if success
//	 */
//	boolean doUpdateTransaction(User user) {
//		// TODO Auto-generated method stub
//		return false;
//	}
}
