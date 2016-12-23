/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author mathiaskielholz
 */
public class DateUtil {

    public static String getDateStringOf(Date date) {
        Format formatter = new SimpleDateFormat("dd.MM.yy");
        return formatter.format(date);
    }

    public static LocalDate getLocalDateOf(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        formatter = formatter.withLocale(Locale.GERMAN);
        return LocalDate.parse(dateString, formatter);
    }

    public static String getTodayString() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        return localDate.format(formatter);
    }

    public static boolean isBefore(String dateString1, String dateString2) {
        return getLocalDateOf(dateString1).isBefore(getLocalDateOf(dateString2));
    }
}
