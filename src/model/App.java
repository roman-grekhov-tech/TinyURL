package model;

import database.ConcurrentHashMapDB;
import ui.linkRedaction.LinkRedaction;

import java.util.UUID;

public class App {
    private UUID user;
    private final ShortLinksService service;

    public App() {
        this.user = null;
        this.service = new ShortLinksService(new ConcurrentHashMapDB());
    }

    public UUID getUser() {
        return user;
    }

    public String addShortLunk(String originalLink) {
        if (user == null) createUser();
        return service.addShortLink(originalLink, user);
    }

    public ShortLinkUseResult useShortLink(String shortLink) {
        return service.useShortLink(shortLink);
    }

    public ShortLinkInfo getShortLinkInfo(String shortLink) {
        return service.getShortLinkInfo(shortLink, user);
    }

    public void deleteShortLink(String shortLink) {
        service.deleteShortLink(shortLink);
    }

    public void editLink(LinkRedaction redaction, String shortLink) {
        service.editShortLink(shortLink, redaction, user);
    }

    private void createUser() {
        user = UUID.randomUUID();
    }

    public void changeUSer(UUID user) {
        this.user = user;
    }

    public void shutdown() {
        service.shutdown();
    }
}
