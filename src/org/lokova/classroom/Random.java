package org.lokova.classroom;

import java.util.List;

public class Random {

	private java.util.Random inner;

	public Random() {
		inner = new java.util.Random();
	}

	public Random(java.util.Random randomizer) {
		inner = randomizer;
	}

	public Random(long seed) {
		inner = new java.util.Random(seed);
	}

	private Student generateFrom(List<Student> list) {
		return list.get(inner.nextInt(list.size()));
	}

	public Student next(Sex sex) {
		if (sex == Sex.MALE) {
			return generateFrom(Student.getBoys());
		}
		if (sex == Sex.FEMALE) {
			return generateFrom(Student.getGirls());
		}
		return generateFrom(Student.getAll());
	}

	public Student nextBoy() {
		return next(Sex.MALE);
	}

	public Student nextGirl() {
		return next(Sex.FEMALE);
	}

}