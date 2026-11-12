import java.util.HashMap;
import java.util.UUID;

public class App {
    private UUID user;
    private final HashMap<String, ShortLinkContainer> links;
    private final ShortLinksService service;

    public App() {
        this.user = null;
        this.links = new HashMap<>();
        this.service = new ShortLinksService(links);
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
