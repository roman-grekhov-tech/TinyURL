public class UrlShortener {

    private static final long initialCounter = Long.parseLong(Config.get("urlShortener.initialCounter"));
    private static long counter = initialCounter;

    public static String createShortUrl() {
        if (counter <= 0) counter = initialCounter;
        return Base62.encode(counter++);
    }

    private static class Base62 {

        private static final char[] ALPHABET =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        private static final int BASE = ALPHABET.length;

        static String encode(long num) {
            StringBuilder sb = new StringBuilder();
            while (num > 0) {
                sb.append(ALPHABET[(int) (num % BASE)]);
                num /= BASE;
            }

            return sb.reverse().toString();
        }
    }
}
