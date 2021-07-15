package m6t3.client;

import static m6t3.client.LoginDialog.RED;
import static m6t3.client.LoginDialog.defBgrdColor;
import static m6t3.client.StudentEditDialog.SELECT_ALL_TEXT;
import static m6t3.client.StudentEditDialog.TRAVERSE_OR_EXIT;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 *  Change password dialog. Users both with and without administrative rights 
 * can use it. A user can enter his current password, then a new password should
 * be typed twice.
 * 
 * @author aabyodj *
 */
public class ChangePassDialog extends Dialog {

	Object result;
	private Shell shell;
		
	private Text txtOldPass;
	private Text txtNewPass;
	private Text txtNewPassAgain;
	
	/** Connection to a server */
	final Connection connection;

	/**
	 * Standard constructor for WindowBuilder
	 * @param parent
	 * @param style
	 */
	public ChangePassDialog(Shell parent, int style) {
		super(parent, style);
		connection = null;
	}

	/**
	 * Create the Change Password Dialog
	 * @param parent
	 * @param connection Connection to the server
	 */
	public ChangePassDialog(Shell parent, Connection connection) {
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
		setText("Изменить свой пароль");
		shell = new Shell(getParent(), getStyle());
		shell.setSize(380, 182);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(100, -9);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.bottom = new FormAttachment(0, 108);
		composite.setLayoutData(fd_composite);
		
		Label lblCurrentPass = new Label(composite, SWT.NONE);
		lblCurrentPass.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCurrentPass.setText("Текущий пароль");
		
		txtOldPass = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtOldPass.addFocusListener(SELECT_ALL_TEXT);
		txtOldPass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtOldPass.getCharCount() < 1) {
					txtOldPass.setBackground(RED);
				} else {
					txtOldPass.setBackground(defBgrdColor);
				}
			}
		});
		txtOldPass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtOldPass.addKeyListener(TRAVERSE_OR_EXIT);
		
		Label lblNewPass = new Label(composite, SWT.NONE);
		lblNewPass.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewPass.setText("Новый пароль");
		
		txtNewPass = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtNewPass.addFocusListener(SELECT_ALL_TEXT);
		txtNewPass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtNewPass.getCharCount() < 1) {
					txtNewPass.setBackground(RED);
				} else if (txtNewPassAgain.getCharCount() < 1){
					txtNewPass.setBackground(defBgrdColor);
				} else if (Arrays.equals(txtNewPass.getTextChars(), txtNewPassAgain.getTextChars())) {
					txtNewPass.setBackground(defBgrdColor);
					txtNewPassAgain.setBackground(defBgrdColor);
				} else {
					txtNewPass.setBackground(RED);
					txtNewPassAgain.setBackground(RED);
				}
			}
		});
		txtNewPass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtNewPass.addKeyListener(TRAVERSE_OR_EXIT);
		
		Label lblNewPassAgain = new Label(composite, SWT.NONE);
		lblNewPassAgain.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewPassAgain.setText("Новый пароль ещё раз");
		
		txtNewPassAgain = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtNewPassAgain.addFocusListener(SELECT_ALL_TEXT);
		txtNewPassAgain.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtNewPassAgain.getCharCount() < 1) {
					txtNewPassAgain.setBackground(RED);
				} else if (txtNewPass.getCharCount() < 1) {
					txtNewPassAgain.setBackground(defBgrdColor);
				} else if (Arrays.equals(txtNewPass.getTextChars(), txtNewPassAgain.getTextChars())) {
					txtNewPass.setBackground(defBgrdColor);
					txtNewPassAgain.setBackground(defBgrdColor);
				} else {
					txtNewPass.setBackground(RED);
					txtNewPassAgain.setBackground(RED);
				}
			}
		});
		txtNewPassAgain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtNewPassAgain.addKeyListener(new KeyAdapter() {
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
		fd_btnOk.width = 87;
		fd_btnOk.top = new FormAttachment(composite, 6);
		fd_btnOk.left = new FormAttachment(0, 87);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(btnOk, 0, SWT.BOTTOM);
		fd_btnCancel.left = new FormAttachment(btnOk, 19);
		fd_btnCancel.width = 87;
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Отмена");

	}

	/** Try to change the password using data typed into the fields */
	private void submit() {
		if (txtNewPass.getCharCount() < 1) return;
		char[] oldPass = txtOldPass.getTextChars();
		char[] newPass = txtNewPass.getTextChars();
		if (!Arrays.equals(newPass, txtNewPassAgain.getTextChars())) {
			return;
		}
		if (connection.changePass(oldPass, newPass)) {
			//Success
			shell.close();
		} else {
			//Current password is wrong
			txtOldPass.setBackground(RED);
		}
	}
}
