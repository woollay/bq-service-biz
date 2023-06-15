package com.biuqu.boot.common.demo.model;

import com.biuqu.annotation.JsonFuzzyAnn;
import com.biuqu.annotation.JsonMaskAnn;
import lombok.Data;

/**
 * 模拟的用户数据
 *
 * @author BiuQu
 * @date 2023/6/8 13:59
 */
@Data
public class UserInner
{
    /**
     * 用户名(打码到日志)
     */
    @JsonMaskAnn
    private String name;

    /**
     * 密码(不能打印到日志)
     */
    private String pwd;

    /**
     * 秘钥key(不能打印到日志)
     */
    private String key;

    /**
     * 真实姓名(打码到日志)
     */
    @JsonMaskAnn
    private String realName;

    /**
     * 身份证号(打码到日志)
     */
    @JsonMaskAnn
    private String cardId;

    /**
     * 银行卡号(打码到日志)
     */
    @JsonMaskAnn
    private String bankNo;

    /**
     * 电话号码(打码到日志)
     */
    @JsonMaskAnn
    private String phone;

    /**
     * 头像Base64(打码到日志)
     */
    @JsonMaskAnn
    private String photo;

    /**
     * 头像Base64签名值(打印摘要值到日志)
     */
    @JsonFuzzyAnn
    private String photoHash;
}
