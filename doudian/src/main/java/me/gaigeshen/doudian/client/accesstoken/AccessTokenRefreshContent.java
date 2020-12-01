package me.gaigeshen.doudian.client.accesstoken;

import me.gaigeshen.doudian.client.DoudianResult;
import me.gaigeshen.doudian.config.Constants;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.request.content.MetadataAttributes;

/**
 * @author gaigeshen
 */
@MetadataAttributes(
        url = Constants.ACCESS_TOKEN_REFRESH_TEMPLATE_URL,
        method = "get",
        requireAccessToken = false,
        type = Metadata.Type.NONE
)
public class AccessTokenRefreshContent extends AbstractContent<DoudianResult<AccessTokenData>> {
}
