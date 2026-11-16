package database;

import model.ShortLinkContainer;

public interface Database {
    void add(String shortLink, ShortLinkContainer container);

    ShortLinkContainer get(String shortLink);

    boolean has(String shortLink);

    void deleteExpired();

    void delete(String shortLink);

    void edit(String shortLink, ShortLinkContainer container);
}
