package org.lokova.classroom;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Utils {

	public static final File currentFolder = new File(".");

	public static void copy(String text) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(text);
		clipboard.setContents(selection, null);
	}

	public static String getShortTime() {
		return getShortTime(ZonedDateTime.now(ZoneId.of("Asia/Irkutsk")));
	}

	public static String getShortTime(ZonedDateTime time) {
		StringBuilder sb = new StringBuilder(Utils.getTime(time));
		sb.delete(10, 16);
		return sb.toString();
	}

	public static String getTime() {
		return getTime(ZonedDateTime.now(ZoneId.of("Asia/Irkutsk")));
	}

	public static String getTime(ZonedDateTime time) {
		String dayOfWeek = time.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
		return String.format("%d-%02d-%02d (%s) %02d:%02d:%02d", time.getYear(), time.getMonthValue(),
				time.getDayOfMonth(), dayOfWeek, time.getHour(), time.getMinute(), time.getSecond());
	}

	public static boolean isInteger(String s) {
		return s.matches("-?\\d+");
	}

	public static String pm(int x) {
		if (x == 0) {
			return "0";
		} else if (x > 0) {
			return "+" + x;
		} else {
			return Integer.toString(x);
		}
	}

	public static void showErrorMsgbox(Throwable e) {
		showErrorMsgbox(withSuppressed(e));
	}

	public static void showErrorMsgbox(Throwable[] e) {
		JFrame frame = new JFrame("错误");
		JPanel panel = new JPanel();

		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		for (Throwable t : e) {
			t.printStackTrace(writer);
		}

		JTextArea text = new JTextArea(22, 40);
		text.setEditable(false);
		text.setText(sw.toString());
		JScrollPane scroller = new JScrollPane(text);
		scroller.setAutoscrolls(true);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		JPanel up = new JPanel();
		up.add(scroller);

		JButton copyButton = new JButton("复制错误信息");
		copyButton.addActionListener(a -> {
			copy(text.getText());
		});
		JButton ok = new JButton("OK");
		ok.addActionListener(a -> {
			frame.dispose();
		});

		JPanel down = new JPanel();
		down.add(copyButton);
		down.add(ok);

		panel.add(up);
		panel.add(down);

		frame.setContentPane(panel);
		frame.setSize(500, 510);
		frame.setVisible(true);
	}

	public static void showMsgbox(String msg, String title) {
		showMsgbox(msg, title, 100, 100);
	}

	public static void showMsgbox(String msg, String title, int width, int height) {
		JFrame msgbox = new JFrame(title);
		JPanel mPanel = new JPanel();
		JButton ok = new JButton("OK");
		ok.addActionListener(a1 -> {
			msgbox.dispose();
		});
		mPanel.add(new JLabel(msg));
		mPanel.add(ok);
		msgbox.getContentPane().add(mPanel);
		msgbox.setSize(width, height);
		msgbox.setVisible(true);
	}

	public static Throwable[] withSuppressed(Throwable e) {
		ArrayList<Throwable> all = new ArrayList<>();
		all.add(e);
		Collections.addAll(all, e.getSuppressed());
		return all.toArray(new Throwable[e.getSuppressed().length + 1]);
	}

	private Utils() {
		throw new AssertionError("Utils is an tool class; cannot be instantiated");
	}

}