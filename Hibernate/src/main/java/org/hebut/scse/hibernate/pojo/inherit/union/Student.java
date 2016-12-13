package org.hebut.scse.hibernate.pojo.inherit.union;

public class Student extends Person {

	private String school;

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

    public Student() {
    }

    public Student(int age,String name, String school) {
		super(age,name);
		this.school = school;
	}



	@Override
	public String toString() {
		return "Student{" +
				"school='" + school + '\'' +
				'}';
	}
}