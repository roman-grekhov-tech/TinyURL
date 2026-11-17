import model.AddShortLinkResult;
import model.ShortLinksService;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class ShortLinksServiceTest {
    ShortLinksService service = new ShortLinksService();

    @Test
    public void shouldRejectInvalidLink() {
        final UUID user = UUID.randomUUID();
        final String noScheme = "primer.com";

        final AddShortLinkResult result = service.addShortLink(noScheme, user);

        Assert.assertEquals(AddShortLinkResult.Status.ORIGINAL_LINK_IS_INVALID, result.getStatus());
    }

    @Test
    public void shouldAcceptValidLink(){
        final UUID user = UUID.randomUUID();
        final String noScheme = "https://primer.com";

        final AddShortLinkResult result = service.addShortLink(noScheme, user);

        Assert.assertEquals(AddShortLinkResult.Status.SUCCESS, result.getStatus());
    }
}
