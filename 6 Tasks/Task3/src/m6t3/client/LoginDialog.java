package m6t3.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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

/**
 *  The Login dialog.
 *
 * @author aabyodj
 */
public class LoginDialog extends Dialog {

	public static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
	public static final Color DEFAULT_BACKGROUND = SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND);

	
	public static final FocusAdapter SELECT_ALL_TEXT = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			((Text) e.widget).selectAll();
		}
	};
	
	public static final FocusAdapter CANNOT_BE_EMPTY = new FocusAdapter() {
		@Override
		public void focusLost(FocusEvent e) {
			Text text = (Text) e.widget;
			if (text.getCharCount() > 0) {
				text.setBackground(DEFAULT_BACKGROUND);
			} else {
				text.setBackground(RED);
			}
		}		
	};
	
	public static final KeyAdapter TRAVERSE_OR_EXIT = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (SWT.CR == e.character) {
				((Control) e.widget).traverse(SWT.TRAVERSE_TAB_NEXT);
			} else if (e.character == SWT.ESC) {
				((Control) e.widget).getShell().close();
			}
		}
	};
	
	static final TraverseListener CHECK_BLANK = new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
			Text text = (Text) e.widget;
			if (text.getText().isBlank()) {
				text.setBackground(RED);
				e.doit = false;
			} else {
				text.setBackground(DEFAULT_BACKGROUND);
			}
		}
	};
	
	protected Object result = false;
	protected Shell shell;
	private Text txtLogin;
	private Text txtPass;
	
	/** The client-side connection controller */
	final Connection connection;
	

	/**
	 * Create the dialog. This is the standard constructor for WindowBuilder.
	 * @param parent
	 * @param style
	 */
	public LoginDialog(Shell parent, int style) {
		super(parent, style);
		connection = null;
	}
	
	/**
	 * Create the Login dialog.
	 * @param parent
	 * @param connection The client-side network connection controller
	 */
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
		txtLogin.setBackground(DEFAULT_BACKGROUND);
		
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
		txtPass.setBackground(DEFAULT_BACKGROUND);
		
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
