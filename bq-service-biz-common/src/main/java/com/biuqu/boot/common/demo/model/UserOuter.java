package com.biuqu.boot.common.demo.model;

import com.biuqu.annotation.JsonMaskAnn;
import lombok.Data;

/**
 * 模拟的用户数据
 *
 * @author BiuQu
 * @date 2023/6/8 13:59
 */
@Data
public class UserOuter
{
    /**
     * 用户名(打码返回)
     */
    @JsonMaskAnn
    private String name;

    /**
     * 密码(不能接口返回)
     */
    private String pwd;

    /**
     * 秘钥key(不能接口返回)
     */
    private String key;

    /**
     * 真实姓名(接口返回)
     */
    @JsonMaskAnn
    private String realName;

    /**
     * 身份证号(打码返回)
     */
    @JsonMaskAnn
    private String cardId;

    /**
     * 银行卡号(打码返回)
     */
    @JsonMaskAnn
    private String bankNo;

    /**
     * 电话号码(打码返回)
     */
    @JsonMaskAnn
    private String phone;

    /**
     * 头像Base64(接口返回)
     */
    private String photo;

    /**
     * 头像Base64签名值(不能接口返回)
     */
    private String photoHash;
}
