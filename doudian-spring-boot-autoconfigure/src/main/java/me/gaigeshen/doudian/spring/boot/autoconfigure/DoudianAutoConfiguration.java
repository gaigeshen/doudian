package me.gaigeshen.doudian.spring.boot.autoconfigure;

import me.gaigeshen.doudian.client.DoudianClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaigeshen
 */
@ConditionalOnClass(DoudianClient.class)
@EnableConfigurationProperties(DoudianProperties.class)
@Configuration
public class DoudianAutoConfiguration {



}
