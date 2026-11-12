import java.util.concurrent.ThreadLocalRandom;

public class ShortLinkBuilder {

    private static final int LENGTH = Integer.parseInt(Config.get("shortLink.length"));
    private static final char[] ALPHABET = Config.get("shortLink.alphabet").toCharArray();
    private static final String DOMAIN = Config.get("shortLink.domain");

    public static String createShortUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(DOMAIN);
        sb.append("/");
        for (int i = 0; i < LENGTH; i++) {
            sb.append(ALPHABET[ThreadLocalRandom.current().nextInt(ALPHABET.length)]);
        }

        return sb.toString();
    }
}
