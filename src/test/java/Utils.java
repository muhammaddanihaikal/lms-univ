import java.util.Random;

public class Utils {

    public static String randomNumber(int length) {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(digits.charAt(random.nextInt(digits.length())));
        }

        return sb.toString();
    }
}
