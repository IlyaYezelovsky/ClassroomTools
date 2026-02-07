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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GroupUI {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				GroupUI window = new GroupUI();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame frame;
	private JTextArea infoArea;
	private JTextField nameField;

	/**
	 * Create the application.
	 */
	public GroupUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("组管理");
		frame.setBounds(100, 100, 550, 250);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JList<Group> list = new JList<>(Group.getAllArray());
		list.addListSelectionListener(e -> {
			if (list.getSelectedValue() != null) {
				infoArea.setText(list.getSelectedValue().getInfo());
			}
		});
		list.setVisibleRowCount(10);
		scrollPane.setViewportView(list);
		list.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);

		nameField = new JTextField();
		nameField.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(nameField);
		nameField.setColumns(10);

		JButton addButton = new JButton("添加");
		addButton.addActionListener(e -> {
			Group neo = new Group(nameField.getText());
			list.setListData(Group.getAllArray());
			list.setSelectedValue(neo, true);
		});
		addButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(addButton);

		JButton renameButton = new JButton("重命名");
		renameButton.addActionListener(e -> {
			if (list.getSelectedValue() != null) {
				list.getSelectedValue().setName(nameField.getText());
			}
		});
		renameButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(renameButton);

		JButton delButton = new JButton("删除");
		delButton.addActionListener(e -> {
			if (list.getSelectedValue() != null) {
				list.getSelectedValue().delete();
			}
		});
		delButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(delButton);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);

		infoArea = new JTextArea();
		infoArea.setRows(10);
		infoArea.setColumns(25);
		panel_2.add(infoArea);
	}

}
