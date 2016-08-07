package com.comlanka.cas.webflow;
import com.comlanka.cas.principal.TokenCredentials;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.AbstractTicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

/**
 * Created by ishara on 8/6/2016.
 */
@Component("tokenAuthenticationAction")
public class TokenAuthAction extends AbstractAction{

    private static final String TOKEN_PARAMETER = "token";
    private static final String TOKEN_SERVICE_ID = "service";

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthAction.class);


    @NotNull
    @Autowired
    @Qualifier("centralAuthenticationService")
    private CentralAuthenticationService centralAuthenticationService;

    @NotNull
    @Autowired(required=false)
    @Qualifier("defaultAuthenticationSystemSupport")
    private AuthenticationSystemSupport authenticationSystemSupport = new DefaultAuthenticationSystemSupport();


    @Override
    protected Event doExecute(RequestContext context) throws Exception {
        HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        HttpSession session = request.getSession();

        // get token and username values
        String tokenValue = request.getParameter(TOKEN_PARAMETER);
//        String tokenService = request.getParameter(TOKEN_SERVICE_ID);
        final Service service =  WebUtils.getService(context);
        // Token exists
        if ( StringUtils.isNotBlank(tokenValue) ) {

            logger.debug(
                    "Got an authentication token: {} from service {}.",
                    tokenValue
//                    ,
//                    tokenService
            );

            try {
                // get credential
                @SuppressWarnings("unchecked")
                TokenCredentials credential = new TokenCredentials( tokenValue, service);

                // put service in session from flow scope
//                Service service = (Service) context.getFlowScope().get("service");
//                session.setAttribute("service", service);

                final AuthenticationContextBuilder builder = new DefaultAuthenticationContextBuilder(
                        this.authenticationSystemSupport.getPrincipalElectionStrategy());
                final AuthenticationTransaction transaction =
                        AuthenticationTransaction.wrap(credential);
                this.authenticationSystemSupport.getAuthenticationTransactionManager().handle(transaction,  builder);
                final AuthenticationContext authenticationContext = builder.build(service);

                final TicketGrantingTicket tgt = this.centralAuthenticationService.createTicketGrantingTicket(authenticationContext);
                WebUtils.putTicketGrantingTicketInScopes(context, tgt);
                return success();
            }
            catch (IllegalStateException | AuthenticationException | AbstractTicketException e)
            {
                logger.warn(e.getMessage(), e);
            }
        }
        return error();
    }

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
}
