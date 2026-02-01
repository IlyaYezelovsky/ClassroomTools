package org.lokova.classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	private Student generate(Sex sex) {
		if (sex == Sex.MALE) {
			return generateFrom(Student.getBoys());
		}
		if (sex == Sex.FEMALE) {
			return generateFrom(Student.getGirls());
		}
		return generateFrom(Student.getAll());
	}

	private List<Student> generate(Sex sex, int count, boolean duplicate) {
		if (sex == Sex.MALE) {
			return generateFrom(Student.getBoys(), count, duplicate);
		}
		if (sex == Sex.FEMALE) {
			return generateFrom(Student.getGirls(), count, duplicate);
		}
		return generateFrom(Student.getAll(), count, duplicate);
	}

	private Student generateFrom(Set<Student> set) {
		Student[] array = set.toArray(new Student[0]);
		return array[inner.nextInt(array.length)];
	}

	private List<Student> generateFrom(Set<Student> set, int count, boolean duplicate) {
		List<Student> result = new ArrayList<>();
		if (duplicate) {
			for (int i = 0; i < count; i++) {
				result.add(generateFrom(set));
			}
		} else {
			Student candidate = null;
			while (result.size() < count) {
				if (!result.contains(candidate = generateFrom(set))) {
					result.add(candidate);
				}
			}
		}
		return result;
	}

	public Student next() {
		return generate(null);
	}

	public List<Student> next(int count) {
		return next(count, false);
	}

	public List<Student> next(int count, boolean duplicate) {
		return generate(null, count, duplicate);
	}

	public Student nextBoy() {
		return generate(Sex.MALE);
	}

	public List<Student> nextBoy(int count) {
		return nextBoy(count, false);
	}

	public List<Student> nextBoy(int count, boolean duplicate) {
		return generate(Sex.MALE, count, duplicate);
	}

	public Student nextGirl() {
		return generate(Sex.FEMALE);
	}

	public List<Student> nextGirl(int count) {
		return nextGirl(count, false);
	}

	public List<Student> nextGirl(int count, boolean duplicate) {
		return generate(Sex.FEMALE, count, duplicate);
	}

}