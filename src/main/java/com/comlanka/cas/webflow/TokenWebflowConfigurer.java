package com.comlanka.cas.webflow;

import org.jasig.cas.web.flow.AbstractCasWebflowConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;

/**
 * Created by ishara on 8/6/2016.
 */
@Component("tokenWebflowConfigurer")
public class TokenWebflowConfigurer extends AbstractCasWebflowConfigurer {

    @Override
    protected void doInitialize() throws Exception {
        final Flow flow = getLoginFlow();
        final ActionState actionState = createActionState(flow, "tokenAuthenticationCheck",
                createEvaluateAction("tokenAuthenticationAction"));
        actionState.getTransitionSet().add(createTransition(TRANSITION_ID_SUCCESS, TRANSITION_ID_SEND_TICKET_GRANTING_TICKET));
        createStateDefaultTransition(actionState, getStartState(flow).getId());
        setStartState(flow, actionState);
    }
}