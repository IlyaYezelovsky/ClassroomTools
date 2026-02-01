package org.lokova.classroom;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class StudentUI implements UI {

	private JFrame frame;
	private JPanel panel;
	private JList<Student> studentList;
	private JTextArea infoArea;

	@Override
	public void go() {

		frame = new JFrame("学生管理");
		panel = new JPanel();
		JScrollPane scroller = new JScrollPane(studentList);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		studentList.setVisibleRowCount(10);
		studentList.setFixedCellWidth(100);
		studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentList.addListSelectionListener(l -> {
			refresh();
		});

		infoArea = new JTextArea();
		infoArea.setEditable(false);
	}

	public void refresh() {
		Student selected = studentList.getSelectedValue();
		if (selected == null) {
			infoArea.setText("");
		} else {
			infoArea.setText(selected.getInfo());
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
