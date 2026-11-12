public class ShortLinkUseResult {
    public enum Status {
        SUCCESS,
        NOT_FOUND,
        LIMIT_REACHED,
        EXPIRED
    }

    private final Status status;
    private final String originalLink;


    public ShortLinkUseResult(Status status, String originalLink) {
        this.status = status;
        this.originalLink = originalLink;
    }

    public ShortLinkUseResult(Status status) {
        this.status = status;
        this.originalLink = null;
    }

    public Status getResult() {
        return this.status;
    }

    public String getOriginalLink() {
        return this.originalLink;
    }
}
