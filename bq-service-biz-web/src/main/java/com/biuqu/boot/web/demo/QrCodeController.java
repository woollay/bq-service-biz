package com.biuqu.boot.web.demo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.biuqu.boot.common.demo.constants.DemoConst;
import com.biuqu.boot.common.demo.model.QrCode;
import com.biuqu.boot.common.demo.model.QrCodeInner;
import com.biuqu.boot.common.demo.model.QrCodeResult;
import com.biuqu.boot.handler.CircuitBreakerHandler;
import com.biuqu.boot.service.RestService;
import com.biuqu.boot.service.demo.AsyncService;
import com.biuqu.boot.web.BaseBizController;
import com.biuqu.log.annotation.AuditLogAnn;
import com.biuqu.model.ResultCode;
import com.biuqu.thread.CommonThreadPool;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * 二维码登录接口
 *
 * @author BiuQu
 * @date 2023/2/12 10:46
 */
@Slf4j
@RestController
public class QrCodeController extends BaseBizController<QrCodeResult, QrCode, QrCodeInner>
{
    @AuditLogAnn
    @PostMapping("/demo/qr")
    @Override
    public ResultCode<QrCodeResult> get(@RequestBody QrCodeInner inner)
    {
        log.info("current inner:{}", JsonUtil.toJson(inner));
        ExecutorService executor = CommonThreadPool.getExecutor("demo", 4, 12);
        executor.execute(() -> log.info("pool1 thread[{}] inner model:{}", Thread.currentThread(), inner.getCode()));
        asyncService.showQr(inner);
        asyncService.showQr2(inner);
        log.info("current executor:{}", executor);
        return ResultCode.ok(inner.toModel().toModel());
    }

    /**
     * 获取Jwk公钥
     *
     * @param inner 业务入参模型
     * @return 公钥结果对象
     */
    @SentinelResource(value = "/demo/jwk", blockHandlerClass = CircuitBreakerHandler.class, defaultFallback = "handle")
    @PostMapping("/demo/jwk")
    @Override
    public ResultCode<QrCodeResult> execute(@RequestBody QrCodeInner inner)
    {
        log.info("current inner:{}", JsonUtil.toJson(inner));
        return restService.execute(inner.toModel());
    }

    /**
     * 注入自定义的Rest服务
     */
    @Resource(name = DemoConst.DEMO_REST_SERVICE)
    private RestService<QrCodeResult, QrCode> restService;

    /**
     * 注入异步服务
     */
    @Autowired
    private AsyncService asyncService;
}
