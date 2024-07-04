package com.weilai.controller;


import com.weilai.base.BaseInfoProperties;
import com.weilai.grace.result.GraceJSONResult;
import com.weilai.tasks.SMSTask;
import com.weilai.utils.IPUtil;
import com.weilai.utils.MyInfo;
import com.weilai.utils.SMSUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController extends BaseInfoProperties {

    // 127.0.0.1:88/passport/getSMSCode

    @Resource
    private SMSTask smsTask;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(String mobile,
                                      HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.error();
        }

        // 获得用户的手机号/ip
        String  userIp = IPUtil.getRequestIp(request);
        // 限制该用户的手机号/ip在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, mobile);


        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        smsTask.sendSMSInTask(MyInfo.getMobile(), code);

        // 把验证码存入到redis中，用于后续的注册/登录的校验
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 10 * 60);

        return GraceJSONResult.ok();
    }
}
