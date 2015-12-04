package com.skidata.grro.java8lessons.datestuff;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class DateTryouts {

	public static void main(String[] args) {
		System.out.println("########## Continuous time ##########");
		// Time axis with origin (=epoch)
		// Duration: amount of time, positive or negative, not bound to a point
		// of time on the time axis
		Duration d;
		Instant start = Instant.now();
		String s = "";
		for (int i = 0; i < 10_000; i++) {
			s = s + i;
		}
		Instant stop = Instant.now();
		d = Duration.between(start, stop);
		System.out.println("Time spent: " + d.toMillis());

		// ZoneId.getAvailableZoneIds().stream().sorted().distinct().forEach(System.out::println);
		// Exact point in time
		ZonedDateTime apollo11Launch = ZonedDateTime.of(1969, Month.JULY.getValue(), 16, 9, 32, 0, 0,
				ZoneId.of("EST5EDT"));
		System.out.println("Day of week of appollo 11 launch: " + apollo11Launch.getDayOfWeek());

		LocalDate mandelasBirthday = LocalDate.of(1918, Month.JULY, 18);
		LocalDate today = LocalDate.now();
		LocalDate xmas = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 24);
		LocalDate nextXmas = xmas.plusYears(1);
		System.out.println("Xmas in two years will be on a " + xmas.plus(2, ChronoUnit.YEARS).getDayOfWeek());
		System.out.println("My birthday was on a " + ZonedDateTime
				.of(LocalDateTime.of(1981, Month.MARCH, 19, 16, 0), ZoneId.of("Europe/Vienna")).getDayOfWeek());
	}

}
