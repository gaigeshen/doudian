package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.client.DoudianResult;
import me.gaigeshen.doudian.client.config.Constants;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.request.content.MetadataAttributes;

/**
 * 获取访问令牌请求数据内容
 *
 * @author gaigeshen
 */
@MetadataAttributes(
        url = Constants.ACCESS_TOKEN_TEMPLATE_URL,
        method = "get",
        requireAccessToken = false,
        type = Metadata.Type.NONE
)
public class AccessTokenContent extends AbstractContent<DoudianResult<AccessTokenData>> {
}
