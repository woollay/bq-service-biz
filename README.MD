# 构建远程调用的说明

## 1.构建远程调用第三方的说明

### 1.1.录入请求的url(如:`/auth/wx`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d012', 'CLIENT_WX_API', '/demo/qr', 'ClientUrl');
```

### 1.2.录入请求的url对应的配置参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d013', 'CLIENT_WX_API', 'client.to.channel', 'ChannelConfig');
```

### 1.3.录入请求的url对应的渠道参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc201', 'client.to.channel', 'app001', 'CLIENT_WX_API', 'CHANNEL_WX_API', '1566382443412');
```

### 1.4.录入请求的url对应的渠道URL参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d002', 'CHANNEL_WX_API', 'https://api.weixin.qq.com/sns/oauth2/access_token', 'ChannelUrl');
```

### 1.5.录入请求的url对应的渠道结果处理参数(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d120', 'channel.status', 'true', 'CHANNEL_WX_API');
INSERT INTO public.bq_global_dict(id, key, value, type)
VALUES ('d121', 'channel.snake', 'true', 'CHANNEL_WX_API');
```

### 1.6.配置客户调用限流(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc231', 'client.limit.qps', 'app001', 'CLIENT_WX_API', '20', '1566382443412');
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc232', 'client.limit.max', 'app001', 'CLIENT_WX_API', '20', '1566382443412');
```

### 1.7.配置客户调用对应的渠道限流(如:`/auth/qr`)

```sql
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc234', 'channel.limit.qps', 'CLIENT_WX_API', 'CHANNEL_WX_API', '20', '1566382443412');
INSERT INTO public.bq_global_config(id, svc_id, client_id, url_id, svc_value, create_time)
VALUES ('svc235', 'channel.limit.max', 'CLIENT_WX_API', 'CHANNEL_WX_API', '20', '1566382443412');
```
