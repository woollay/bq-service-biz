package com.biuqu.boot.web.demo;

import com.biuqu.boot.common.demo.model.UserInner;
import com.biuqu.boot.common.demo.model.UserOuter;
import com.biuqu.json.JsonFacade;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟的用户服务
 *
 * @author BiuQu
 * @date 2023/6/8 08:11
 */
@Slf4j
@RestController
public class DemoUserController
{
    @PostMapping("/demo/user/query")
    public ResultCode<UserOuter> execute(@RequestBody UserInner user)
    {
        log.info("current inner 1:{}", JsonUtil.toJson(user));
        log.info("current inner snake 2:{}", jsonFacade.toJson(user, true));
        log.info("current inner mask 3:{}", jsonFacade.toMask(user, true));
        log.info("current inner ignore 4:{}", jsonFacade.toIgnore(user, true));
        log.info("current inner all 5:{}", jsonFacade.toJson(user, true, true, true));
        UserOuter outer = new UserOuter();
        BeanUtils.copyProperties(user, outer);
        log.info("current outer 1:{}", JsonUtil.toJson(outer));
        log.info("current outer snake 2:{}", jsonFacade.toJson(outer, true));
        log.info("current outer mask 3:{}", jsonFacade.toMask(outer, true));
        log.info("current outer ignore 4:{}", jsonFacade.toIgnore(outer, true));
        log.info("current outer all 5:{}", jsonFacade.toJson(outer, true, true, true));
        ResultCode<UserOuter> resultCode = ResultCode.ok(outer);
        return resultCode;
    }

    /**
     * 注入json处理服务
     */
    @Autowired
    private JsonFacade jsonFacade;
}
