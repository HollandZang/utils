package SpeechAndLanguageProcessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式与自动机
 */
public class Part2 {

    public static void main(String[] args) {
        System.out.println(practise1("asd YOU ARE depressed balbalabala"));
        System.out.println(practise1("asd all balbalabala"));
        System.out.println(practise1("asd always balbalabala"));
    }

    public static String practise1(String sentence) {
        final Pattern pattern = Pattern.compile("\\bYOU ARE (depressed|sad)\\b");
        final Matcher matcher = pattern.matcher(sentence);
        if (matcher.find()) {
//            System.out.println(matcher.group() + ": " + matcher.start() + "-" + matcher.end());
            return (int) (Math.random() * 2) == 0 ?
                    "I AM SORRY TO HEARD YOU ARE" + sentence.substring(matcher.end())
                    : "WHY DO YOU THINK YOU ARE" + sentence.substring(matcher.end());
        }

        final Pattern pattern1 = Pattern.compile("\\ball\\b");
        final Matcher matcher1 = pattern1.matcher(sentence);
        if (matcher1.find()) {
            return "IN WHAT WAY";
        }

        final Pattern pattern2 = Pattern.compile("\\balways\\b");
        final Matcher matcher2 = pattern2.matcher(sentence);
        if (matcher2.find()) {
            return "CAN YOU THINK OF A SPECIFIC EXAMPLE";
        }

        return "";
    }
}
