package com.biuqu.boot.service.demo.impl;

import com.biuqu.boot.common.demo.model.QrCode;
import com.biuqu.service.BaseBizService;
import org.springframework.stereotype.Service;

/**
 * 二维码扫码服务
 *
 * @author BiuQu
 * @date 2023/2/12 11:10
 */
@Service
public class QrCodeServiceImpl extends BaseBizService<QrCode>
{
    @Override
    public QrCode get(QrCode model)
    {
        return model;
    }
}
