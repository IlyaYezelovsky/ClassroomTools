package org.lokova.classroom;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {

	private static final long serialVersionUID = 1L;

	public static String pm(int n) {
		if (n == 0) {
			return String.valueOf(n);
		}
		if (n > 0) {
			return "+" + n;
		} else {
			return "-" + n;
		}
	}

	private UUID uuid;
	private Student owner;
	private int score;
	private String reason;
	private LocalDateTime time;

	public ScoreRecord(Student owner, int score, String reason) {
		this(owner, score, reason, LocalDateTime.now());
	}

	public ScoreRecord(Student owner, int score, String reason, LocalDateTime time) {
		Objects.requireNonNull(owner, "Student cannot be null");
		Objects.requireNonNull(reason, "Reason must be provided");
		Objects.requireNonNull(time, "Time cannot be null");
		if (score == 0) {
			throw new IllegalArgumentException("Score cannot be 0");
		}
		uuid = UUID.randomUUID();
		this.owner = owner;
		this.score = score;
		this.reason = reason;
		this.time = time;
	}

	public ScoreRecord(Student owner, QuickRecord qr) {
		this(owner, qr, LocalDateTime.now());
	}

	public ScoreRecord(Student owner, QuickRecord qr, LocalDateTime time) {
		this(owner, qr.getScore(), qr.getReason(), time);
	}

	@Override
	public int compareTo(ScoreRecord o) {
		return uuid.compareTo(o.uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		ScoreRecord other = (ScoreRecord) obj;
		return Objects.equals(uuid, other.uuid);
	}

	public String getInfo() {
		return time + "：" + pm(score) + "（" + reason + "）";
	}

	public Student getOwner() {
		return owner;
	}

	public String getReason() {
		return reason;
	}

	public int getScore() {
		return score;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public String toString() {
		return getInfo();
	}

}