package m6t3.client;

import static m6t3.client.LoginDialog.RED;
import static m6t3.client.LoginDialog.defBgrdColor;
import static m6t3.client.StudentEditDialog.TRAVERSE_OR_EXIT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class ConnectionDialog extends Dialog {
	
	static final int MAX_PORT = 65535;
	final Connection connection;
	protected Object result = false;
	protected Shell shell;
	private Text txtHost;
	private Label lblPort;
	private Spinner spnPort;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	ConnectionDialog(Shell parent, Connection connection) {
		super(parent, SWT.NONE);
		this.connection = connection;
	}
	
	public ConnectionDialog(Shell parent, int style) {
		super(parent, style);
		connection = null;
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
		setText("Соединение с сервером");
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(227, 167);
		shell.setText(getText());
		
		Label lblHost = new Label(shell, SWT.NONE);
		lblHost.setBounds(10, 13, 48, 19);
		lblHost.setText("Сервер");
		
		txtHost = new Text(shell, SWT.BORDER);
		txtHost.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				checkHostname();
			}
		});
		txtHost.addKeyListener(TRAVERSE_OR_EXIT);
		txtHost.setText(connection.getServerHost());
		txtHost.setBounds(64, 10, 147, 26);
		
		lblPort = new Label(shell, SWT.NONE);
		lblPort.setBounds(26, 54, 32, 19);
		lblPort.setText("Порт");
		
		spnPort = new Spinner(shell, SWT.BORDER);
		spnPort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					submit();
				}
			}
		});
		spnPort.setMaximum(MAX_PORT);
		spnPort.setBounds(64, 42, 147, 44);
		spnPort.setSelection(connection.getServerPort());
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submit();
			}
		});
		btnOk.setBounds(10, 96, 89, 32);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setBounds(122, 96, 89, 32);
		btnCancel.setText("Отмена");

	}

	private void checkHostname() {
		if (txtHost.getCharCount() > 0) {
			txtHost.setBackground(defBgrdColor);
		} else {
			txtHost.setBackground(RED);
		}
	}

	protected void submit() {
		connection.setServerHost(txtHost.getText());
		connection.setServerPort(spnPort.getSelection());
		result = true;
		shell.close();
	}	
}
