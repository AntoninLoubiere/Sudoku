package fr.pyjacpp.sudoku;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

class Utils {
    @NonNull
    static String getStringFromResolveTime(long resolveTime, boolean precision) {
        long diffInHours = TimeUnit.MILLISECONDS.toHours(resolveTime);
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(resolveTime) % 60;
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(resolveTime) % 60;
        final StringBuilder stringBuilder = new StringBuilder();
        String s;

        if (diffInHours > 0) {
            s = String.valueOf(diffInHours);
            if (s.length() < 2) {
                stringBuilder.append('0');
            }
            stringBuilder.append(s);
            stringBuilder.append(':');
        }

        s = String.valueOf(diffInMin);
        if (s.length() < 2) stringBuilder.append("0");
        stringBuilder.append(s);
        stringBuilder.append(':');

        s = String.valueOf(diffInSec);
        if (s.length() < 2) stringBuilder.append("0");
        stringBuilder.append(s);

        if (precision) {
            stringBuilder.append('.');
            stringBuilder.append(resolveTime % 1000 / 10);
        }
        return stringBuilder.toString();
    }
}
