package m6t3.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import m6t3.server.Student;

class TableItemStudent extends TableItem {
	Student student;
//	boolean synchronous;

	public TableItemStudent(Table parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public TableItemStudent(Table parent, Student student) {
		super(parent, SWT.NONE);
		this.student = student;
		this.setText(student.getFullName());
	}

	@Override
	protected void checkSubclass() {
	}
}
