package com.fsk.framework.executor.config;

import com.fsk.framework.pubconfig.entity.JobSourceEntity;
import com.fsk.framework.pubconfig.entity.KeyEntities;
import com.fsk.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author gary 2022-03-25
 */
@Slf4j
@Configuration
@ConditionalOnProperty(
        prefix = "fsk.global.job",
        value = "enable",
        havingValue = "true"
)
public class DistributedScheduledTaskConfig implements ApplicationContextAware, InitializingBean {

    @Value("${spring.application.name}")
    private String appname;
    private static JobSourceEntity jobSource;
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jobSource = context.getBean(KeyEntities.class).getJobSource();
    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobSource.getAdminAddr());
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(jobSource.getExecutorAddr());
        xxlJobSpringExecutor.setIp(jobSource.getIp());
        xxlJobSpringExecutor.setPort(jobSource.getPort());
        xxlJobSpringExecutor.setAccessToken(jobSource.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobSource.getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(jobSource.getLogretentiondays());
        log.info(">>>>>>>>>>> Fsk-job Executor Init Success.");
        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}