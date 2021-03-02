package m6t3.client;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import m6t3.common.Student;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class EditDialog extends Dialog {

	Student student;
	ClientMain client;
	protected Object result;
	protected Shell shell;
	private Text txtNumber;
	private Text txtSurname;
	private Text txtName;
	private Text txtPatronymic;

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
	
	public EditDialog(Shell parent, Student student) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
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
		shell.setSize(392, 220);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				student.setNumber(txtNumber.getText());
				student.setSurname(txtSurname.getText());
				student.setName(txtName.getText());
				student.setPatronymic(txtPatronymic.getText());
				client.connection.sendStudent(student);
				shell.close();
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
		txtNumber.setText(student.getNumber());
		GridData gd_txtNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNumber.widthHint = 142;
		txtNumber.setLayoutData(gd_txtNumber);
		
		Label lblSurname = new Label(composite, SWT.NONE);
		lblSurname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSurname.setText("Фамилия");
		
		txtSurname = new Text(composite, SWT.BORDER);
		txtSurname.setText(student.getSurname());
		txtSurname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Имя");
		
		txtName = new Text(composite, SWT.BORDER);
		txtName.setText(student.getName());
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPatronymic = new Label(composite, SWT.NONE);
		lblPatronymic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPatronymic.setText("Отчество");
		
		txtPatronymic = new Text(composite, SWT.BORDER);
		txtPatronymic.setText(student.getPatronymic());
		txtPatronymic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}
}
