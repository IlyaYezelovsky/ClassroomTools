package org.lokova.classroom;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window.Type;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class EditStudentUI {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				EditStudentUI window = new EditStudentUI();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JFrame frame;
	private JTextField nameField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField codeField;
	private Student editee;

	/**
	 * Create the application.
	 */
	public EditStudentUI() {
		initialize();
	}

	public EditStudentUI(Student editee) {
		this.editee = editee;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		frame.setTitle("编辑学生");
		frame.setType(Type.POPUP);
		frame.setBounds(100, 100, 300, 200);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);

		JLabel nameLabel = new JLabel("姓名");
		panel.add(nameLabel);
		nameLabel.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));

		nameField = new JTextField();
		nameField.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel.add(nameField);
		nameField.setColumns(20);
		if (!isNew()) {
			nameField.setText(editee.getName());
		}

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);

		JLabel sexLabel = new JLabel("性别");
		sexLabel.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(sexLabel);

		JRadioButton maleOption = new JRadioButton("男");
		buttonGroup.add(maleOption);
		maleOption.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(maleOption);

		JRadioButton femaleOption = new JRadioButton("女");
		buttonGroup.add(femaleOption);
		femaleOption.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_1.add(femaleOption);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);

		JLabel codeLabel = new JLabel("学号");
		codeLabel.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_2.add(codeLabel);

		codeField = new JTextField();
		codeField.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_2.add(codeField);
		codeField.setColumns(16);
		if (!isNew()) {
			codeField.setText(String.valueOf(editee.getNumber()));
		}

		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3);

		JLabel groupLabel = new JLabel("组别");
		groupLabel.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_3.add(groupLabel);

		JComboBox<Group> comboBox = new JComboBox<>(Group.getAllArray());
		comboBox.setMaximumRowCount(16);
		comboBox.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_3.add(comboBox);
		if (!isNew()) {
			comboBox.setSelectedItem(editee.getGroup());
		}

		JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4);

		JButton okButton = new JButton("确定");
		okButton.addActionListener(e -> {
			if (isNew()) {
				new Student(Integer.parseInt(codeField.getText()), nameField.getText(),
						maleOption.isSelected() ? Sex.MALE : Sex.FEMALE, (Group) comboBox.getSelectedItem());
			} else {
				editee.setName(nameField.getText());
				editee.setGroup((Group) comboBox.getSelectedItem());
				editee.setNumber(Integer.parseInt(codeField.getText()));
				editee.setSex(maleOption.isSelected() ? Sex.MALE : Sex.FEMALE);
			}
		});
		okButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_4.add(okButton);

		JButton cancelButton = new JButton("取消");
		cancelButton.setFont(new Font("Noto Sans SC", Font.PLAIN, 12));
		panel_4.add(cancelButton);
	}

	private boolean isNew() {
		return editee == null;
	}

}
