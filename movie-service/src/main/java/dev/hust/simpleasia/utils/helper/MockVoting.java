package dev.hust.simpleasia.utils.helper;

import java.util.Random;

public class MockVoting {

    private static Integer generateCount(Double voteAverage, Integer voteCount, Double minPercentage, Double maxPercentage) {

        Random random = new Random();

        // Calculate minimum and maximum values (cast to int for rounding)
        int minValue = (int) Math.ceil(minPercentage * voteCount);
        int maxValue = (int) Math.ceil(maxPercentage * voteCount);

        return random.nextInt(maxValue - minValue + 1) + minValue;
    }

    public static String generateVote(Double voteAverage, Integer voteCount, Integer originCount) {
        int[] ratings = {5, 4, 3, 2, 1};
        String res = "";

        Integer countFive = generateCount(voteAverage, voteCount, voteAverage < 2.5 ? 0.0 : 0.1, voteAverage < 2.5 ? 0.2 : 0.75);
        res += countFive + ", ";
        Integer countFour = generateCount(voteAverage, voteCount - countFive, 0.1, voteAverage < 2.5 ? 0.2 : 0.6);
        res += countFour + ", ";
        Integer countThree = generateCount(voteAverage, voteCount - countFive - countFour, 0.0, voteAverage < 2.5 ? 0.3 : 0.8);
        res += countThree + ", ";

        int remainCount = voteCount - countFive - countFour - countThree;

        int countTwo = (int) (originCount * voteAverage - countFive * 5 - countFour * 4 - countThree * 3 - remainCount);
        if (countTwo > remainCount || countTwo < 0) return "Failed";
        res += countTwo + ", ";
        int countOne = remainCount - countTwo;
        res += countOne;

       double summary = ratings[0] * countFive + ratings[1] * countFour + ratings[2] * countThree + ratings[3] * countTwo + ratings[4] * countOne;


        if (Math.abs(summary / originCount - voteAverage) > 0.01) return "Failed";

        return res;
    }
}
