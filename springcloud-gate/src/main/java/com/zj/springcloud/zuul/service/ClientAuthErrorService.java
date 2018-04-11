//-------------------------------------------------------------------------
// Copyright (c) 2000-2016 Digital. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Digital
//
// Original author: allnas
//
//-------------------------------------------------------------------------
// APACHE
//-------------------------------------------------------------------------
package com.zj.springcloud.zuul.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zj.springcloud.common.msg.RestResponse;

@Component
public class ClientAuthErrorService implements ClientAuthService
{

    private Logger LOGGER = LoggerFactory
            .getLogger(ClientAuthErrorService.class);

    @Override
    public RestResponse<String> getAccessToken(String clientId,
            String secret)
    {
        LOGGER.error("clientId:{},secret:{}", clientId, secret);
        return new RestResponse<String>().data("");
    }

}
