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
package com.zj.springcloud.zuul.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PostFilter extends ZuulFilter
{
    private static Logger log = LoggerFactory.getLogger(PostFilter.class);

    @Override
    public boolean shouldFilter()
    {
        return true;
    }

    @Override
    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        long total = System.currentTimeMillis() - (Long) ctx.get("start");
        log.info("执行了：{}ms", total);
        return null;
    }

    @Override
    public String filterType()
    {
        return "post";// 路由之后
    }

    @Override
    public int filterOrder()
    {
        return 1;
    }

}
