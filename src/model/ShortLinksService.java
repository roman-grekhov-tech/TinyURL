package model;

import database.Database;
import ui.linkRedaction.LinkRedaction;

import java.util.UUID;

public class ShortLinksService {

    private static final int DEFAULT_LIMIT = Integer.parseInt(Config.get("shortLink.defaultLimit"));

    private final Database links;
    private final SchedulerToDeleteExpired scheduler;

    public ShortLinksService(Database links) {
        this.links = links;
        this.scheduler = new SchedulerToDeleteExpired(links);
    }

    public String addShortLink(String originalLink, UUID user) {
        String shortLink;
        do {
            shortLink = ShortLinkBuilder.createShortUrl();
        } while (links.has(shortLink));

        ShortLinkContainer container = new ShortLinkContainer(originalLink, DEFAULT_LIMIT, user);

        links.add(shortLink, container);

        return shortLink;
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

    public void shutdown() {
        scheduler.shutdown();
    }
}
