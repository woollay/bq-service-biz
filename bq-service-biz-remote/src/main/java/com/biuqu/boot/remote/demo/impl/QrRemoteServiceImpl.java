package com.biuqu.boot.remote.demo.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.biuqu.boot.common.demo.constants.DemoConst;
import com.biuqu.boot.common.demo.model.QrCode;
import com.biuqu.boot.common.demo.model.QrCodeResult;
import com.biuqu.boot.remote.BaseRemoteService;
import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.http.CommonRestTemplate;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
        log.info("current param1:{},snake:{}", JsonUtil.toJson(model), snake);
        return callRemote(model, snake);
    }

    @SentinelResource(value = "demo_jwk2", blockHandler = "blockHandler")
    protected String callRemote(QrCode model, boolean snake)
    {
        log.info("current param2:{},snake:{}", JsonUtil.toJson(model), snake);
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
}
