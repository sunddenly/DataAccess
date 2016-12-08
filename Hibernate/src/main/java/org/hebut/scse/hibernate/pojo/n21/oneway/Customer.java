package org.hebut.scse.hibernate.pojo.n21.oneway;

public class Customer {

	private Integer customerId;
	private String customerName;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Customer(String customerName) {
		this.customerName = customerName;
	}

	public Customer() {
	}
}