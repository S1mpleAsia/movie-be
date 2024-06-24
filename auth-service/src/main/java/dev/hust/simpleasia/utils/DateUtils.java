package dev.hust.simpleasia.utils;

import dev.hust.simpleasia.core.exception.BusinessException;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtils {
    public static Date toDate(String dateString, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(dateString);
        } catch (ParseException ex) {
            System.out.println("Parsing error");
            throw new BusinessException(ex.getMessage());
        }
    }
}
