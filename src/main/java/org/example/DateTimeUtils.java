package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
    public static final int JULIAN_CONSTANT = 15018;

    private static final LocalDate START_JULIAN = LocalDate.of(1900, 1, 1);

    public static LocalDate ftDateToLocalDate(int ftDate) {
        // ftDate: yyyyMMdd
        return LocalDate.of(ftDate / 10000, (ftDate / 100) % 100, ftDate % 100);
    }

    // NB the Excel date is computed EXCLUSIVE of ftDate
    public static int ftDateToExcelDate(int ftDate) {
        return (int) ChronoUnit.DAYS.between(START_JULIAN, ftDateToLocalDate(ftDate));
    }

    // NB the FMR date is computed INCLUSIVE of ftDate
    public static int excelDateToFMRDate(int excelDate) {
        return excelDate + JULIAN_CONSTANT + 1;
    }

    // NB the FMR date is computed INCLUSIVE of ftDate
    public static int ftDateToFMRDate(int ftDate) {
        return excelDateToFMRDate(ftDateToExcelDate(ftDate));
    }

    // private constructor to hide the implicit one
    private DateTimeUtils() { }
}
