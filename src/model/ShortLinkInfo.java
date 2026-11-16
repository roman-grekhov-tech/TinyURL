package model;

import java.time.LocalDateTime;

public class ShortLinkInfo {
    private final String originalLink;
    private final int limit;
    private final int timesUsed;
    private final LocalDateTime expirationDate;

    public ShortLinkInfo(ShortLinkContainer container) {
        this.originalLink = container.getOriginalLink();
        this.limit = container.getLimit();
        this.timesUsed = container.getTimesUsed();
        this.expirationDate = container.getExpirationDate();
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public int getLimit() {
        return limit;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
