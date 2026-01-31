package org.lokova.classroom;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Student implements Serializable, Comparable<Student> {

	private static final long serialVersionUID = 1L;
	private static Map<Integer, Student> map = new TreeMap<>();
	private int number;
	private String name;
	private Sex sex;
	private int score;
	private List<ScoreRecord> records = new ArrayList<>();

	public Student(int number, String name, Sex sex) {
		this.number = number;
		this.name = name;
		this.sex = sex;
		map.put(number, this);
	}

	@Override
	public int compareTo(Student o) {
		return Integer.compare(number, o.number);
	}

	public boolean deleteRecord(ScoreRecord rekord) {
		return records.remove(rekord);
	}

	public String getName() {
		return name;
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
		this.name = name;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

}