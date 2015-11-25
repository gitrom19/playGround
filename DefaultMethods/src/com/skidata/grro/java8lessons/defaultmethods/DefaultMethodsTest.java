package com.skidata.grro.java8lessons.defaultmethods;

import java.util.function.Consumer;

public class DefaultMethodsTest {
	
	public static void main(String[] args) {
		CClass1 c1 = new CClass1();
		c1.foo();
		
		CClass2 c2 = new CClass2();
		c2.foo();
		
	    Consumer<String> c = (x) -> System.out.println(x.toLowerCase());
	    c.andThen(c).accept("Java2s.com");
		
	    Consumer<String> ci = new Consumer<String>() {

			@Override
			public void accept(String t) {
				System.out.println("<"+t+">");
				
			}
		};
		
		Consumer<String> ci2 = (x) -> System.out.println("!!!"+x+"!!!");
		
		ci.andThen(ci2).accept("Roman");
	}

}
