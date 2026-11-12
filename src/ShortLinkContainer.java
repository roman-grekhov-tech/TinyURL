import java.time.LocalDateTime;
import java.util.UUID;

public class ShortLinkContainer {
    private final String originalLink;
    private final int limit;
    private int timesUsed;
    private final UUID owner;
    private final LocalDateTime expirationDate;

    public ShortLinkContainer(String originalLink, int limit, UUID owner, long timeToLiveInMinutes) {
        this.originalLink = originalLink;
        this.limit = limit;
        this.timesUsed = 0;
        this.owner = owner;

        LocalDateTime now = LocalDateTime.now();
        this.expirationDate = now.plusMinutes(timeToLiveInMinutes);
    }

    public void increaseTimesUsed() {
        timesUsed++;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public boolean hasReachedLimit() {
        return timesUsed >= limit;
    }

    public boolean hasExpired() {
//        return expirationDate.isAfter(LocalDateTime.now());
        return false;
    }

    public boolean hasAccess(UUID uuid) {
        return this.owner.equals(uuid);
    }
}
