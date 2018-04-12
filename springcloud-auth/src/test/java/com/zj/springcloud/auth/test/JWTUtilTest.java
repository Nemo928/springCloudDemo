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
package com.zj.springcloud.auth.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zj.springcloud.common.jwt.JWTInfo;
import com.zj.springcloud.common.utils.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JWTUtilTest
{
    private final static Logger LOGGER = LoggerFactory
            .getLogger(JWTUtilTest.class);

    @Autowired
    private JWTUtil             jwtUtil;

    @Test
    public void generateToken() throws Exception
    {
        JWTInfo info = new JWTInfo();
        info.setLoginName("allnas");
        info.setId(1L);
        info.setUserName("赵大神");
        String token = jwtUtil.generateToken(info);
        LOGGER.info("token:{}", token);

        Jws<Claims> claimsJws = jwtUtil.parserToken(token);
        LOGGER.info("claimsJws:{}", claimsJws);

        assertEquals("allnas", jwtUtil.getInfoFromToken(token).getUniqueName());
    }
}