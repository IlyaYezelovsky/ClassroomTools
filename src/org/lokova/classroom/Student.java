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

	public static Set<Student> getBoys() {
		return Collections.unmodifiableSet(boys);
	}

	public static Set<Student> getGirls() {
		return Collections.unmodifiableSet(girls);
	}

	public static void load(File file) throws IOException, ClassNotFoundException {
		Objects.requireNonNull(file, "File cannot be null");
		try (var is = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = is.readObject();
			if (obj.getClass() == TreeSet.class) {
				var set = (TreeSet<?>) obj;
				if ((set.getFirst() instanceof Student)) {
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

	public static void save(File file) throws IOException {
		try (var os = new ObjectOutputStream(new FileOutputStream(file))) {
			os.writeObject(all);
		}
	}

	private int number;
	private String name;
	private Sex sex;
	private int score;
	private List<ScoreRecord> records = new ArrayList<>();

	public Student(int number, String name, Sex sex) {
		this.number = number;
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
		return Integer.compare(number, o.number);
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
		return Objects.equals(name, other.name) && (number == other.number) && (sex == other.sex);
	}

	private void fixSex() {
		if (sex == Sex.MALE) {
			boys.add(this);
		} else {
			girls.add(this);
		}
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public Sex getSex() {
		return sex;
	}

	public char getSexSymbol() {
		if (sex == Sex.MALE) {
			return '♂';
		}
		return '♀';
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, number, sex);
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

	public void setName(String name) {
		Objects.requireNonNull(name, "Name cannot be null");
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setSex(Sex sex) {
		Objects.requireNonNull(sex, "Sex must be MALE or FEMALE, not null");
		this.sex = sex;
	}

	@Override
	public String toString() {
		return number + getSexSymbol() + name;
	}

}