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
package com.zj.springcloud.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.zj.springcloud.email.EmailService;

/**
 * 
 * 用于监听eureka服务停机通知
 *
 * @author allnas
 * @version 1.0
 * @since 2017年11月7日
 */
@Configuration
public class EurekaInstanceCancelListener
        implements ApplicationListener<ApplicationEvent>
{
    private Logger       logger = LoggerFactory
            .getLogger(EurekaInstanceCancelListener.class);

    @Autowired
    private EmailService emailService;

    @Value("${info.eurekaServiceCancalToEmail}")
    private String       to;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent)
    {
        if (applicationEvent instanceof EurekaInstanceCanceledEvent)
        {
            EurekaInstanceCanceledEvent event = (EurekaInstanceCanceledEvent) applicationEvent;
            // 获取当前Eureka实例中的节点信息
            PeerAwareInstanceRegistry registry = EurekaServerContextHolder
                    .getInstance().getServerContext().getRegistry();
            Applications applications = registry.getApplications();
            applications.getRegisteredApplications()
                    .forEach(registeredApplication -> {
                        registeredApplication.getInstances()
                                .forEach(instance -> {
                                    if (instance.getInstanceId()
                                            .equals(event.getServerId()))
                                    {
                                        logger.error(
                                                "服务【" + instance.getAppName()
                                                        + "】挂了");
                                        emailService.sendSimpleMail(to, "系統异常",
                                                "服务【" + instance.getAppName()
                                                        + "】挂了"); // 发送邮件
                                    }
                                });
                    });
        }
    }

}
