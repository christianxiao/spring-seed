package com.profullstack.springseed.jproject.web.configuration;

import com.profullstack.springseed.infrastructure.web.CommonWebInitializer;
import com.profullstack.springseed.jproject.domain.configuration.DomainContextConfig;

/**
 * Created by christianxiao on 6/11/17.
 */
public class WebInitializer extends CommonWebInitializer {

    @Override
    protected Class<?>[] getRootContext() {
        return new Class<?>[]{DomainContextConfig.class};
    }

    @Override
    protected Class<?> getWebServlet() {
        return WebServletConfig.class;
    }

    @Override
    protected Class<?> getApiServlet() {
        return ApiServletConfig.class;
    }
}
