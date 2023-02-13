# Fsk-base
An enterprise-level application framework based on spring cloud 3.0.3 and nacos is designed to simplify and standardize the structure of applications, encapsulate some own properties, and form a framework with its own characteristics.*![See the Chinese document](https://github.com/zrread/fsk-base/blob/main/README-zh.md)*

![license](https://img.shields.io/badge/license-Apache2.0-blue?style=flat&logoColor=D22128&logo=Apache)
![open issues](https://img.shields.io/badge/open%20issues-0%25-orange?style=flat&logo=Issuu)
![language](https://img.shields.io/badge/language-java-green?style=flat&logo=boulanger)
![downloads](https://img.shields.io/badge/downloads-10k/year-blueorange?style=flat&logoColor=blue&logo=Uploaded)
![Author](https://img.shields.io/badge/Author-Gary-640000?style=flat&logo=SurveyMonkey)
![Company](https://img.shields.io/badge/Company-%E7%A6%8F%E5%AF%BF%E5%BA%B7-01b528?style=flat&logo=Houzz)
![codecover](https://img.shields.io/badge/codecover-0%25-c23400?style=flat&logo=codecov)
![build](https://img.shields.io/badge/build-passing-c200b2?style=flat&logo=Buildkite)
![chat](https://img.shields.io/badge/chat-on%20wx%20or%20qq-5b05cb?style=flat&logoColor=EB1923&logo=tencentqq)
![springboot](https://img.shields.io/badge/springboot-version2.5.3-148400?style=flat&logo=springboot)
![springcloud](https://img.shields.io/badge/springcloud-version3.0.3-148400?style=flat&logo=spring)
![nacos](https://img.shields.io/badge/nacos-2.0.2-008799?style=flat&logo=Alibaba.com)

# ** Use of Fsk-base infrastructure：**

### **1、Program Structure**


*① Basic jar package project git address：*[https://github.com/zrread/fsk-base/edit/main/fsk-base-starter-autoconfigurer](https://github.com/zrread/fsk-base/edit/main/fsk-base-starter-autoconfigurer)

*② Monitoring platform jar package address：*[https://github.com/zrread/fsk-base/edit/main/fsk-base-starter-autoconfigurer](https://github.com/zrread/fsk-base/edit/main/fsk-base-starter-autoconfigurer)

++***Little knowledge***++


```text
1. Business public beans can be placed in jar packages to facilitate uniform and universal use, such as login user beans: package name com.fsk.framework.bean, which is maintained by the framework;
2. If you want to use the beans in the container directly when necessary, you can use the FskSpringContextHolder to obtain them. Of course, we do not recommend this;
3. The BizException class is used to throw exceptions, such as throw new BizException (String errorCode, String errorMsg) in the catch code block; The result class returned by the controller layer uses BaseApiResponse;
4. It is recommended to add login status in the application layer: you can open the login interceptor in the com.fsk.framework.core.interceptor.login package;
5. Mybatis-plus supports the automatic filling of necessary field information such as createDate and updateDate when inserting and updating. The processing handler is MyMetaObjectHandler in the package;
6. If your business service logic deals with multithreaded execution tasks, you can use the asynchronous processing tools in com.fsk.framework.core.async package;
7. Common public enumeration classes are in the com.fsk.framework.enums package, which can be updated later; The common constant class is in FskConstants; Common utils toolkits are in com.fsk.framework.extend.utils;
8. All controllers, services, mappers, and models need to implement the corresponding top-level interface classes: FskBaseController, FskBaseService, FskBaseMapper, and FskBaseModel;
Finally, if your personal ability is limited, and the package is unreasonable or wrong, please actively put forward corrections and learn and make progress together.
```


### **2:StartUp**

#### Importing fsk-base-starter 1.0.0 from pom.xml depends on jar package：
```xml
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

#### Configure private server warehouse in pom.xml

```xml
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

###### ##### #### Bootstrap.yml configures the basic information of profiles and application name, the registration center nacos information, and the database mysql and redis information. The environment configuration of dev  test  prod is still configured in the old way. The configuration information must start with fsk and be configured in the fixed format according to the following example：


###### It is isolated from non-business parameters. Only business logic parameters are configured on nacos：
1
2
3
4
###### ##### # User-defined business parameters, configured on nacos

```yaml
yourBizKey:
  bizKey:
    bizValue: 12345
```

##### The startup class adds the FskAppStarterService annotation. The main method class uses: FskApplication. run (AiServiceApplication. class, args); To start.

```java
package com.fsk;
 
import com.fsk.framework.annotation.FskAppStarterService;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@FskAppStarterService // Use this annotation
public class AiServiceApplication extends AbstractApp {  // The startup class needs to inherit this abstract class
 
    public static void main(String[] args) {
        // Start in this way
        FskApplication.run(AiServiceApplication.class,args);
    }
 
    // Start extension method
    @Override
    FskDecorateProxy decorate(FskDecorateProperties properties) {
        // Add attributes to properties to extend, and support later
        return properties.build();
    }
}
```

##### Extension method: provides a user-defined method (not yet planned). The implementation method is as follows:

```java
@Override
FskDecorateProxy decorate(FskDecorateProperties properties) {
    return properties.build();
}
```

### 3、 Metrics indicator monitoring
###### In package com.fsk.framework.metrics; The middle is the monitoring indicator class. The monitoring class AppRequestMetrics implements the 95-line and 99-line monitoring of http requests, concurrent monitoring, and total request monitoring:
###### Of course, you can also customize the buried point monitoring indicators, for example:


```java
@Configuration
public class AppCommonTagMetrics {
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName){
        // CommonTags indicator
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
```

###### Monitoring index email alarm (to be updated)
###### For specific implementation, please refer to the blog and official documents:
######  http://www.heartthinkdo.com/?p=2457
######  https://gitee.com/mirrors/micrometer?utm_source=alading&utm_campaign=repo#milestone -release
### 4、 Distributed timed task platform
###### The scheduled task platform uses the open source project XXL-Job, Doc document: https://www.xuxueli.com/xxl-job/
###### Code usage: create your own executor class in the executor package. The method tag annotation @ FskJobSign. The default value is the name of the job. The extension attributes include init and destroy methods


```java
@Component
public class MyExecutor {
    /**
    * Custom job execution method, marked with @ FskJobSign annotation
    /
    @FskJobSign("myFirstJob")
    public void executor(){
        System.out.println("This is my first job");
    }
}
```

##### Configuration and monitoring platform:
##### Main screen monitoring
##### Timed task email alarm push (to be updated)
### 5、 Link tracking platform
###### Build a tracking platform based on sleuth and zipkin. This function has been integrated in the jar package. Developers do not need to operate. The application can simply introduce the fsk-base-starter.jar package. The request link can be tracked and analyzed according to traceId.
###### Interested partners can learn from the official documents: https://zipkin.io/pages/quickstart.html
### 6、 Jacoco code test coverage
##### Code coverage testing based on jacoco
### 7、 Optimization point
###### Build an http cross-gateway call platform, control permissions: control cross-gateway interface calls, and issue a custom accessToken access mechanism through the platform to achieve cross-gateway security calls;
###### Call link registration and monitoring;
###### Monitor all feign calls in the project;
###### Distributed transaction construction: JTA distributed transaction or Alibaba open source distributed transaction
###### Service degradation and fusing
### 8、 Long-term planning
###### Application cluster, lossless release
###### Grayscale publishing
###### Mycat read-write separation
###### Disaster recovery
###### Integrated security and unified login configuration: (WeChat, applet, APP, PC) login free, weak login, strong login one-key configuration
