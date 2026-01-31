package org.lokova.classroom;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Student implements Serializable, Comparable<Student> {

	private static final long serialVersionUID = 1L;
	private static List<Student> all = new ArrayList<>();
	private static List<Student> boys = new ArrayList<>();
	private static List<Student> girls = new ArrayList<>();

	public static List<Student> getAll() {
		return Collections.unmodifiableList(all);
	}

	public static List<Student> getBoys() {
		return Collections.unmodifiableList(boys);
	}

	public static List<Student> getGirls() {
		return Collections.unmodifiableList(girls);
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

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public Sex getSex() {
		return sex;
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
		Objects.requireNonNull(sex, "Sex cannot be null");
		this.sex = sex;
	}

}