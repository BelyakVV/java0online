package m6t3.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import m6t3.common.Student;

class TableItemStudent extends TableItem {
	Student student;
//	boolean synchronous;

	public TableItemStudent(Table parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public TableItemStudent(Table parent, Student student) {
		super(parent, SWT.NONE);
		setStudent(student);
	}

	public void setStudent(Student student) {
		this.student = student;
		setText(0, student.getNumber());
		setText(1, student.getFullName());
	}
	
	@Override
	protected void checkSubclass() {
	}
}
