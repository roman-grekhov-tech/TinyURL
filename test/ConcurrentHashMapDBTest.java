import database.ConcurrentHashMapDB;
import database.Database;
import model.ShortLinkContainer;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConcurrentHashMapDBTest {
    @Test
    public void shouldDeleteExpiredLinks() {
        Database database = new ConcurrentHashMapDB();
        UUID user = UUID.randomUUID();

        ShortLinkContainer a = new ShortLinkContainer("a", 10, user);
        ShortLinkContainer b = new ShortLinkContainer("b", 10, user);
        ShortLinkContainer toDelete = new ShortLinkContainer("c", 10, user);

        toDelete.setExpirationDate(LocalDateTime.now().minusMinutes(1));

        database.add("a", a);
        database.add("b", b);
        database.add("toDelete", toDelete);

        database.deleteExpired();

        Assert.assertTrue(database.has("a"));
        Assert.assertTrue(database.has("b"));
        Assert.assertFalse(database.has("toDelete"));

    }

}
