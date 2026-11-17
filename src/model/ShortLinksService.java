package model;

import database.ConcurrentHashMapDB;
import database.Database;
import model.linkRedaction.LinkRedaction;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class ShortLinksService {

    private static final int DEFAULT_LIMIT = Integer.parseInt(Config.get("shortLink.defaultLimit"));

    private final Database links;
    private final SchedulerToDeleteExpired scheduler;

    public ShortLinksService() {
        this.links = new ConcurrentHashMapDB();
        this.scheduler = new SchedulerToDeleteExpired(links);
    }

    public AddShortLinkResult addShortLink(String originalLink, UUID user) {

        if (!isValidUrl(originalLink)) {
            return new AddShortLinkResult(AddShortLinkResult.Status.ORIGINAL_LINK_IS_INVALID, null);
        }

        ShortLinkContainer container = new ShortLinkContainer(originalLink, DEFAULT_LIMIT, user);

        for (int attempt = 0; attempt < 3; attempt++) {
            String shortLink = ShortLinkGenerator.generateShortLink();
            if (links.add(shortLink, container) == null) {
                return new AddShortLinkResult(AddShortLinkResult.Status.SUCCESS, shortLink);
            }
        }

        return new AddShortLinkResult(AddShortLinkResult.Status.FAILED, null);
    }

    public ShortLinkInfo getShortLinkInfo(String shortLink, UUID user) {
        if (!links.has(shortLink)) return null;

        ShortLinkContainer container = links.get(shortLink);
        if (!container.hasAccess(user)) return null;

        return new ShortLinkInfo(container);
    }

    public void deleteShortLink(String shortLink) {
        links.delete(shortLink);
    }

    public void editShortLink(String shortLink, LinkRedaction redaction, UUID user) {
        ShortLinkContainer oldContainer = links.get(shortLink);
        if (oldContainer == null) return;

        if (!oldContainer.hasAccess(user)) return;

        ShortLinkContainer newContainer = new ShortLinkContainer(
                oldContainer.getOriginalLink(),
                redaction.getLimit(),
                user
        );
        newContainer.setExpirationDate(oldContainer.getExpirationDate());
        newContainer.setTimesUsed(oldContainer.getTimesUsed());

        links.edit(shortLink, newContainer);
    }

    public ShortLinkUseResult useShortLink(String shortLink) {
        if (!links.has(shortLink)) {
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

    private boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) return false;
        try {
            URI uri = new URI(url);
            return uri.isAbsolute() && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
