package iShamrock.Postal.wear;


/**
 * Created by Tong on 03.08.
 */
public class TimeCastUtil {
    public String getCastedTime(long duration) {
        /*sec,min,hour,day*/
        long[] cuts = new long[4];
        String[] des = new String[]{" seconds", " minutes", " hours", " days"};
        cuts[3] = duration % 60;
        if (duration > 60) duration /= 60;
        cuts[2] = duration % 60;
        if (duration > 60) duration /= 60;
        cuts[1] = duration % 24;
        if (duration > 24) duration /= 24;
        cuts[0] = duration;

        int count = 0;
        String result = "";
        for (int i = 0; i < 4; i++) {
            if (cuts[i] != 0) {
                if (count == 0) {
                    result += cuts[i] + des[i];
                    count++;
                } else if (count == 1) {
                    result += "and " + cuts[i] + des[i];
                }
            }
        }
        return result;
    }
}
