public interface Database {
    void add(String shortLink, ShortLinkContainer container);

    ShortLinkContainer get(String shortLink);

    boolean has(String shortLink);

    void deleteExpired();
}
