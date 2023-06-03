package com.biuqu.boot.service.demo.impl;

import com.biuqu.boot.common.demo.constants.DemoConst;
import com.biuqu.boot.common.demo.model.QrCode;
import com.biuqu.boot.common.demo.model.QrCodeResult;
import com.biuqu.boot.remote.RemoteService;
import com.biuqu.boot.service.BaseRestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自定义的Rest服务
 *
 * @author BiuQu
 * @date 2023/3/18 17:38
 */
@Service(DemoConst.DEMO_REST_SERVICE)
public class QrRestServiceImpl extends BaseRestService<QrCodeResult, QrCode>
{
    @Override
    protected RemoteService<QrCodeResult, QrCode> getRemoteService()
    {
        return remoteService;
    }

    /**
     * 注入远程服务名
     */
    @Resource(name = DemoConst.DEMO_REMOTE_SERVICE)
    private RemoteService<QrCodeResult, QrCode> remoteService;
}
