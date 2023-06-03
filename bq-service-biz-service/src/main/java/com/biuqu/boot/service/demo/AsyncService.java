package com.biuqu.boot.service.demo;

import com.biuqu.boot.common.demo.model.QrCodeInner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * spring集成线程池的注解使用样例
 *
 * @author BiuQu
 * @date 2023/2/26 18:01
 */
@Slf4j
@Component
public class AsyncService
{
    @Async("customAsync")
    public void showQr(QrCodeInner inner)
    {
        log.info("pool2 thread[{}] inner model:{}", Thread.currentThread().getName(), inner.getCode());
    }

    @Async
    public void showQr2(QrCodeInner inner)
    {
        log.info("pool3 thread[{}] inner model:{}", Thread.currentThread().getName(), inner.getCode());
    }
}
