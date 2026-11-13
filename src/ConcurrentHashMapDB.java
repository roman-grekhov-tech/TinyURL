import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapDB implements Database {
    private final ConcurrentHashMap<String, ShortLinkContainer> db;

    public ConcurrentHashMapDB() {
        this.db = new ConcurrentHashMap<>();
    }

    public void add(String shortLink, ShortLinkContainer container) {
        this.db.putIfAbsent(shortLink, container);
    }

    public ShortLinkContainer get(String shortLink) {
        return db.get(shortLink);
    }

    public boolean has(String shortLink) {
        return db.containsKey(shortLink);
    }

    public void deleteExpired() {
        for (Map.Entry<String, ShortLinkContainer> item : db.entrySet()) {
            if (item.getKey() != null && item.getValue().hasExpired()) {
                db.remove(item.getKey());
            }
        }
    }
}
