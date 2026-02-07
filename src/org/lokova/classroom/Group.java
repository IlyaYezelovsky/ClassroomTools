package org.lokova.classroom;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Group implements Serializable, Comparable<Group> {

	private static final long serialVersionUID = 1L;
	private static Set<Group> all = new TreeSet<>();
	private static Set<String> names = new HashSet<>();

	public static Set<Group> getAll() {
		return Collections.unmodifiableSet(all);
	}

	public static Group[] getAllArray() {
		return all.toArray(new Group[0]);
	}

	private String name;
	private Set<Student> members;

	public Group(String name) {
		members = new TreeSet<>();
		if (!names.add(name)) {
			throw new IllegalStateException("Cannot create a group with a name that already exists");
		}
	}

	public boolean add(Student s) {
		return members.add(s);
	}

	public void clear() {
		members.clear();
	}

	@Override
	public int compareTo(Group o) {
		return name.compareTo(o.name);
	}

	public boolean contains(Student s) {
		return members.contains(s);
	}

	public void delete() {
		all.remove(this);
		names.remove(name);
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
		return Objects.equals(name, other.name);
	}

	public String getInfo() {
		var sb = new StringBuilder(name + "\n");
		for (Student s : members) {
			sb.append(s + "\n");
		}
		sb.append("Hashcode: " + hashCode());
		return sb.toString();
	}

	public Set<Student> getMembers() {
		return Collections.unmodifiableSet(members);
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public int memberCount() {
		return members.size();
	}

	public boolean remove(Student s) {
		return members.remove(s);
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}