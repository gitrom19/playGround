package com.skidata.grro.java8lessons.lambdas;

import java.util.Iterator;
import java.util.function.Consumer;

public class IterImpl implements Iterable<String> {

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEach(Consumer<? super String> action) {
		// TODO Auto-generated method stub
		Iterable.super.forEach(action);
	}
}
