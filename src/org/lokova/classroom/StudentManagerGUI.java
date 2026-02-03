package org.lokova.classroom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class StudentManagerGUI extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			new StudentManagerGUI();
		});
	}

	private DefaultListModel<Student> studentListModel;
	private JList<Student> studentList;
	private DefaultListModel<ScoreRecord> recordListModel;
	private JList<ScoreRecord> recordList;
	private JTextArea studentInfoArea;
	private JButton addStudentButton, editStudentButton, deleteStudentButton;
	private JButton addRecordButton, deleteRecordButton;
	private JComboBox<Group> groupComboBox;
	private List<Group> groups;

	public StudentManagerGUI() {
		super("学生管理系统");
		initializeComponents();
		setupLayout();
		setupListeners();
//		loadSampleData();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addRecord() {
		Student selectedStudent = studentList.getSelectedValue();
		if (selectedStudent == null) {
			JOptionPane.showMessageDialog(this, "请先选择一个学生", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

		JTextField scoreField = new JTextField();
		JTextArea reasonArea = new JTextArea(3, 20);

		panel.add(new JLabel("分数："));
		panel.add(scoreField);
		panel.add(new JLabel("原因："));
		panel.add(new JScrollPane(reasonArea));

		int result = JOptionPane.showConfirmDialog(this, panel, "添加评分记录", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				int score = Integer.parseInt(scoreField.getText().trim());
				String reason = reasonArea.getText().trim();

				if (reason.isEmpty()) {
					JOptionPane.showMessageDialog(this, "原因不能为空", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (score == 0) {
					JOptionPane.showMessageDialog(this, "分数不能为0", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				selectedStudent.addRecord(score, reason, LocalDateTime.now());
				updateRecordList();
				updateStudentInfo();

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "分数必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "添加失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void addStudent() {
		JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

		JTextField codeField = new JTextField();
		JTextField nameField = new JTextField();
		JComboBox<Sex> sexCombo = new JComboBox<>(Sex.values());

		panel.add(new JLabel("学号："));
		panel.add(codeField);
		panel.add(new JLabel("姓名："));
		panel.add(nameField);
		panel.add(new JLabel("性别："));
		panel.add(sexCombo);
		panel.add(new JLabel("组别："));
		panel.add(groupComboBox);

		int result = JOptionPane.showConfirmDialog(this, panel, "添加学生", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				int code = Integer.parseInt(codeField.getText().trim());
				String name = nameField.getText().trim();
				Sex sex = (Sex) sexCombo.getSelectedItem();
				Group group = (Group) groupComboBox.getSelectedItem();

				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "姓名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Student student = new Student(code, name, sex);
				if (group != null) {
					student.setGroup(group);
					group.add(student);
				}

				studentListModel.addElement(student);
				studentList.setSelectedValue(student, true);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "学号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "添加失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void deleteRecord() {
		Student selectedStudent = studentList.getSelectedValue();
		ScoreRecord selectedRecord = recordList.getSelectedValue();

		if ((selectedStudent == null) || (selectedRecord == null)) {
			JOptionPane.showMessageDialog(this, "请先选择要删除的记录", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this,
				"确定要删除这条记录吗？\n分数: " + selectedRecord.getScore() + "\n原因: " + selectedRecord.getReason(), "确认删除",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			selectedStudent.deleteRecord(selectedRecord);
			updateRecordList();
			updateStudentInfo();
		}
	}

	private void deleteStudent() {
		Student selected = studentList.getSelectedValue();
		if (selected == null) {
			JOptionPane.showMessageDialog(this, "请先选择要删除的学生", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, "确定要删除学生 " + selected.getName() + " 吗？", "确认删除",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			// 从组中移除
			if (selected.getGroup() != null) {
				selected.getGroup().remove(selected);
			}

			// 从列表中移除
			Student.getAll().remove(selected);
			studentListModel.removeElement(selected);

			// 清除信息显示
			studentInfoArea.setText("");
			recordListModel.clear();
		}
	}

	private void editStudent() {
		Student selected = studentList.getSelectedValue();
		if (selected == null) {
			JOptionPane.showMessageDialog(this, "请先选择要修改的学生", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

		JTextField codeField = new JTextField(String.valueOf(selected.getNumber()));
		JTextField nameField = new JTextField(selected.getName());
		JComboBox<Sex> sexCombo = new JComboBox<>(Sex.values());
		sexCombo.setSelectedItem(selected.getSex());

		panel.add(new JLabel("学号:"));
		panel.add(codeField);
		panel.add(new JLabel("姓名:"));
		panel.add(nameField);
		panel.add(new JLabel("性别:"));
		panel.add(sexCombo);
		panel.add(new JLabel("组别:"));
		panel.add(groupComboBox);

		int result = JOptionPane.showConfirmDialog(this, panel, "修改学生信息", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				int code = Integer.parseInt(codeField.getText().trim());
				String name = nameField.getText().trim();
				Sex sex = (Sex) sexCombo.getSelectedItem();
				Group group = (Group) groupComboBox.getSelectedItem();

				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "姓名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// 更新学生信息
				selected.setNumber(code);
				selected.setName(name);
				selected.setSex(sex);
				selected.setGroup(group);

				// 如果选择了组，将学生添加到组
				if (group != null) {
					group.add(selected);
				}

				// 刷新显示
				updateStudentInfo();
				studentList.repaint();

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "学号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void initializeComponents() {
		studentListModel = new DefaultListModel<>();
		studentList = new JList<>(studentListModel);
		studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		recordListModel = new DefaultListModel<>();
		recordList = new JList<>(recordListModel);
		recordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		studentInfoArea = new JTextArea();
		studentInfoArea.setEditable(false);
		studentInfoArea.setFont(new Font("Noto Serif SC", Font.PLAIN, 14));
		studentInfoArea.setLineWrap(true);
		studentInfoArea.setWrapStyleWord(true);

		addStudentButton = new JButton("添加学生");
		editStudentButton = new JButton("修改学生");
		deleteStudentButton = new JButton("删除学生");

		addRecordButton = new JButton("添加记录");
		deleteRecordButton = new JButton("删除记录");

		// 创建一些示例组
		groups = new ArrayList<>();
//		groups.add(new Group("第一组", 1));
//		groups.add(new Group("第二组", 2));
//		groups.add(new Group("第三组", 3));

		groupComboBox = new JComboBox<>(groups.toArray(new Group[0]));
		groupComboBox.insertItemAt(null, 0); // 添加空选项
		groupComboBox.setSelectedIndex(0);
	}

	private void loadSampleData() {
		// 加载一些示例数据
		try {
			// 添加一些示例学生
			Student s1 = new Student(1001, "张三", Sex.MALE);
			Student s2 = new Student(1002, "李四", Sex.MALE);
			Student s3 = new Student(1003, "王五", Sex.FEMALE);
			Student s4 = new Student(1004, "赵六", Sex.FEMALE);

			// 添加示例记录
			s1.addRecord(10, "课堂表现优秀");
			s1.addRecord(-5, "迟到");
			s2.addRecord(15, "作业完成优秀");
			s3.addRecord(8, "小组活动积极");
			s4.addRecord(12, "回答问题正确");

			// 添加到组
			groups.get(0).add(s1);
			groups.get(0).add(s2);
			groups.get(1).add(s3);
			groups.get(2).add(s4);

			s1.setGroup(groups.get(0));
			s2.setGroup(groups.get(0));
			s3.setGroup(groups.get(1));
			s4.setGroup(groups.get(2));

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显示所有学生
		updateStudentsByGroup();
	}

	private void setupLayout() {
		// 主面板
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// 左侧学生列表面板
		JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
		leftPanel.setBorder(BorderFactory.createTitledBorder("学生列表"));
		leftPanel.add(new JScrollPane(studentList), BorderLayout.CENTER);

		JPanel leftButtonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		leftButtonPanel.add(addStudentButton);
		leftButtonPanel.add(editStudentButton);
		leftButtonPanel.add(deleteStudentButton);
		leftButtonPanel.add(new JLabel("选择组别:"));
		leftButtonPanel.add(groupComboBox);
		leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

		// 中间学生信息面板
		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		centerPanel.setBorder(BorderFactory.createTitledBorder("学生信息"));
		centerPanel.add(new JScrollPane(studentInfoArea), BorderLayout.CENTER);

		// 右侧记录列表面板
		JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
		rightPanel.setBorder(BorderFactory.createTitledBorder("评分记录"));
		rightPanel.add(new JScrollPane(recordList), BorderLayout.CENTER);

		JPanel rightButtonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		rightButtonPanel.add(addRecordButton);
		rightButtonPanel.add(deleteRecordButton);
		rightPanel.add(rightButtonPanel, BorderLayout.SOUTH);

		// 添加到主面板
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(rightPanel, BorderLayout.EAST);

		// 设置比例
		leftPanel.setPreferredSize(new Dimension(200, 0));
		rightPanel.setPreferredSize(new Dimension(300, 0));

		add(mainPanel);
	}

	private void setupListeners() {
		// 学生列表选择监听器
		studentList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				updateStudentInfo();
				updateRecordList();
			}
		});

		// 添加学生按钮
		addStudentButton.addActionListener(e -> addStudent());

		// 修改学生按钮
		editStudentButton.addActionListener(e -> editStudent());

		// 删除学生按钮
		deleteStudentButton.addActionListener(e -> deleteStudent());

		// 添加记录按钮
		addRecordButton.addActionListener(e -> addRecord());

		// 删除记录按钮
		deleteRecordButton.addActionListener(e -> deleteRecord());

		// 组别选择监听器
		groupComboBox.addActionListener(e -> updateStudentsByGroup());
	}

	private void updateRecordList() {
		recordListModel.clear();
		Student selected = studentList.getSelectedValue();
		if (selected != null) {
			for (ScoreRecord record : selected.getRecords()) {
				recordListModel.addElement(record);
			}
		}
	}

	private void updateStudentInfo() {
		Student selected = studentList.getSelectedValue();
		if (selected != null) {
			studentInfoArea.setText(selected.getInfo());
		} else {
			studentInfoArea.setText("");
		}
	}

	private void updateStudentsByGroup() {
		studentListModel.clear();
		Group selectedGroup = (Group) groupComboBox.getSelectedItem();

		if (selectedGroup == null) {
			// 显示所有学生
			for (Student student : Student.getAll()) {
				studentListModel.addElement(student);
			}
		} else {
			// 显示该组的学生
			// 注意：这里假设Group类有一个获取成员的方法
			// 根据你的Group类，可能需要修改这部分
			for (Student student : Student.getAll()) {
				if ((student.getGroup() != null) && student.getGroup().equals(selectedGroup)) {
					studentListModel.addElement(student);
				}
			}
		}

		if (studentListModel.size() > 0) {
			studentList.setSelectedIndex(0);
		}
	}

}