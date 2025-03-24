package nel.marco;

import java.util.Base64;

public class EncryptUtil {
    public EncryptUtil() {
    }

    public static String encrypt(String text) {
        String encodedText = Base64.getEncoder().encodeToString(text.getBytes());

        return shiftCharacters(encodedText, true);
    }

    public static String decrypt(String base64Encoding) {
        String s = shiftCharacters(base64Encoding, false);
        return new String(Base64.getDecoder().decode(s));
    }

    private static String shiftCharacters(String text, boolean shiftingCharactersUp) {

        StringBuilder sb = new StringBuilder();
        char[] chars = text.toCharArray();

        if (shiftingCharactersUp) {

            for (Character letter : chars) {
                int value = letter - 1;
                sb.append((char) value);
            }
        } else {
            for (Character letter : chars) {
                int value = letter + 1;
                sb.append((char) value);
            }
        }

        return sb.toString();
    }
}
