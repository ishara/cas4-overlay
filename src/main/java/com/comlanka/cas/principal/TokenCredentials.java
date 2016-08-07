package com.comlanka.cas.principal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jasig.cas.authentication.BasicIdentifiableCredential;
import org.jasig.cas.authentication.principal.Service;

import javax.validation.constraints.NotNull;

/**
 * Created by ishara on 8/6/2016.
 */
public class TokenCredentials extends BasicIdentifiableCredential {
    @NotNull
    private final Service service;
    public TokenCredentials(String tokenValue, Service service1)
    {
        super(tokenValue);
        this.service = service1;
    }
    public Service getService() {
        return this.service;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("service", service)
                .toString();
    }
}
