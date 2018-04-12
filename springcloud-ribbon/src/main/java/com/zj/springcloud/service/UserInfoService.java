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
package com.zj.springcloud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zj.springcloud.entity.UserInfo;

@Service
public class UserInfoService
{
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getUserInfoError")
    public String getUserInfo(String name, String authToken, String clientToken)
    {
        String url = "http://SPRINGCLOUD-UCENTER/index?name=" + name;
        return getInfo(authToken, clientToken, url, HttpMethod.GET,
                String.class);
    }

    public String getUserInfoError(String name, String authToken,
            String clientToken)
    {
        return name + ",服务关闭";
    }

    @HystrixCommand(fallbackMethod = "getAllError")
    @SuppressWarnings("unchecked")
    public List<UserInfo> getAll(String authToken, String clientToken)
    {
        String url = "http://SPRINGCLOUD-UCENTER/getAll";
        return getInfo(authToken, clientToken, url, HttpMethod.GET, List.class);
    }

    public List<UserInfo> getAllError(String authToken, String clientToken)
    {
        return Lists.newArrayList();
    }

    private <T> T getInfo(String authToken, String clientToken, String url,
            HttpMethod method, Class<T> clazz)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("auth-token", authToken);
        headers.add("client-token", clientToken);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null,
                headers);
        return restTemplate.exchange(url, method, requestEntity, clazz)
                .getBody();
    }
}