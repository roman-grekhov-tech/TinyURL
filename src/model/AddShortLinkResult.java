package model;

public class AddShortLinkResult {
    public enum Status {
        SUCCESS, ORIGINAL_LINK_IS_INVALID, FAILED
    }

    private final Status status;
    private final String shortLink;

    public AddShortLinkResult(Status status, String shortLink) {
        this.status = status;
        this.shortLink = shortLink;
    }

    public Status getStatus() {
        return status;
    }

    public String getShortLink() {
        return shortLink;
    }
}
