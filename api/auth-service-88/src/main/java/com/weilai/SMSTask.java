package com.weilai;

import com.weilai.utils.SMSUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SMSTask {

    @Resource
    private SMSUtils smsUtils;

    @Async
    public void sendSMSInTask(String mobile, String code) throws Exception {
        // 腾讯云短信未开通
        // smsUtils.sendSMS(mobile, code);
        log.info("异步任务中所发送的验证码为：{}", code);
    }

}