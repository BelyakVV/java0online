
package m6t3.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Login extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text login;
	private Text passwd;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Login(Shell parent, int style) {
		super(parent, style);
		setText("Вход в архив");
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(219, 153);
		shell.setText("Вход в архив");
		
		login = new Text(shell, SWT.BORDER);
		login.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == 13) passwd.setFocus();
			}
		});
		login.setBounds(79, 3, 126, 26);
		
		Label lblLogin = new Label(shell, SWT.NONE);
		lblLogin.setBounds(10, 10, 63, 19);
		lblLogin.setText("Логин:");
		
		Label lblPasswd = new Label(shell, SWT.NONE);
		lblPasswd.setBounds(10, 42, 63, 19);
		lblPasswd.setText("Пароль");
		
		passwd = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		passwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == 13) proceed();
			}
		});
		passwd.setBounds(79, 35, 126, 26);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				proceed();
			}
		});
		btnOk.setBounds(10, 83, 89, 32);
		btnOk.setText("Ок");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(116, 83, 89, 32);
		btnCancel.setText("Отмена");
	}
	
	void proceed() {
		shell.close();
	}
}
