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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import m6t3.common.Student;

public class EditDialog extends Dialog {

	Student student;
	ClientMain client;
	protected Object result;
	protected Shell shell;
	private Text txtNumber;
	private Text txtSurname;
	private Text txtName;
	private Text txtPatronymic;

	private static Color defBgrdColor;
	private static final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */
	public EditDialog(Shell parent, int style) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Новый студент");
		student = new Student();
	}
	
	public EditDialog(ClientMain client) {
		super(client.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.client = client;
		setText("Новый студент");
		student = new Student();
	}

	public EditDialog(ClientMain client, Student student) {
		super(client.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.client = client;
		setText("Редактирование студента");
		this.student = student;
	}
	
	public EditDialog(Shell parent, Student student) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}
	
	public static final FocusAdapter SELECT_ALL_TEXT = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			((Text) e.widget).selectAll();
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
				text.setBackground(defBgrdColor);
			}
		}
	};

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		txtNumber.setFocus();
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
		shell.setSize(392, 220);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submit();
			}
		});
		FormData fd_btnOk = new FormData();
		fd_btnOk.right = new FormAttachment(0, 176);
		fd_btnOk.top = new FormAttachment(0, 149);
		fd_btnOk.left = new FormAttachment(0, 87);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shell, SWT.CENTER);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(0, 297);
		fd_btnCancel.top = new FormAttachment(0, 149);
		fd_btnCancel.left = new FormAttachment(0, 208);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setText("Отмена");
		
		Composite composite = new Composite(shell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 143);
		fd_composite.right = new FormAttachment(0, 377);
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblNumber = new Label(composite, SWT.NONE);
		lblNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNumber.setText("Номер");
		
		txtNumber = new Text(composite, SWT.BORDER);
		defBgrdColor = txtNumber.getBackground();
		txtNumber.addFocusListener(SELECT_ALL_TEXT);
		txtNumber.addTraverseListener(CHECK_BLANK);
		txtNumber.addKeyListener(TRAVERSE_OR_EXIT);
		txtNumber.setText(student.getNumber());
		GridData gd_txtNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNumber.widthHint = 142;
		txtNumber.setLayoutData(gd_txtNumber);
		
		Label lblSurname = new Label(composite, SWT.NONE);
		lblSurname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSurname.setText("Фамилия");
		
		txtSurname = new Text(composite, SWT.BORDER);
		txtSurname.addFocusListener(SELECT_ALL_TEXT);
		txtSurname.addTraverseListener(CHECK_BLANK);
		txtSurname.addKeyListener(TRAVERSE_OR_EXIT);
		txtSurname.setText(student.getSurname());
		txtSurname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Имя");
		
		txtName = new Text(composite, SWT.BORDER);
		txtName.addFocusListener(SELECT_ALL_TEXT);
		txtName.addTraverseListener(CHECK_BLANK);
		txtName.addKeyListener(TRAVERSE_OR_EXIT);
		txtName.setText(student.getName());
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPatronymic = new Label(composite, SWT.NONE);
		lblPatronymic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPatronymic.setText("Отчество");
		
		txtPatronymic = new Text(composite, SWT.BORDER);
		txtPatronymic.addFocusListener(SELECT_ALL_TEXT);
		txtPatronymic.addTraverseListener(CHECK_BLANK);
		txtPatronymic.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (SWT.CR == e.character) {
					submit();
				} else if (e.character == SWT.ESC) {
					((Control) e.widget).getShell().close();
				}
			}
		});
		txtPatronymic.setText(student.getPatronymic());
		txtPatronymic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	private void submit() {
		boolean ok = true;
		ok &= checkText(txtPatronymic);
		ok &= checkText(txtName);
		ok &= checkText(txtSurname);
		ok &= checkText(txtNumber);
		if (!ok) return;
		student.setNumber(txtNumber.getText());
		student.setSurname(txtSurname.getText());
		student.setName(txtName.getText());
		student.setPatronymic(txtPatronymic.getText());
		client.connection.outQueue.add(student);
		shell.close();
	}
	
	boolean checkText(Text text) {
		boolean result = !text.getText().isBlank();
		if (!result) {
			text.setBackground(RED);
			text.setFocus();
		} else {
			text.setBackground(defBgrdColor);
		}
		return result;
	}
}
