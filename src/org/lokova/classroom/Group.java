package org.lokova.classroom;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Group implements Serializable, Comparable<Group> {

	private static final long serialVersionUID = 1L;
	private String name;
	private int code;
	private Set<Student> members;

	public Group(String name, int code) {
		this.name = name;
		this.code = code;
		members = new TreeSet<>();
	}

	public boolean add(Student s) {
		return members.add(s);
	}

	public void clear() {
		members.clear();
	}

	@Override
	public int compareTo(Group o) {
		return Integer.compare(code, o.code);
	}

	public boolean contains(Student s) {
		return members.contains(s);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Group other = (Group) obj;
		return (code == other.code) && Objects.equals(members, other.members) && Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, members, name);
	}

	public int memberCount() {
		return members.size();
	}

	public boolean remove(Student s) {
		return members.remove(s);
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", code=" + code + "]";
	}

}