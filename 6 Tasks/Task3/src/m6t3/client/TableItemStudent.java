package m6t3.client;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import m6t3.server.Student;

class TableItemStudent extends TableItem {
	Student student;
	boolean synchronous;

	public TableItemStudent(Table parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void checkSubclass() {
	}
}
