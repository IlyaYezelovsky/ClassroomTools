package org.lokova.classroom;

import java.time.LocalDateTime;

public class QuickRecord {

	private int score;
	private String reason;

	public QuickRecord(int score, String reason) {
		this.score = score;
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public int getScore() {
		return score;
	}

	public ScoreRecord rekord(Student s) {
		return rekord(s, LocalDateTime.now());
	}

	public ScoreRecord rekord(Student s, LocalDateTime time) {
		return new ScoreRecord(s, score, reason, time);
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
