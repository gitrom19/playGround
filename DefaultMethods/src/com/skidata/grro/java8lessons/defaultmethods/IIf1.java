package com.skidata.grro.java8lessons.defaultmethods;

public interface IIf1 {
	default public void foo () {
		System.out.println(IIf1.class.getName() + ".foo");
	}

}
