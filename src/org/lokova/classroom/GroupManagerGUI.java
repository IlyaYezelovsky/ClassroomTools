package org.lokova.classroom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
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

public class GroupManagerGUI extends JFrame {

	// 自定义列表渲染器 - 用于美化Group显示
	class GroupListRenderer extends DefaultListCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Group group) {
				// 使用Group的memberCount方法获取成员数量
				int memberCount = group.memberCount();

				// 从toString中提取组名
				String displayText = group.toString();
				setText(displayText + " (" + memberCount + "人)");
				setFont(new Font("Noto Serif SC", Font.BOLD, 12));

				if (memberCount == 0) {
					setForeground(Color.BLACK);
				}
			}

			return this;
		}

	}

	// 自定义列表渲染器 - 用于美化Student显示
	class StudentListRenderer extends DefaultListCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value instanceof Student student) {
				setText(student.toString());
				setFont(new Font("Noto Serif SC", Font.PLAIN, 12));

				// 根据性别设置不同的图标或颜色
				if (student.getSex() == Sex.MALE) {
					setForeground(new Color(0, 100, 200)); // 蓝色
				} else {
					setForeground(new Color(200, 0, 100)); // 粉色
				}
			}

			return this;
		}

	}

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
			new GroupManagerGUI();
		});
	}

	private DefaultListModel<Group> groupListModel;
	private JList<Group> groupList;
	private DefaultListModel<Student> memberListModel;
	private JList<Student> memberList;
	private JTextArea groupInfoArea;
	private JButton addGroupButton, editGroupButton, deleteGroupButton;
	private JButton addMemberButton, removeMemberButton;
	private JComboBox<Student> studentComboBox;
	private List<Group> allGroups;

	public GroupManagerGUI() {
		super("组管理系统");
		initializeComponents();
		setupLayout();
		setupListeners();
		loadGroups();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addGroup() {
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

		JTextField nameField = new JTextField();
		JTextField codeField = new JTextField();

		panel.add(new JLabel("组名："));
		panel.add(nameField);
		panel.add(new JLabel("组号："));
		panel.add(codeField);

		int result = JOptionPane.showConfirmDialog(this, panel, "添加新组", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				String name = nameField.getText().trim();
				int code = Integer.parseInt(codeField.getText().trim());

				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "组名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// 检查组号是否重复
				for (Group group : allGroups) {
					if (group.compareTo(new Group("", code)) == 0) {
						JOptionPane.showMessageDialog(this, "组号已存在", "错误", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				Group newGroup = new Group(name, code);
				allGroups.add(newGroup);
				refreshGroupList();
				groupList.setSelectedValue(newGroup, true);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "组号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "添加失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void addMember() {
		Group selectedGroup = groupList.getSelectedValue();
		Student selectedStudent = (Student) studentComboBox.getSelectedItem();

		if (selectedGroup == null) {
			JOptionPane.showMessageDialog(this, "请先选择一个组", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (selectedStudent == null) {
			JOptionPane.showMessageDialog(this, "没有可添加的学生", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// 检查学生是否已经在其他组中
		if (selectedStudent.getGroup() != null) {
			int confirm = JOptionPane.showConfirmDialog(this,
					"该学生已经在组" + selectedStudent.getGroup().toString() + " 中，\n是否要转移到当前组？", "确认转移",
					JOptionPane.YES_NO_OPTION);

			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
		}

		// 添加学生到组
		selectedGroup.add(selectedStudent);
		selectedStudent.setGroup(selectedGroup);

		// 更新显示
		updateGroupInfo();
		updateMemberList();
		updateStudentComboBox();

		JOptionPane.showMessageDialog(this, "已添加学生到组", "成功", JOptionPane.INFORMATION_MESSAGE);
	}

	private void deleteGroup() {
		Group selected = groupList.getSelectedValue();
		if (selected == null) {
			JOptionPane.showMessageDialog(this, "请先选择要删除的组", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// 检查组是否为空
		int memberCount = 0;
		for (Student student : Student.getAll()) {
			if ((student.getGroup() != null) && student.getGroup().equals(selected)) {
				memberCount++;
			}
		}

		if (memberCount > 0) {
			int confirm = JOptionPane.showConfirmDialog(this,
					"该组还有" + memberCount + "个成员，删除后这些成员将变为未分组状态。\n确定要删除组 " + selected.toString() + " 吗？", "确认删除",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			// 移除所有成员的组关联
			for (Student student : Student.getAll()) {
				if ((student.getGroup() != null) && student.getGroup().equals(selected)) {
					student.setGroup(null);
				}
			}
		}

		allGroups.remove(selected);
		refreshGroupList();

		// 清除显示
		groupInfoArea.setText("");
		memberListModel.clear();
	}

	private void editGroup() {
		Group selected = groupList.getSelectedValue();
		if (selected == null) {
			JOptionPane.showMessageDialog(this, "请先选择要修改的组", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

		String groupStr = selected.toString();
		String currentName = "";
		String currentCode = "";

		try {
			// 解析toString获取组名和组号
			if (groupStr.contains("name=") && groupStr.contains("code=")) {
				int nameStart = groupStr.indexOf("name=") + 5;
				int nameEnd = groupStr.indexOf(", code=");
				int codeStart = groupStr.indexOf("code=") + 5;
				int codeEnd = groupStr.indexOf("]", codeStart);

				currentName = groupStr.substring(nameStart, nameEnd).trim();
				currentCode = groupStr.substring(codeStart, codeEnd).trim();
			}
		} catch (Exception e) {
			// 如果解析失败，使用默认值
			currentName = "未命名";
			currentCode = "0";
		}

		JTextField nameField = new JTextField(currentName);
		JTextField codeField = new JTextField(currentCode);

		panel.add(new JLabel("组名:"));
		panel.add(nameField);
		panel.add(new JLabel("组号:"));
		panel.add(codeField);

		int result = JOptionPane.showConfirmDialog(this, panel, "修改组信息", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				String name = nameField.getText().trim();
				int code = Integer.parseInt(codeField.getText().trim());

				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "组名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// 检查组号是否重复（除了当前组）
				for (Group group : allGroups) {
					if ((group != selected) && (group.compareTo(new Group("", code)) == 0)) {
						JOptionPane.showMessageDialog(this, "组号已存在", "错误", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				// 由于Group类是不可变的，我们需要创建新组并转移成员
				Group newGroup = new Group(name, code);

				// 获取当前组的所有成员
				Set<Student> members = selected.getMembers();

				// 转移所有成员 - 使用Group的add方法
				for (Student student : members) {
					// 添加到新组
					newGroup.add(student);
					// 更新学生的组引用
					student.setGroup(newGroup);
				}

				// 替换组
				int index = allGroups.indexOf(selected);
				allGroups.set(index, newGroup);

				refreshGroupList();
				groupList.setSelectedValue(newGroup, true);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "组号必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "修改失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void initializeComponents() {
		groupListModel = new DefaultListModel<>();
		groupList = new JList<>(groupListModel);
		groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupList.setCellRenderer(new GroupListRenderer());

		memberListModel = new DefaultListModel<>();
		memberList = new JList<>(memberListModel);
		memberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		memberList.setCellRenderer(new StudentListRenderer());

		groupInfoArea = new JTextArea();
		groupInfoArea.setEditable(false);
		groupInfoArea.setFont(new Font("宋体", Font.PLAIN, 14));
		groupInfoArea.setLineWrap(true);
		groupInfoArea.setWrapStyleWord(true);

		addGroupButton = new JButton("添加组");
		editGroupButton = new JButton("修改组");
		deleteGroupButton = new JButton("删除组");

		addMemberButton = new JButton("添加成员");
		removeMemberButton = new JButton("移除成员");

		// 获取所有学生
		studentComboBox = new JComboBox<>();
		updateStudentComboBox();
	}

	private void loadGroups() {
		// 这里应该从全局数据加载组
		// 为了简单起见，我们使用一个静态列表来存储组
		allGroups = new ArrayList<>();

		// 从现有学生中收集所有组
		for (Student student : Student.getAll()) {
			if ((student.getGroup() != null) && !allGroups.contains(student.getGroup())) {
				allGroups.add(student.getGroup());
			}
		}

		// 添加一些额外的组
		// if (allGroups.isEmpty()) {
		// allGroups.add(new Group("第一组", 1));
		// allGroups.add(new Group("第二组", 2));
		// allGroups.add(new Group("第三组", 3));
		// }

		// 更新列表
		refreshGroupList();
	}

	private void refreshGroupList() {
		groupListModel.clear();
		for (Group group : allGroups) {
			groupListModel.addElement(group);
		}

		if (groupListModel.size() > 0) {
			groupList.setSelectedIndex(0);
		}
	}

	private void removeMember() {
		Group selectedGroup = groupList.getSelectedValue();
		Student selectedStudent = memberList.getSelectedValue();

		if (selectedGroup == null) {
			JOptionPane.showMessageDialog(this, "请先选择一个组", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (selectedStudent == null) {
			JOptionPane.showMessageDialog(this, "请先选择要移除的成员", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, "确定要将学生 " + selectedStudent.getName() + " 从组中移除吗？", "确认移除",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			selectedGroup.remove(selectedStudent);
			selectedStudent.setGroup(null);

			// 更新显示
			updateGroupInfo();
			updateMemberList();
			updateStudentComboBox();
		}
	}

	private void setupLayout() {
		// 主面板
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// 左侧组列表面板
		JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
		leftPanel.setBorder(BorderFactory.createTitledBorder("组列表"));
		leftPanel.add(new JScrollPane(groupList), BorderLayout.CENTER);

		JPanel leftButtonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		leftButtonPanel.add(addGroupButton);
		leftButtonPanel.add(editGroupButton);
		leftButtonPanel.add(deleteGroupButton);
		leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

		// 中间组信息面板
		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		centerPanel.setBorder(BorderFactory.createTitledBorder("组信息"));
		centerPanel.add(new JScrollPane(groupInfoArea), BorderLayout.CENTER);

		// 右侧成员列表面板
		JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
		rightPanel.setBorder(BorderFactory.createTitledBorder("组成员"));
		rightPanel.add(new JScrollPane(memberList), BorderLayout.CENTER);

		JPanel rightTopPanel = new JPanel(new BorderLayout(5, 5));
		rightTopPanel.add(new JLabel("添加成员:"), BorderLayout.WEST);
		rightTopPanel.add(studentComboBox, BorderLayout.CENTER);
		rightTopPanel.add(addMemberButton, BorderLayout.EAST);

		JPanel rightButtonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		rightButtonPanel.add(rightTopPanel);
		rightButtonPanel.add(removeMemberButton);

		rightPanel.add(rightButtonPanel, BorderLayout.NORTH);

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
		// 组列表选择监听器
		groupList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				updateGroupInfo();
				updateMemberList();
				updateStudentComboBox();
			}
		});

		// 添加组按钮
		addGroupButton.addActionListener(e -> addGroup());

		// 修改组按钮
		editGroupButton.addActionListener(e -> editGroup());

		// 删除组按钮
		deleteGroupButton.addActionListener(e -> deleteGroup());

		// 添加成员按钮
		addMemberButton.addActionListener(e -> addMember());

		// 移除成员按钮
		removeMemberButton.addActionListener(e -> removeMember());
	}

	private void updateGroupInfo() {
		Group selected = groupList.getSelectedValue();
		if (selected != null) {
			String info = "组名: " + selected.toString() + "\n\n";
			info += "成员数量: " + selected.memberCount() + "\n\n";
			info += "成员列表:\n";

			// 使用Group的getMembers方法获取成员信息
			// 注意：Group类的getMembers方法返回的是Set<Student>
			int count = 0;
			for (Student student : selected.getMembers()) {
				count++;
				info += count + ". " + student.toString() + "\n";
			}

			groupInfoArea.setText(info);
		} else {
			groupInfoArea.setText("");
		}
	}

	private void updateMemberList() {
		memberListModel.clear();
		Group selected = groupList.getSelectedValue();
		if (selected != null) {
			// 使用Group的getMembers方法获取成员
			for (Student student : selected.getMembers()) {
				memberListModel.addElement(student);
			}
		}
	}

	private void updateStudentComboBox() {
		studentComboBox.removeAllItems();
		Group selectedGroup = groupList.getSelectedValue();

		// 添加所有未分组的学生
		for (Student student : Student.getAll()) {
			if (student.getGroup() == null) {
				studentComboBox.addItem(student);
			}
		}

		// 添加"无学生"选项
		if (studentComboBox.getItemCount() == 0) {
			studentComboBox.addItem(null);
		}
	}

}