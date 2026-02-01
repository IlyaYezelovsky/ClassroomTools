package org.lokova.classroom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MainUI implements UI {

	public static void main(String[] args) {
		new MainUI().go();
	}

	private JFrame frame;
	private JPanel panel;

	@Override
	public void go() {
		frame = new JFrame("ClassroomTools 260201");
		panel = new JPanel();

		JButton studentButton = new JButton("学生管理");
		studentButton.addActionListener(a -> {
			// TODO
		});
		JPanel studentPanel = new JPanel();
		studentPanel.add(studentButton);

		JButton extractButton = new JButton("随机抽取");
		extractButton.addActionListener(a -> {
			// TODO
		});
		JPanel extractPanel = new JPanel();
		extractPanel.add(extractButton);

		JButton seatButton = new JButton("一键排座位");
		seatButton.addActionListener(a -> {
			// TODO
		});
		JPanel seatPanel = new JPanel();
		seatPanel.add(seatButton);

		JButton aboutButton = new JButton("关于");
		aboutButton.addActionListener(a -> {
			JFrame msgbox = new JFrame("关于");
			JPanel mPanel = new JPanel();
			JButton ok = new JButton("OK");
			ok.addActionListener(a1 -> {
				msgbox.dispose();
			});
			mPanel.setLayout(new BoxLayout(mPanel, BoxLayout.Y_AXIS));
			JPanel panel1 = new JPanel();
			JPanel panel2 = new JPanel();
			JPanel panel3 = new JPanel();
			JPanel panel4 = new JPanel();
			panel1.add(new JLabel("Class 15 Utilities"));
			panel2.add(new JLabel("v2.1.7 on 2025-10-24"));
			panel3.add(new JLabel("by IlyaYezelovsky"));
			panel4.add(ok);
			mPanel.add(panel1);
			mPanel.add(panel2);
			mPanel.add(panel3);
			mPanel.add(panel4);
			msgbox.getContentPane().add(mPanel);
			msgbox.setSize(210, 150);
			msgbox.setVisible(true);
		});
		JPanel aboutPanel = new JPanel();
		aboutPanel.add(aboutButton);

		JButton exitButton = new JButton("退出程序");
		exitButton.addActionListener(a -> {
			stop();
		});
		JPanel exitPanel = new JPanel();
		exitPanel.add(exitButton);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(studentPanel);
		panel.add(extractPanel);
		panel.add(seatPanel);
		panel.add(aboutPanel);
		panel.add(exitPanel);

		frame.setContentPane(panel);
		frame.setSize(320, 250);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void stop() {
		System.exit(0);
	}

}
