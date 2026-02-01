package org.lokova.classroom;

import java.util.Collection;
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
	private JList<ScoreRecord> recordList;

	public StudentUI(Collection<Student> students) {
		studentList = new JList<>(Student.getAllArray());
		recordList = new JList<>();
	}

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

		// recordList
	}

	public void refresh() {
		Student selected = studentList.getSelectedValue();
		if (selected == null) {
			infoArea.setText("");
			recordList.setListData(new ScoreRecord[0]);
		} else {
			infoArea.setText(selected.getInfo());
			recordList.setListData(selected.getRecordsArray());
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
