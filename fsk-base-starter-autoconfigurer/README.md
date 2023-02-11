
# **Fsk-base基础框架使用：**

### **一、项目结构**


*① 基础 jar包项目git地址：*https://git.fortunecare.com.cn/base/fsk-base-starter.git

*② 监控平台 jar包地址：*https://git.fortunecare.com.cn/base/fsk-xxl-job.git

++***小知识***++


```
1、业务公共bean可以放如jar包中，方便统一通用，如登录用户的bean：包名com.fsk.framework.bean，由框架统一维护；

2、如果必要时你想直接使用容器中的bean，可以使用FskSpringContextHolder来获取，当然我们不建议这么做；

3、抛出异常统一用BizException类，如在catch代码块中throw new BizException(String errorCode, String errorMsg)；controller层返回结果类使用BaseApiResponse；

4、建议在应用层添加登录态：你可以在com.fsk.framework.core.interceptor.login包中开启登录拦截器；

5、mybatis-plus支持了在insert和update的时候，自动填入createDate,updateDate等等必要字段信息，处理Handler是包中的MyMetaObjectHandler；

6、如果你的业务Service逻辑中有处理多线程执行任务的时候，你可以使用com.fsk.framework.core.async包中的异步处理工具；

7、常用公共枚举类在com.fsk.framework.enums包中，后续可以继续更新；公共常量类在FskConstants中；常用utils工具包在com.fsk.framework.extend.utils中；

8、所有的controller、service、mapper、model均需实现对应的顶级接口类：FskBaseController、FskBaseService、FskBaseMapper、FskBaseModel；

最后，个人能力有限，封装不合理或者有误的地方，还请大家积极提出指正，一起学习进步。
```


### **二、StartUp**

#### pom.xml中导入fsk-base-starter 1.0.0依赖jar包：
```java
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.3</version>
    <relativePath/>
</parent>
<dependencies>
  <dependency>
    <groupId>com.fsk.framework</groupId>
    <artifactId>fsk-base-starter</artifactId>
    <version>1.0.0</version>
    <type>pom</type>
  </dependency>
</dependencies>
```

#### 在pom.xml中配置私服仓库

```
<repositories>
    <repository>
        <id>fsk</id>
        <url>https://hub.fortunecare.com.cn/repository/maven/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <releases>
            <enabled>true</enabled>
        </releases>
    </repository>
</repositories>
```

###### ##### #### bootstrap.yml配置profiles和application name基本信息，注册中心nacos信息，数据库mysql、redis信息即可。dev\test\prod环境配置依然按照老方式配置，配置信息必须以fsk为前缀开头，按以下示例固定格式配置：


###### 和非业务参数隔离，nacos上只配置业务逻辑参数：
1
2
3
4
###### ##### # 自定义业务参数，在nacos上配置

```
yourBizKey:
  bizKey:
    bizValue: 12345
```

##### 启动类添加FskAppStarterService注解，主方法类使用：FskApplication.run(AiServiceApplication.class,args);来启动。

```
package com.fsk;
 
import com.fsk.framework.annotation.FskAppStarterService;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@FskAppStarterService // 使用该注解
public class AiServiceApplication extends AbstractApp {  // 启动类需继承该抽象类
 
    public static void main(String[] args) {
        // 使用该种方式启动
        FskApplication.run(AiServiceApplication.class,args);
    }
 
    // 启动扩展方法
    @Override
    FskDecorateProxy decorate(FskDecorateProperties properties) {
        // 向properties添加属性以扩展，后续支持
        return properties.build();
    }
}
```

##### 扩展方法：提供了自定义方法（目前还未规划），通过实现方法如下：

1
2
3
4

```
@Override
FskDecorateProxy decorate(FskDecorateProperties properties) {
    return properties.build();
}
```

### 三、Metrics指标监控
###### 在包package com.fsk.framework.metrics;中是监控指标类，监控类AppRequestMetrics实现了http请求的95线和99线监控，并发监控，请求总量监控：

###### 当然，你也可以自定义埋点监控指标，示例：


```
@Configuration
public class AppCommonTagMetrics {
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName){
        // commonTags指标
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
```

###### 监控指标邮件告警（待更新）

###### 具体实现，可以参见博客及官方文档：

###### http://www.heartthinkdo.com/?p=2457

###### https://gitee.com/mirrors/micrometer?utm_source=alading&utm_campaign=repo#milestone-release

### 四、分布式定时任务平台
###### 定时任务平台使用的是开源项目XXL-Job，Doc文档：https://www.xuxueli.com/xxl-job/



###### 编码使用方式：在executor包中新建自己的执行器类，方法标记注解@FskJobSign，默认value为job的name，扩展属性有init、destroy方法


```
@Component
public class MyExecutor {
    /**
    * 自定义job执行方法，用 @FskJobSign 注解标记
    /
    @FskJobSign("myFirstJob")
    public void executor(){
        System.out.println("这是我的第一个job");
    }
}
```

##### 配置和监控平台：
##### 主屏监控
##### 定时任务邮件告警推送（待更新）

### 五、链路追踪平台
###### 基于sleuth和zipkin构建追踪平台，jar包中已经集成了该项功能，开发人员无需操作，应用引入fsk-base-starter.jar包即可。可以根据traceId进行追踪分析请求链路。

###### 有兴趣的伙伴可以参见官方文档学习：https://zipkin.io/pages/quickstart.html



### 六、jacoco代码测试覆盖


##### 基于jacoco实现代码覆盖测试

### 七、优化点
###### 搭建http跨网关调用平台，控制权限：管控跨网关接口调用，通过平台发放自定义accessToken准入机制，实现跨网关安全调用；
###### 调用链路注册与监控；
###### 监控项目中所有的feign调用；
###### 分布式事务构建：使用JTA的分布式事务或阿里开源分布式事务
###### 服务降级、熔断
### 八、远期规划
###### 应用集群，无损发布
###### 灰度发布
###### mycat读写分离
###### 容灾
###### 集成安全及统一登录配置：（微信、小程序、APP、PC）免登、弱登、强登一键配置
