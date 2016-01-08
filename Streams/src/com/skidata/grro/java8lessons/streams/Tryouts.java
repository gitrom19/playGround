package com.skidata.grro.java8lessons.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
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
		List<String> stringList = Arrays
				.asList(new String[] { "State", "of", "the", "Lambda", "Libraries", "Edition", "Lambda" });
		// Stream<String> stringStream = Stream.of( "State", "of", "the",
		// "Lambda", "Libraries", "Edition" );
		String resultString = stringList.stream().collect(Collectors.joining(" "));
		System.out.println("Collect Result = " + resultString);
		// inperformant, temporary String objects used
		resultString = stringList.stream().reduce("", (s1, s2) -> s1 + "|" + s2);
		System.out.println("Reduce Result = " + resultString);
		StringBuilder sb = new StringBuilder();
		// Now we have a stateful lambda expression!!! Be careful when use it in
		// parallel streams, StringBuilder is not thread safe
		// StringBuffer is an alternative --> performance and order of
		// strings!!!
		stringList.stream().forEach(s -> {
			sb.append(s);
			sb.append(",");
		});
		resultString = sb.toString();
		System.out.println("StringBuilder Result = " + resultString);

		System.out.println("\n\n####################### 8b ######################\n\n");
		// Using collectors
		Set<String> resultSet = stringList.parallelStream().filter(w -> w.length() > 0)
				.filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toSet());
		// Collectors.toSet() --> HashSet (unordered)
		System.out.print("Filter out all uppercase words: ");
		resultSet.stream().forEach(System.out::print);
		System.out.println(System.lineSeparator());
		Set<String> orderedResultSet = stringList.parallelStream().filter(w -> w.length() > 0)
				.filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toCollection(TreeSet::new));
		System.out.print("Filter out all uppercase words (ordered): ");
		orderedResultSet.stream().forEach(System.out::print);
		System.out.println(System.lineSeparator());

		System.out.println("\n\n####################### 8c ######################\n\n");
		// Map-Collectors
		Collection<Person> people = new ArrayList<Person>();
		people.add(new Person(1, "Hans", "AA"));
		people.add(new Person(2, "Franz", "BB"));
		people.add(new Person(3, "Kurt", "CC"));
		people.add(new Person(4, "Andi", "DD"));
		people.add(new Person(5, "Peter", "BB"));
		people.add(new Person(6, "Heidi", "DD"));
		Map<Integer, String> idToName = people.stream().collect(Collectors.toMap(Person::gettIN, Person::getName));
		// Function.identity() == p -> p
		Map<Integer, Person> idToPerson = people.stream()
				.collect(Collectors.toMap(Person::gettIN, Function.identity()));
		// Define what to do when there are multiple values for one key (second
		// param), otherwise you get IllegalStateException("Duplicate key")
		Map<String, List<Person>> addressToPerson = people.stream().collect(Collectors.toMap(Person::getAddress, p -> {
			List<Person> tmp = new ArrayList<>();
			tmp.add(p);
			return tmp;
		} , (l1, l2) -> {
			l1.addAll(l2);
			return l1;
		} , TreeMap::new));
		idToName.forEach((i, n) -> System.out.println(i + ": " + n));
		System.out.println("\n---");
		idToPerson.forEach((i, p) -> System.out.println(i + ": " + p));
		System.out.println("\n---");
		addressToPerson.forEach((a, lp) -> {
			System.out.print(a + ": ");
			lp.stream().forEach(System.out::print);
			System.out.print(System.lineSeparator());
		});
		// More elegant
		Map<String, List<Person>> addressToPersonGrouped = people.stream()
				.collect(Collectors.groupingBy(Person::getAddress));
		System.out.println("\n---");
		addressToPersonGrouped.forEach((a, lp) -> {
			System.out.print(a + ": ");
			lp.stream().forEach(System.out::print);
			System.out.print(System.lineSeparator());
		});

		//
		System.out.println("\n--- Grouped by first character");
		List<String> stringList2 = Arrays.asList(new String[] { "State", "of", "the", "Lambda", "Libraries", "Edition",
				" ", "", "!", "of", "exciting" });
		Map<Character, List<String>> groupedMap = stringList2.stream().filter(w -> w.length() > 0).distinct()
				.collect(Collectors.groupingBy(w -> w.charAt(0)));
		groupedMap.forEach((c, ls) -> {
			System.out.print("<" + c + ":");
			ls.forEach(System.out::print);
			System.out.println(">");
		});
		System.out.println("\n--- Partitions: Uppercase?");
		Map<Boolean, List<String>> partitionedMap = stringList2.stream().filter(w -> w.length() > 0).distinct()
				.collect(Collectors.partitioningBy(w -> Character.isUpperCase(w.charAt(0))));
		partitionedMap.forEach((c, ls) -> {
			System.out.print("<" + c + ":");
			ls.forEach(System.out::print);
			System.out.println(">");
		});
		System.out.println("\n--- Partitions: downstream to maximum?");
		Map<Character, Optional<String>> partitionedDownstreamedMap = stringList2.stream().filter(w -> w.length() > 0)
				.distinct().collect(Collectors.groupingBy(w -> w.charAt(0), Collectors.maxBy(String::compareTo)));
		partitionedDownstreamedMap.forEach((c, ls) -> {
			System.out.print("<" + c + ":");
			if (ls.isPresent()) {
				System.out.print(ls.get());
			} else {
				System.out.print("<--->");
			}
			System.out.println(">");
		});
		System.out.println("\n--- Partitions: largest element?");
		Optional<String> largestElement = stringList2.stream().filter(w -> w.length() > 0).distinct()
				.collect(Collectors.maxBy(String::compareTo));
		if (largestElement.isPresent()) {
			System.out.println("largestElemement: <" + largestElement.get() + ">");
		} else {
			System.out.println("---");
		}
		// Simpler version:
		Optional<String> largestElement2 = stringList2.stream().filter(w -> w.length() > 0).distinct()
				.max(String::compareTo);

		if (largestElement2.isPresent()) {
			System.out.println("largestElemement: <" + largestElement.get() + ">");
		} else {
			System.out.println("---");
		}

		System.out.println("\n\n####################### 9 Side effects ######################\n\n");
		List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));
		List<Integer> squaredIntList = intList.parallelStream().map(i -> i * i).collect(Collectors.toList());
		squaredIntList.forEach(i -> System.out.print(i + ","));

		ConcurrentHashMap<Integer, Integer> chm = new ConcurrentHashMap<>();
		chm.put(4, 4);
		chm.put(5, 5);
		chm.keySet().stream().map(i -> i * 2).forEach(k -> chm.put(k, k));
		// {16=16, 4=4, 20=20, 5=5, 8=8, 10=10}, nondeterministic result, -->
		// avoid non-inference violations, no modification of the underlying
		// data source of the stream.
		System.out.println("\nCHM: " + chm);

		// If the the original list should be concatenated with the new
		// calculations, save calculations intermediately.
		List<Integer> underlyingList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));
		List<Integer> resultList = underlyingList.stream().map(i -> i * 2).collect(Collectors.toList());
		underlyingList.addAll(resultList);
		System.out.println("\nConcatenated list: " + underlyingList);

		Map<Integer, Long> countLetters = Files.lines(Paths.get(filename), java.nio.charset.StandardCharsets.ISO_8859_1)
				.flatMapToInt(String::chars).filter(Character::isLetter).boxed()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		countLetters.forEach((i, l) -> {
			System.out.format("%c", i);
			System.out.println(": " + l);
		});

		System.out.println("\n\n####################### 10 Optionals ######################\n\n");

		try {
			Optional<String> opt = Stream.of(null, "hello", "world").findFirst();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		Stream.of(-2, -1, 0, 1, 2).reduce((i, j) -> i + j).ifPresent(summ -> System.out.println("sum: " + summ));
		System.out.println("sum: " + Stream.of(-2, -1, 0, 1, 2).filter(i -> i > 10).reduce((i, j) -> i + j).orElse(33));
		// equals reduce(0, (i,j) -> i+j);

		Set<Integer> results = new HashSet<Integer>();
		for (int i = 0; i < 3; i++) {
			Optional<Boolean> res = underlyingList.stream().findAny().map(results::add);
			if (res.isPresent()) {
				if (res.get() == true) {
					System.out.println("added item to set");
			
					
					
					
					
					
					
					
					results.stream().forEach(item -> System.out.println(" " + item));
				} else if (res.get() == false) {
					System.out.println("item already in set");
				} else if (res == Optional.<Boolean> empty()) {
					System.out.println("Input stream was empty");
				}
			}
		}
	}
}
