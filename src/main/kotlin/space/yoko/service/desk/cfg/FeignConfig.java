package space.yoko.service.desk.cfg;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {

        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            requestTemplate.header("Authorization", "Bearer " + authentication.getCredentials());
        };
    }
}