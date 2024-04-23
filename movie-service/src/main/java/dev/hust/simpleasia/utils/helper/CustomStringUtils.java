package dev.hust.simpleasia.utils.helper;

import dev.hust.simpleasia.core.exception.BusinessException;

public class CustomStringUtils {
    public static int[] splitStringToIntArray(String input, String regex) {
        if (input == null || input.isEmpty()) {
            throw new BusinessException("Input invalid format");
        }

        String[] stringArray = input.split(regex);
        int[] intArray = new int[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            try {
                intArray[i] = Integer.parseInt(stringArray[i].trim());
            } catch (NumberFormatException ex) {
                throw new BusinessException("Can not parse to number");
            }
        }

        return intArray;
    }

    public static String mergeIntArrayToString(int[] intArray, String regex) {
        if (intArray == null || intArray.length == 0) {
            throw new BusinessException("Input invalid format");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < intArray.length; i++) {
            sb.append(intArray[i]);

            if (i < intArray.length - 1) {
                sb.append(regex);
            }
        }

        return sb.toString();
    }
}
