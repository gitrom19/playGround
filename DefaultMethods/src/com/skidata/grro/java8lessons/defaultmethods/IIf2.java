package com.skidata.grro.java8lessons.defaultmethods;

public interface IIf2 extends IIf1{
	default public void foo () {
		System.out.println(IIf2.class.getName() + ".foo");
	}

}
