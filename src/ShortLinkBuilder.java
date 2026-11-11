import java.util.concurrent.ThreadLocalRandom;

public class ShortLinkBuilder {

    private static final int LENGTH = Integer.parseInt(Config.get("shortLinkLength"));
    private static final char[] ALPHABET = Config.get("shortLinkAlphabet").toCharArray();

    public static String createShortUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("tiny.ru/");
        for (int i = 0; i < LENGTH; i++) {
            sb.append(ALPHABET[ThreadLocalRandom.current().nextInt(ALPHABET.length)]);
        }

        return sb.toString();
    }
}
