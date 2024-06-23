package dev.hust.simpleasia.utils.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date formatDate(String dateString, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Parsing error");
            return null;
        }
    }
}
