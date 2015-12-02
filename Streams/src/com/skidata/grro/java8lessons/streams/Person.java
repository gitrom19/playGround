package com.skidata.grro.java8lessons.streams;

public class Person {

	private final int tIN;
	private final String name;
	private final String address;
	
	public Person (int tIN, String name, String address) {
		this.tIN = tIN;
		this.name = name;
		this.address = address;
	}

	public int gettIN() {
		return tIN;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(tIN);
		sb.append(":");
		sb.append(name);
		sb.append(":");
		sb.append(address);
		sb.append(">");
		return sb.toString();
	}
}
