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

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zj.springcloud.common.msg.RestResponse;

@FeignClient(value = "springcloud-auth", fallback = ClientAuthErrorService.class)
public interface ClientAuthService
{
    @RequestMapping(value = "/client/token", method = RequestMethod.POST)
    RestResponse<String> getAccessToken(
            @RequestParam(value = "clientId") String clientId,
            @RequestParam(value = "secret") String secret);
}
