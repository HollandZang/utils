package convertor;

public class Timer {

    public static String getUsedTime(long millisecond, String formatter) {
        long quotient = millisecond / 1000;
        final long mill = millisecond % 1000;

        final long seconds = quotient % 60;
        quotient /= 60;

        final long minutes = quotient % 60;
        quotient /= 60;

        final long hours = quotient % 24;
        quotient /= 24;

        return formatter.replace("hh", String.valueOf(hours))
                .replace("mm", String.valueOf(minutes))
                .replace("ss", String.valueOf(seconds))
                .replace("nn", String.valueOf(mill));
    }
}
