package com.comlanka.cas.handler;

import com.comlanka.cas.principal.TokenCredentials;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.integration.pac4j.authentication.handler.support.AbstractTokenWrapperAuthenticationHandler;
import org.pac4j.http.credentials.authenticator.Authenticator;
import org.springframework.stereotype.Component;

/**
 * Created by ishara on 8/6/2016.
 */
@Component("tokenAuthenticationHandlerCP")
public class TokenAuthenticationHandler extends AbstractTokenWrapperAuthenticationHandler
{
    @Override
    protected Authenticator getAuthenticator(Credential credentials)
    {
        boolean result = false;
        TokenCredentials credential = (TokenCredentials) credentials;
//        throw new BadCredentialsAuthenticationException("error.authentication.credentials.bad.token.key");
        result = true;
        return new CPAuthenticator();
    }

}
