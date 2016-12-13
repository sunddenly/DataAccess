package org.hebut.scse.hibernate.pojo.n2n.oneway;

import java.util.HashSet;
import java.util.Set;

public class Item {

	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}

	public Item() {
	}

	public Item(String name) {
		this.name = name;
	}
}