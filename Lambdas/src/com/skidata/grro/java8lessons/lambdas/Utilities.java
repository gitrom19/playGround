package com.skidata.grro.java8lessons.lambdas;

import java.util.Iterator;
import java.util.function.Consumer;

public class Utilities {

	public static <E> void forEach(Iterable<E> seq, Consumer<E> block) {
		Iterator<E> iter = seq.iterator();
		while(iter.hasNext()) {
			E elem = iter.next();
			block.accept(elem);
		}
	}
	
	public static void stringOut (String s) {
		System.out.println(s);
	}
	
	public static void intOut(Integer i) {
		System.out.println(i);
	}
}
