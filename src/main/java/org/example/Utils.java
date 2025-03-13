package org.example;

import com.google.common.base.Strings;
//import it.list.jft.dd.ftxcandeal.r1.FTXCANDEAL;
//import it.list.jft.dd.ftxcandeal.r1.FT_C_PRICE_INFO;
//import it.list.jft.dd.ftxcandeal.r1.FT_C_PRICE_STATE;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.time.ZoneId;


public class Utils {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter MTIME_FORMATTER =
            DateTimeFormatter.ofPattern("HHmmssSSS");
    private static final DateTimeFormatter MICROSECONDS_FORMATTER =
            DateTimeFormatter.ofPattern("HHmmssSSSSSS");
    private static final DateTimeFormatter NANOSECONDS_FORMATTER =
            DateTimeFormatter.ofPattern("HHmmssSSSSSSSSS");

    private static final double RANDOM_BLANK_CHANCE = .25;

    public static final double FT_C_NULL_PRICE = 0.0;
    public static final double FT_C_NULL_YIELD = 9999.0;
    public static final double FT_C_NULL_RATE = -999999.0;
    public static final double FT_C_NULL_SPREAD = -999999.0;
    public static final double FT_C_NULL_NPV = -999999.0;
    
    private Utils() {
    	// So we can't instantiate the unnecessary class
    }
    
    private static final double[] POW_10 = {
            1.0,
            10.0,
            100.0,
            1000.0,
            10000.0,
            100000.0,
            1000000.0,
            10000000.0,
            100000000.0,
            1000000000.0,
            10000000000.0
    };

    public static int getLDateToday() {
        return Integer.parseInt(DATE_FORMATTER.format(LocalDate.now()));
    }

    public static int getMTimeNow() {
        return Integer.parseInt(MTIME_FORMATTER.format(LocalTime.now()));
    }

    public static long getTimeNowMicroseconds(){
          // Get the current local date and time with microsecond precision
          Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
          LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
          
          return Long.parseLong(localDateTime.format(MICROSECONDS_FORMATTER));
    }

    public static long getTimeNowNanoseconds(){
        // Get the current local date and time with nanosecond precision
        Instant now = Instant.now().truncatedTo(ChronoUnit.NANOS);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        
        return Long.parseLong(localDateTime.format(NANOSECONDS_FORMATTER));
    }
    
    public static double getRandomPrice(double origin, double bound, double tick) {
        if (ThreadLocalRandom.current().nextDouble() < RANDOM_BLANK_CHANCE) return FT_C_NULL_PRICE;
        return getRandomDouble(origin, bound, tick);
    }

    public static double getRandomYield(double origin, double bound) {
        if (ThreadLocalRandom.current().nextDouble() < RANDOM_BLANK_CHANCE) return FT_C_NULL_YIELD;
        return getRandomDouble(origin, bound);
    }

    public static double getRandomSpread(double origin, double bound, double tick) {
        if (ThreadLocalRandom.current().nextDouble() < RANDOM_BLANK_CHANCE) return FT_C_NULL_SPREAD;
        return getRandomDouble(origin, bound, tick);
    }

    public static double getRandomRate(double origin, double bound) {
        if (ThreadLocalRandom.current().nextDouble() < RANDOM_BLANK_CHANCE) return FT_C_NULL_RATE;
        return getRandomDouble(origin, bound);
    }

    public static double getRandomQuantity(double origin, double bound, double tick) {
        if (ThreadLocalRandom.current().nextDouble() < RANDOM_BLANK_CHANCE) return 0.0;
        return getRandomDouble(origin, bound, tick);
    }

    public static double getRandomDouble(double origin, double bound) {
        return round(ThreadLocalRandom.current().nextDouble(origin, bound), 6);
    }

    public static double getRandomDouble(double origin, double bound, double tick) {
        return roundToTick(ThreadLocalRandom.current().nextDouble(origin, bound), tick == 0 ? 0.001 : tick);
    }

    public static int getRandomInt(int origin, int bound) {
        if (ThreadLocalRandom.current().nextDouble() < RANDOM_BLANK_CHANCE) return 0;
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }


    public static int getPrecision(double number) {
        if (number == 0.001) {
            return 4; // shortcut for 99.9% of real securities
        } else if (number <= 1) {
            return ((int) -Math.log10(number)) + 1;
        } else {
            return (int) Math.log10(number);
        }
    }

    public static double round(double number, int decimals) {
        return ((long) (number * POW_10[decimals])) / POW_10[decimals];
    }

    public static double roundToTick(double number, double tick) {
        return round(Math.round(number / tick) * tick, getPrecision(tick));
    }

    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    // returns an exponentially increasing multiple of 100, capped at 300000 (5 minutes)
    public static long exponentialBackoffTimeMs(int attemptNumber) {
        // 2^12 *100 = 409600
        return attemptNumber > 11 ? 300000L : (long) Math.pow(2, attemptNumber) * 100L;
    }

}
