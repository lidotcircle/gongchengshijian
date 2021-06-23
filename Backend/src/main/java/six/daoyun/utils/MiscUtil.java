package six.daoyun.utils;


public class MiscUtil {
    private static String one2nine = "123456789";
    public static String generateDigitString(int n) //{
    {
        String ans = "";
        for(;n>0;n--) {
            final int idx = (int)Math.floor(Math.random() * 100) % 9;
            ans += one2nine.charAt(idx);
        }

        return ans;
    } //}
}
