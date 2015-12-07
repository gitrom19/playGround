package com.skidata.grro.java8lessons.datestuff;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

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

		System.out.println("########## Human time ##########");
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
		LocalDate nextTaxDeadline = today.withDayOfMonth(10).plusMonths((today.getDayOfMonth() > 10) ? 1 : 0);
		System.out.println("Next tax deadlilne: " + nextTaxDeadline.toString());

		System.out.println("########## Period ##########");
		Period daysUntilNewYear = today
				.until(LocalDate.of(today.getYear(), Month.DECEMBER, 31).plus(1, ChronoUnit.DAYS));
		// In comparison to Duration a Period works on years, months, days...
		// instead of nanoseconds
		System.out.println("Days until new year: " + daysUntilNewYear.getDays());
		daysUntilNewYear.getUnits().stream().forEach(u -> System.out.print(u + " " + daysUntilNewYear.get(u)));
		MonthDay laDiadaDeCatalunya = MonthDay.of(Month.SEPTEMBER, 11);
		LocalDate siegeOfBarcelona = laDiadaDeCatalunya.atYear(1714);

		System.out.println("\n########## Adjuster ##########");
		System.out.println(
				LocalDate.of(2016, Month.NOVEMBER, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.SATURDAY)));

		System.out.println("\n########## Formatter ##########");
		ZonedDateTime fallOfBerlinWall = LocalDateTime.of(1989, 11, 9, 21, 15).atZone(ZoneId.of("Europe/Berlin"));
		String sResult = DateTimeFormatter.ISO_DATE_TIME.format(fallOfBerlinWall) + "\n"
				+ DateTimeFormatter.ISO_WEEK_DATE.format(fallOfBerlinWall) + "\n"
				+ DateTimeFormatter.RFC_1123_DATE_TIME.format(fallOfBerlinWall);
		System.out.println(sResult);

		for (FormatStyle fs : FormatStyle.values()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(fs);
			System.out.println(formatter.format(fallOfBerlinWall));
		}
		DateTimeFormatter italiaFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
				.withLocale(Locale.FRANCE);
		System.out.println("Locale Formatter: " + italiaFormatter.format(fallOfBerlinWall));

		DateTimeFormatter patternFormatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm a VV");
		System.out.println("Pattern formatter: " + patternFormatter.format(fallOfBerlinWall));
	}

}
