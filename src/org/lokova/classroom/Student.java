package org.lokova.classroom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Student implements Serializable, Comparable<Student> {

	private static final long serialVersionUID = 1L;
	private static Set<Student> all = new TreeSet<>();
	private static Set<Student> boys = new TreeSet<>();
	private static Set<Student> girls = new TreeSet<>();

	public static Set<Student> getAll() {
		return Collections.unmodifiableSet(all);
	}

	public static Student[] getAllArray() {
		return all.toArray(new Student[0]);
	}

	public static Set<Student> getBoys() {
		return Collections.unmodifiableSet(boys);
	}

	public static Student[] getBoysArray() {
		return boys.toArray(new Student[0]);
	}

	public static Set<Student> getGirls() {
		return Collections.unmodifiableSet(girls);
	}

	public static Student[] getGirlsArray() {
		return girls.toArray(new Student[0]);
	}

	public static void load() throws IOException, ClassNotFoundException {
		load(new File(Config.data().getDataPath()));
	}

	@SuppressWarnings("unchecked") // safe cast
	public static void load(File file) throws IOException, ClassNotFoundException {
		Objects.requireNonNull(file, "File cannot be null");
		try (var is = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = is.readObject();
			if (obj.getClass() == TreeSet.class) {
				var set = (TreeSet<?>) obj;
				if ((set.getFirst() instanceof Student)) { // checked here
					all = (TreeSet<Student>) set;
					return;
				} else {
					throw new IOException("A TreeSet<Student> is expected but a TreeSet<"
							+ set.getFirst().getClass().getName() + "> was found");
				}
			}
			throw new IOException("A TreeSet<Student> is expected but a " + obj.getClass().getName() + " was found");
		}
	}

	public static void save() throws IOException {
		save(new File(Config.data().getDataPath()));
	}

	public static void save(File file) throws IOException {
		try (var os = new ObjectOutputStream(new FileOutputStream(file))) {
			os.writeObject(all);
		}
	}

	private int code;
	private String name;
	private Sex sex;
	private int score;
	private List<ScoreRecord> records = new ArrayList<>();
	private Group group;

	public Student(int code, String name, Sex sex) {
		this.code = code;
		this.name = name;
		this.sex = sex;
		all.add(this);
		if (sex == Sex.MALE) {
			boys.add(this);
		}
		if (sex == Sex.FEMALE) {
			girls.add(this);
		}
	}

	@Override
	public int compareTo(Student o) {
		Objects.requireNonNull(o, "Student cannot be null");
		return Integer.compare(code, o.code);
	}

	public boolean deleteRecord(ScoreRecord rekord) {
		return records.remove(rekord);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Student other = (Student) obj;
		return Objects.equals(name, other.name) && (code == other.code) && (sex == other.sex);
	}

	private void fixSex() {
		if (sex == Sex.MALE) {
			boys.add(this);
		} else {
			girls.add(this);
		}
	}

	public Group getGroup() {
		return group;
	}

	public String getInfo() {
		return """
				学号：%s
				姓名：%s
				性别：%s
				组别：%s
				分数：%s
				""".formatted(code, name, getSexCharacter(), group, getScore());
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return code;
	}

	public List<ScoreRecord> getRecords() {
		return Collections.unmodifiableList(records);
	}

	public ScoreRecord[] getRecordsArray() {
		return records.toArray(new ScoreRecord[0]);
	}

	public int getScore() {
		int total = 0;
		for (ScoreRecord rekord : records) {
			total += rekord.getScore();
		}
		return total;
	}

	public Sex getSex() {
		return sex;
	}

	public char getSexCharacter() {
		return sex == Sex.MALE ? '男' : '女';
	}

	public char getSexSymbol() {
		return sex == Sex.MALE ? '♂' : '♀';
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, code, sex);
	}

	public void modifyScore(int score, String reason) {
		modifyScore(score, reason, LocalDateTime.now());
	}

	public void modifyScore(int score, String reason, LocalDateTime time) {
		Objects.requireNonNull(reason, "Reason must be provided");
		Objects.requireNonNull(time, "Time cannot be null");
		if (score == 0) {
			throw new IllegalArgumentException("Score cannot be 0");
		}
		ScoreRecord rekord = new ScoreRecord(this, score, reason, time);
		records.add(rekord);
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setName(String name) {
		Objects.requireNonNull(name, "Name cannot be null");
		this.name = name;
	}

	public void setNumber(int number) {
		code = number;
	}

	public void setSex(Sex sex) {
		Objects.requireNonNull(sex, "Sex must be MALE or FEMALE, not null");
		this.sex = sex;
	}

	@Override
	public String toString() {
		return code + getSexSymbol() + name;
	}

}