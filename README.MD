# bq-service-biz服务使用说明

- bq微服务解决方案参见中的[`README`文档](https://github.com/woollay/bq-parent/blob/main/README.MD)
  ```xml
  <dependency>
	  <groupId>com.biuqu</groupId>
	  <artifactId>bq-parent</artifactId>
	  <version>1.0.3</version>
  </dependency>
  ```

  > 注意：如果无法下载该组件，则需要更换alibaba maven代理为官方代理：`https://repo1.maven.org/maven2/` ，因为alibaba在2022年底之后就更改了同步策略，新的jar包不保证同步；

- 本服务基于SpringBoot-Web二次封装，主要是为了简化常规服务的使用，当然也支持灵活的配置扩展：
- 本服务具备完整的独立服务能力，核心依赖：
    ```xml
    <dependency>
        <groupId>com.biuqu</groupId>
        <artifactId>bq-boot-base</artifactId>
        <version>1.0.3</version>
    </dependency>
    ```
  > 注意：如果无法下载该组件，则需要更换alibaba maven代理为官方代理：`https://repo1.maven.org/maven2/` ，因为alibaba在2022年底之后就更改了同步策略，新的jar包不保证同步；

## 1. 为什么要写bq-service-biz服务

- 微服务原本应该是使用非常简单的，但是SpringBoot为了兼容各种第三方的组件，必须提供非常开放的整合能力，且还不能绑定数据库、redis等具体的实现，这样必然会使用SpringBoot的微服务时会异常复杂；
- 我结合了多年的研发经验，从纯Web接口到Web管理服务(带页面)的研发过程中，逐步整合了加解密/Jackson/Guava/Redis/PostgreSQL等各种组件，并形成了可配置限流、可支持各种Json使用场景的微服务解决方案，一定程度上简化了微服务的使用场景，尽量做到拿来即用；
- 整合了较多的非功能的安全设计，包括记录用户操作日志、接口日志、数据库数据自动加解密等；
- 整合了sleuth/zipkin，并同时整合了logback，服务做到了Access Log和运行日志均具有链路追踪ID；
- 整合了`CircuitBreaker`/Sentinel服务降级、`Nacos`/`Eureka`服务注册中心，使之具有完备的服务能力；

## 2. 使用bq-service-biz服务有什么好处

- 简化了SpringBoot微服务的使用，提供了较多的扩展能力，如：加解密、限流、驼峰和下划线Json转换等基础能力；
- 对报文做了缓存和耗时记录，并贯穿了链路追踪ID，基本上可以达到开箱即用；
- 整合了docker脚本和docker-compose两种方式，可以非常方便地构建docker集群(目前只验证了MacOS)；

## 3. bq-service-biz最佳实践

- `bq-service-auth`最佳实践是配合[bq-service-gateway](https://github.com/woollay/bq-service-gateway) 和[bq-service-auth](https://github.com/woollay/bq-service-auth)
  中一起使用，以达到gateway关注鉴权（验证Token/完整性校验/加解密等）、auth服务关注认证（生成Token）、biz专注业务逻辑的微服务目标；

## 4.bq-service-biz限流初始化说明

### 4.1.录入请求的url(如:`/auth/wx`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d012', 'CLIENT_WX_API', '/demo/qr', 'ClientUrl');
```

### 4.2.录入请求的url对应的配置参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d013', 'CLIENT_WX_API', 'client.to.channel', 'ChannelConfig');
```

### 4.3.录入请求的url对应的渠道参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc201', 'client.to.channel', 'app001', 'CLIENT_WX_API', 'CHANNEL_WX_API', '1566382443412');
```

### 4.4.录入请求的url对应的渠道URL参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d002', 'CHANNEL_WX_API', 'https://api.weixin.qq.com/sns/oauth2/access_token', 'ChannelUrl');
```

### 4.5.录入请求的url对应的渠道结果处理参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d120', 'channel.status', 'true', 'CHANNEL_WX_API');
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d121', 'channel.snake', 'true', 'CHANNEL_WX_API');
```

### 4.6.配置客户调用限流(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc231', 'client.limit.qps', 'app001', 'CLIENT_WX_API', '20', '1566382443412');
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc232', 'client.limit.max', 'app001', 'CLIENT_WX_API', '20', '1566382443412');
```

### 4.7.配置客户调用对应的渠道限流(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc234', 'channel.limit.qps', 'CLIENT_WX_API', 'CHANNEL_WX_API', '20', '1566382443412');
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc235', 'channel.limit.max', 'CLIENT_WX_API', 'CHANNEL_WX_API', '20', '1566382443412');
```
