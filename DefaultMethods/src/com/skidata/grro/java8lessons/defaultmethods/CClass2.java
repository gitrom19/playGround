package com.skidata.grro.java8lessons.defaultmethods;

public class CClass2 extends CClass1 implements IIf1{
	
	@Override
	public void foo() {
		System.out.println(CClass2.class.getName() + ".foo");
	}

}
