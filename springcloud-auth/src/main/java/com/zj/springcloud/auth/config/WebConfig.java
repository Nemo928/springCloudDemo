//-------------------------------------------------------------------------
// Copyright (c) 2000-2016 Digital. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Digital
//
// Original author: zhaojin
//
//-------------------------------------------------------------------------
// APACHE
//-------------------------------------------------------------------------
package com.zj.springcloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zj.springcloud.common.handler.GlobalExceptionHandler;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Bean
    GlobalExceptionHandler globalExceptionHandler(){
        return new GlobalExceptionHandler();
    }
}
