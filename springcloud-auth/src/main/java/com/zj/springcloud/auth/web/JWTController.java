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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zj.springcloud.common.msg.JwtAuthenticationResponse;
import com.zj.springcloud.common.web.BaseController;
import com.zj.springcloud.service.UserInfoService;

@RestController
@RequestMapping("jwt")
public class JWTController extends BaseController
{
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("token")
    public ResponseEntity<?> authorize(String loginName, String password)
            throws Exception
    {
        LOGGER.debug("loginName:{},password:{}", loginName, password);

        String token = userInfoService.login(loginName, password);

        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}
