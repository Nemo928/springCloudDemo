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
package com.zj.springcloud.auth.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zj.springcloud.common.exception.ClientInvalidException;
import com.zj.springcloud.common.msg.RestResponse;
import com.zj.springcloud.common.utils.ClientTokenUtil;
import com.zj.springcloud.common.web.BaseController;
import com.zj.springcloud.entity.GateClient;
import com.zj.springcloud.service.GateClientService;

@RestController
@RequestMapping("client")
public class ClientController extends BaseController
{
    @Autowired
    private GateClientService gateClientService;

    @Autowired
    private ClientTokenUtil   clientTokenUtil;

    @PostMapping("token")
    public RestResponse<String> accessToken(String clientId, String secret)
            throws Exception
    {
        LOGGER.debug("clientId:{},secret:{}", clientId, secret);

        GateClient gateClient = gateClientService.apply(clientId, secret);

        String token = clientTokenUtil.generateToken(gateClient);
        LOGGER.info("token:{}", token);

        return new RestResponse<String>().data(token);
    }

    @GetMapping("myClient")
    public RestResponse<List<String>> allowClient(String serviceId,
            String secret) throws ClientInvalidException
    {
        List<String> results = gateClientService.getAllowClient(serviceId,
                secret);
        LOGGER.info("results:{}", results);
        return new RestResponse<List<String>>().data(results);
    }
}
