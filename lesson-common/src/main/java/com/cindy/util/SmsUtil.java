package com.cindy.util;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

import java.util.Map;


public class SmsUtil {

    private static final String SERVER_IP = "app.cloopen.com";
    private static final String SERVER_PORT = "8883";
    private static final String ACCOUNT_SID = "2c94811c855ce725018571a5a8dc015c";
    private static final String ACCOUNT_TOKEN = "54036afa9a7c4ede81031dc319103175";
    private static final String APP_ID = "2c94811c855ce725018571a5a9b80163";

    @SuppressWarnings("all")
    public static Map<String, Object> sendSms(String phone, String msg) {
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(SERVER_IP, SERVER_PORT);
        sdk.setAccount(ACCOUNT_SID, ACCOUNT_TOKEN);
        sdk.setAppId(APP_ID);
        sdk.setBodyType(BodyType.Type_JSON);

        // 使用1号模板发送短信：[云通讯] 您使用的是云通讯短信模板，您的验证码是data[0]，请于data[1] 分钟内正确输入
        String[] datas = {msg, "5"};

        // 发送短信成功时，返回输出data包体的Map信息，异常时返回错误码和错误信息
        return sdk.sendTemplateSMS(phone, "1", datas);
    }
}
