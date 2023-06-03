package com.biuqu.boot.remote.demo.impl;

import com.biuqu.boot.common.demo.constants.DemoConst;
import com.biuqu.boot.common.demo.model.QrCode;
import com.biuqu.boot.common.demo.model.QrCodeResult;
import com.biuqu.boot.remote.BaseRemoteService;
import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.http.CommonRestTemplate;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 二维码远程查询服务
 *
 * @author BiuQu
 * @date 2023/3/18 15:27
 */
@Slf4j
@Service(DemoConst.DEMO_REMOTE_SERVICE)
public class QrRemoteServiceImpl extends BaseRemoteService<QrCodeResult, QrCode>
{
    @Override
    protected String call(QrCode model, boolean snake)
    {
        log.info("current param:{},snake:{}", JsonUtil.toJson(model), snake);
        return call(model);
    }

    @CircuitBreaker(name = "default", fallbackMethod = "callFallback")
    protected String call(QrCode model)
    {
        String channelUrl = this.getChannelUrl(model);
        if (StringUtils.isEmpty(channelUrl))
        {
            log.error("[{}]no channel[{}] url found.", model.getUrlId(), model.getChannelId());
            return null;
        }

        ResponseEntity<String> jwkJson = restTemplate.getForEntity(channelUrl, String.class);
        log.info("remote result:{}", jwkJson.getBody());
        return jwkJson.getBody();
    }

    /**
     * 服务降级
     *
     * @param model 业务模型
     * @param e     异常对象
     * @return 降级的结果
     */
    private String callFallback(QrCode model, Throwable e)
    {
        log.error("circuit break happened:{}.", JsonUtil.toJson(model), e);
        if (e instanceof CallNotPermittedException)
        {
            String name = ((CallNotPermittedException)e).getCausingCircuitBreakerName();
            io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitRegistry.circuitBreaker(name);
            io.github.resilience4j.circuitbreaker.CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
            log.error("Circuit[{}]'s metrics:{},with param:{}", name, JsonUtil.toJson(metrics), JsonUtil.toJson(model));
        }
        return StringUtils.EMPTY;
    }

    @Override
    protected ResultCode<QrCodeResult> toModel(String json, TypeReference<ResultCode<QrCodeResult>> typeRef,
        boolean snake)
    {
        if (null == json)
        {
            return ResultCode.error(ErrCodeEnum.SERVER_ERROR.getCode());
        }
        QrCodeResult result = new QrCodeResult();
        result.setOpenId(Hex.toHexString(json.getBytes(StandardCharsets.UTF_8)));
        return ResultCode.ok(result);
    }

    /**
     * 注入远程服务
     */
    @Autowired
    private CommonRestTemplate restTemplate;

    /**
     * 注入断路策略
     */
    @Autowired
    private CircuitBreakerRegistry circuitRegistry;
}
