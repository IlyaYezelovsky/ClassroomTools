package org.lokova.classroom;

import java.time.LocalDateTime;
import java.util.Objects;

public record ScoreRecord(Student owner, int score, String reason, LocalDateTime time) {

	public ScoreRecord {
		Objects.requireNonNull(owner, "Student cannot be null");
		if (score == 0) {
			throw new IllegalArgumentException("Score cannot be 0");
		}
		Objects.requireNonNull(reason, "Reason must be provided");
		Objects.requireNonNull(time, "Time cannot be null");
	}

}