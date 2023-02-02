package hu.okrim.puzzlecenter;

public abstract class TimeFormatController {
    public static String createTimeText(int ms){
        int time = ms;
        //Millis / 60_000 gives number of minutes since 1m = 60s = 60_000ms
        String minutes = String.valueOf(time / 60_000);
        time = time % 60_000;
        //Whatever is left will be the number of seconds if devided by 1000
        String seconds = String.valueOf(time / 1_000);
        time = time % 1000;
        //Rest are the millis, and we only want to display two of them so we devide by 10
        String millis = String.valueOf((time % 1_000) / 10);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(minutes.length() == 1 ? "0" + minutes : minutes);
        stringBuilder.append(":");
        stringBuilder.append(seconds.length() == 1 ? "0" + seconds : seconds);
        stringBuilder.append(".");
        stringBuilder.append(millis.length() == 1 ? "0" + millis : millis);

        return stringBuilder.toString();
    }
}
