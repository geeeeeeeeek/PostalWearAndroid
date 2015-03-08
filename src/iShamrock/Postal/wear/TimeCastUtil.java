package iShamrock.Postal.wear;


/**
 * Created by Tong on 03.08.
 */
public class TimeCastUtil {
    public static String getCastedTime(long duration) {
        /*sec,min,hour,day*/
        long[] cuts = new long[4];
        String[] des = new String[]{" seconds", " minutes", " hours", " days"};
        cuts[0] = duration % 60;
        duration /= 60;
        cuts[1] = duration % 60;
        duration /= 60;
        cuts[2] = duration % 24;
        duration /= 24;
        cuts[3] = duration;

        int count = 0;
        String result = "";
        for (int i = 3; i >= 0; i--) {
            if (cuts[i] != 0) {
                if (count == 0) {
                    result += cuts[i] + des[i];
                    count++;
                } else if (count == 1) {
                    result += "and " + cuts[i] + des[i];
                    break;
                }
            }
        }
        return result;
    }

    public static String getCastedPeriod(int[] activityStartedTime) {
        String result = " at " + activityStartedTime[0] + ":" + activityStartedTime[1];
        if (activityStartedTime[0] < 12 && activityStartedTime[0] > 4) {
            return result + " in the morning.";
        } else if (activityStartedTime[0] < 18) {
            return result + " in the afternoon.";
        } else {
            return result + " in the evening.";
        }
    }
}
