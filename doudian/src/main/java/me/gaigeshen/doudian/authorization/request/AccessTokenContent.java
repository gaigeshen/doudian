package me.gaigeshen.doudian.authorization.request;

import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.MetadataAttributes;

import static me.gaigeshen.doudian.config.Constants.ACCESS_TOKEN_TEMPLATE_URL;
import static me.gaigeshen.doudian.request.content.Metadata.Type.NONE;

/**
 * @author gaigeshen
 */
@MetadataAttributes(url = ACCESS_TOKEN_TEMPLATE_URL, method = "get", requireAccessToken = false, type = NONE)
public class AccessTokenContent extends AbstractContent<AccessTokenResult> {



}
