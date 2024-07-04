package com.weilai.controller;

import com.weilai.SMSTask;
import com.weilai.utils.MyInfo;
import com.weilai.utils.SMSUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("a")
public class HelloController {

    @Resource
    private SMSUtils smsUtils;

    @Resource
    private SMSTask smsTask;

    @GetMapping("hello")
    public Object hello() {
        return "Hello world~";
    }

    @GetMapping("sms")
    public Object sms() throws Exception {

        // 腾讯云短信未开通
        // smsUtils.sendSMS(MyInfo.getMobile(), "9875");
        return "Send SMS OK~";
    }

    @GetMapping("smsTask")
    public Object smsTask() throws Exception {

        smsTask.sendSMSInTask(MyInfo.getMobile(), "8111");
        return "Send SMS In Task OK~";
    }
}
