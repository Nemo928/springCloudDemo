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
package com.zj.springcloud.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.zj.springcloud.common.web.BaseController;
import com.zj.springcloud.entity.UserInfo;
import com.zj.springcloud.service.UserInfoService;

@RestController
public class IndexController extends BaseController
{
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("index")
    public String index(String name,
            @RequestHeader("auth-token") String authToken,
            @RequestHeader("client-token") String clientToken)
    {
        LOGGER.info("name:{}\nauthToken:{}\nclientToken:{}", name, authToken,
                clientToken);
        return userInfoService.getUserInfo(name, authToken, clientToken);
    }

    @GetMapping("getAll")
    public List<UserInfo> getAll(@RequestHeader("auth-token") String authToken,
            @RequestHeader("client-token") String clientToken)
    {
        return userInfoService.getAll(authToken, clientToken);
    }
}
