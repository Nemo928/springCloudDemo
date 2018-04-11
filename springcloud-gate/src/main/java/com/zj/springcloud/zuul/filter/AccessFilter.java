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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zj.springcloud.common.constants.CommonConstants;
import com.zj.springcloud.common.exception.ClientInvalidException;
import com.zj.springcloud.common.exception.ValidRequestException;
import com.zj.springcloud.common.handler.BaseContextHandler;
import com.zj.springcloud.common.jwt.IJWTInfo;
import com.zj.springcloud.common.msg.BaseResponse;
import com.zj.springcloud.common.msg.RestResponse;
import com.zj.springcloud.common.utils.JWTUtil;
import com.zj.springcloud.common.utils.JsonUtil;
import com.zj.springcloud.entity.Permission;
import com.zj.springcloud.service.PermissionService;
import com.zj.springcloud.zuul.service.ClientAuthService;

@Component
public class AccessFilter extends ZuulFilter
{
    private static Logger     log = LoggerFactory.getLogger(AccessFilter.class);

    @Autowired
    private JWTUtil           jwtUtil;

    @Value("${gate.ignore.startWith}")
    private String            startWith;

    @Value("${client.id}")
    private String            clientId;

    @Value("${client.secret}")
    private String            secret;

    @Value("${client.token-header}")
    private String            clientTokenHeader;

    @Value("${auth.user.token-header}")
    private String            authTokenHeader;

    @Autowired
    private ClientAuthService clientAuthService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean shouldFilter()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.put("start", System.currentTimeMillis());

        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        String authToken = request.getHeader(authTokenHeader);
        log.debug("requestURI:{},authToken:{}", requestURI, authToken);
        BaseContextHandler.setToken(null);

        if (isStartWith(requestURI))
        {
            return null;
        }

        try
        {
            // 验证客户合法性
            if (StringUtils.isBlank(authToken))
            {
                authToken = request.getParameter("token");
            }
            IJWTInfo info = jwtUtil.getInfoFromToken(authToken);
            log.info("info:{}", info);

            // 验证请求URL合法性
            List<Permission> permissions = permissionService
                    .getPermissions(info.getUserId());
            log.info("permissions:{}", permissions);

            if (!checkAll(permissions, requestURI))
            {
                throw new ValidRequestException("Illegal request!");
            }

            // 申请客户端秘钥
            BaseResponse resp = clientAuthService.getAccessToken(clientId,
                    secret);
            if (200 == resp.getStatus())
            {
                String clientToken = ((RestResponse<String>) resp).getData();
                log.info("clientToken:{}", clientToken);
                ctx.addZuulRequestHeader(clientTokenHeader, clientToken);
            }
            else
            {
                throw new ClientInvalidException("Gate client is Error!");
            }

            ctx.addZuulRequestHeader(authTokenHeader, authToken);
            BaseContextHandler.setToken(authToken);
        }
        catch (ClientInvalidException | ValidRequestException e)
        {
            setFailedRequest(JsonUtil.toJsonString(
                    new BaseResponse(e.getStatus(), e.getMessage())));
        }
        catch (Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            setFailedRequest(JsonUtil.toJsonString(
                    new BaseResponse(CommonConstants.TOKEN_ERROR_CODE,
                            "Get ACCESS-TOKEN failure")));
            return null;
        }

        BaseContextHandler.remove();
        return null;
    }

    private boolean checkAll(List<Permission> permissions, String method)
    {
        return permissions.parallelStream()
                .anyMatch(permission -> method.equals(permission.getUrl()));
    }

    @Override
    public String filterType()
    {
        return "pre"; // 路由之前
    }

    @Override
    public int filterOrder()
    {
        return 0;
    }

    private boolean isStartWith(String requestUri)
    {
        boolean flag = false;
        for (String s : startWith.split(","))
        {
            if (requestUri.startsWith(s))
            {
                return true;
            }
        }
        return flag;
    }

    private void setFailedRequest(String body)
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getResponseBody() == null)
        {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }
}
