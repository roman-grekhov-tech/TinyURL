import java.util.HashMap;
import java.util.UUID;

public class App {
    private UUID user;
    private final ShortLinksService service;

    public App() {
        this.user = null;
        this.service = new ShortLinksService(new ConcurrentHashMapDB());
    }

    public String addShortLunk(String originalLink) {
        if (user == null) createUser();
        return service.addShortLink(originalLink, user);
    }

    public ShortLinkUseResult useShortLink(String shortLink) {
        return service.useShortLink(shortLink);
    }

    private void createUser() {
        user = UUID.randomUUID();
    }
}
