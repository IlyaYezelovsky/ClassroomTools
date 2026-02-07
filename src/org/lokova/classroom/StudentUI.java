package org.lokova.classroom;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class StudentUI {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				StudentUI window = new StudentUI();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame frame;
	private JTextArea textArea;
	private JList<ScoreRecord> list_1;

	/**
	 * Create the application.
	 */
	public StudentUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("学生管理");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JList<Student> list = new JList<>(Student.getAllArray());
		list.addListSelectionListener(e -> {
			textArea.setText(list.getSelectedValue().getInfo());
			list_1.setListData(list.getSelectedValue().getRecordsArray());
		});
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(15);
		list.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);

		JButton addButton = new JButton("添加");
		addButton.addActionListener(e -> new EditStudentUI());
		panel_1.add(addButton);
		addButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));

		JButton editButton = new JButton("编辑");
		editButton.addActionListener(e -> {
			if (list.getSelectedValue() != null) {
				new EditStudentUI(list.getSelectedValue());
			}
		});
		panel_1.add(editButton);
		editButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));

		JButton delButton = new JButton("删除");
		delButton.addActionListener(e -> {
			if (list.getSelectedValue() != null) {
				list.getSelectedValue().delete();
			}
		});
		delButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(delButton);

		textArea = new JTextArea();
		textArea.setRows(18);
		textArea.setColumns(20);
		textArea.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		frame.getContentPane().add(textArea);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1);

		list_1 = new JList<>();
		list_1.setVisibleRowCount(15);
		scrollPane_1.setViewportView(list_1);
		list_1.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3);

		JButton addRecordButton = new JButton("添加");
		addRecordButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_3.add(addRecordButton);

		JButton editRecordButton = new JButton("编辑");
		editRecordButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_3.add(editRecordButton);

		JButton delRecordButton = new JButton("删除");
		delRecordButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_3.add(delRecordButton);
	}

}
