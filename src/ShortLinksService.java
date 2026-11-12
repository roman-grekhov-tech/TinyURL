import java.util.HashMap;
import java.util.UUID;

public class ShortLinksService {

    private static final int DEFAULT_LIMIT = Integer.parseInt(Config.get("shortLink.defaultLimit"));
    private static final int TTL_IN_MINUTES = Integer.parseInt(Config.get("shortLink.timeToLiveInMinutes"));

    private final HashMap<String, ShortLinkContainer> links;

    public ShortLinksService(HashMap<String, ShortLinkContainer> links) {
        this.links = links;
    }

    public String addShortLink(String originalLink, UUID user) {
        String shortLink;
        do {
            shortLink = ShortLinkBuilder.createShortUrl();
        } while (links.containsKey(shortLink));

        ShortLinkContainer container = new ShortLinkContainer(
                originalLink,
                DEFAULT_LIMIT,
                user,
                TTL_IN_MINUTES
        );

        links.put(shortLink, container);

        return shortLink;
    }

    public ShortLinkUseResult useShortLink(String shortLink) {
        if (!links.containsKey(shortLink)) {
            return new ShortLinkUseResult(ShortLinkUseResult.Status.NOT_FOUND);
        }

        ShortLinkContainer container = links.get(shortLink);

        if (container.hasExpired()) {
            return new ShortLinkUseResult(ShortLinkUseResult.Status.EXPIRED);
        }

        if (container.hasReachedLimit()) {
            return new ShortLinkUseResult(ShortLinkUseResult.Status.LIMIT_REACHED);
        }

        container.increaseTimesUsed();
        return new ShortLinkUseResult(ShortLinkUseResult.Status.SUCCESS, container.getOriginalLink());

    }

}
