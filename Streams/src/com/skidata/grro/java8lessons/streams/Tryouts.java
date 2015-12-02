package com.skidata.grro.java8lessons.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Tryouts {

	private static List<String> makeWords(String filename) {
		List<String> words = null;
		try {
			words = Arrays.asList(new String(Files.readAllBytes(Paths.get(filename))).split("[^\\p{L}]"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}

	private static void test(String filename) {
		List<String> words = makeWords(filename);
		words.parallelStream().filter(s -> s.length() > 0).map(String::toUpperCase) // intermediate
																					// operations
				.forEach(s -> System.out.println(Thread.currentThread().getName() + ": " + s)); // final
																								// operation
	}

	private static void test2(String filename) {
		List<String> words = makeWords(filename);
		String giantStream = words.parallelStream().filter(s -> s.length() > 0).map(String::toUpperCase).reduce("",
				(s1, s2) -> s1 + " " + s2);
		System.out.println(giantStream);
	}

	private static void multipleFor() {
		int dimX = 4;
		int dimY = 4;
		int dimZ = 4;
		Random random = new Random();

		List<List<List<Integer>>> cubicA = new ArrayList<>();

		for (int z = 0; z < dimZ; z++) {
			List<List<Integer>> yList = new ArrayList<>();
			for (int y = 0; y < dimY; y++) {
				List<Integer> xList = new ArrayList<>();
				for (int x = 0; x < dimX; x++) {
					xList.add(random.nextInt());
				}
				yList.add(xList);
			}
			cubicA.add(yList);
		}

		for (List<List<Integer>> yList : cubicA) {
			for (List<Integer> xList : yList) {
				for (Integer integer : xList) {
					System.out.print("  integer=" + integer);
				}
				System.out.println("");
			}
			System.out.println(System.lineSeparator());
		}
	}

	private static void multipleFor1() {
		int dimX = 4;
		int dimY = 4;
		int dimZ = 4;
		Random random = new Random();

		List<List<List<Integer>>> cubicB = new ArrayList<>();

		Stream.generate(() -> Stream.generate(() -> random.ints().limit(dimX).boxed().collect(Collectors.toList()))
				.limit(dimY).collect(Collectors.toList())).limit(dimZ).forEach(cubicB::add);

		cubicB.forEach(z -> z.forEach(y -> {
			System.out.println();
			y.forEach(System.out::print);
		}));

	}

	private static void print(IntStream stream, boolean asChar) {
		String format = (asChar) ? "%c" : "%d";
		stream.forEach(i -> System.out.format(format, i));
	}

	public static void main(String[] args) throws IOException {
		String filename = "text.txt";
		test(filename);
		System.out.println("\n\n####################### 1 ######################\n\n");
		test2(filename);
		System.out.println("\n\n####################### 2 ######################\n\n");
		String[] txt = { "State", "of", "the", "Lambda", "Libraries", "Edition" };
		int sum = Arrays.stream(txt).filter(s -> s.length() > 3).peek(System.out::println).map(String::length)
				.peek(System.out::println).reduce(0, Integer::sum);
		System.out.println("\n\n####################### 3 ######################\n\n");
		multipleFor();
		System.out.println("\n\n####################### 4 ######################\n\n");
		multipleFor1();
		System.out.println("\n\n####################### 5 ######################\n\n");
		Stream<String> sequentialStringStream = Stream.of("Ich", "ging", "im", "Walde", "so", "für", "mich", "hin",
				"...");
		Stream<String> emptyStream = Stream.empty();
		Stream<Double> infiniteSequentialStream = Stream.generate(Math::random);
		Stream<Integer> sequentialIntegerStream = Stream.iterate(2, i -> i ^ 2);
		IntStream intStreamWithRange = IntStream.range(0, 10);
		String str = "abc";
		IntStream chars1 = str.codePoints();
		IntStream chars2 = str.chars();
		print(chars1, true);
		print(intStreamWithRange, false);
		System.out.println("\n\n####################### 6 ######################\n\n");
		String poem = "Ich ging im Wald so für mich hin und nichts zu suchen das war mein Sinn";
		Stream<String> words = Pattern.compile("[^\\p{L}]").splitAsStream(poem);
		words.forEach(s -> System.out.print("<" + s + ">"));
		System.out.println("\n\n####################### 7 ######################\n\n");
		IntStream characters = Files.lines(Paths.get(filename), java.nio.charset.StandardCharsets.ISO_8859_1)
				.flatMap(Pattern.compile("[^\\p{L}]")::splitAsStream).flatMapToInt(String::chars);
		characters.forEach(s -> System.out.format("%c", s));
		System.out.println("\n\n####################### 8 ######################\n\n");
		// Collection - Collectors
		List<String> stringStream = Arrays
				.asList(new String[] { "State", "of", "the", "Lambda", "Libraries", "Edition" });
		// Stream<String> stringStream = Stream.of( "State", "of", "the",
		// "Lambda", "Libraries", "Edition" );
		String resultString = stringStream.stream().collect(Collectors.joining(" "));
		System.out.println("Collect Result = " + resultString);
		// inperformant, temporary String objects used
		resultString = stringStream.stream().reduce("", (s1, s2) -> s1 + "|" + s2);
		System.out.println("Reduce Result = " + resultString);
		StringBuilder sb = new StringBuilder();
		// Now we have a stateful lambda expression!!! Be careful when use it in
		// parallel streams, StringBuilder is not thread safe
		// StringBuffer is an alternative --> performance and order of
		// strings!!!
		stringStream.stream().forEach(s -> {
			sb.append(s);
			sb.append(",");
		});
		resultString = sb.toString();
		System.out.println("StringBuilder Result = " + resultString);

		// Using collectors
		Set<String> resultSet = stringStream.parallelStream().filter(w -> w.length() > 0)
				.filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toSet());
		// Collectors.toSet() --> HashSet (unordered)
		System.out.print("Filter out all uppercase words: ");
		resultSet.stream().forEach(System.out::print);
		System.out.println(System.lineSeparator());
		Set<String> orderedResultSet = stringStream.parallelStream().filter(w -> w.length() > 0)
				.filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toCollection(TreeSet::new));
		System.out.print("Filter out all uppercase words (ordered): ");
		orderedResultSet.stream().forEach(System.out::print);
		System.out.println(System.lineSeparator());

		// Map-Collectors
		Collection<Person> people = new ArrayList<Person>();
		people.add(new Person(1, "Hans", "AA"));
		people.add(new Person(2, "Franz", "BB"));
		people.add(new Person(3, "Kurt", "CC"));
		people.add(new Person(4, "Andi", "DD"));
		people.add(new Person(5, "Peter", "BB"));
		Map<Integer, String> idToName = people.stream().collect(Collectors.toMap(Person::gettIN, Person::getName));
		Map<Integer, Person> idToPerson = people.stream()
				.collect(Collectors.toMap(Person::gettIN, Function.identity()));
		// Define what to do when there are multiple values for one key (second
		// param)
		Map<String, List<Person>> addressToPerson = people.stream().collect(Collectors.toMap(Person::getAddress, p -> {
			List<Person> tmp = new ArrayList<>();
			tmp.add(p);
			return tmp;
		} , (l1, l2) -> {
			l1.addAll(l2);
			return l1;
		}));
		idToName.forEach((i, s) -> System.out.println(i + ": " + s));
		idToPerson.forEach((i, s) -> System.out.println(i + ": " + s));
		addressToPerson.forEach((i, s) -> {
			System.out.println(i + ": ");
			s.stream().forEach(System.out::println);
		});

	}
}
