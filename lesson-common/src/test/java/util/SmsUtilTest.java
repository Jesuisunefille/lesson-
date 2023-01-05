package util;

import com.cindy.util.SmsUtil;
import org.junit.Test;

public class SmsUtilTest {

    @Test
    public void testSendSms() {
        System.out.println(SmsUtil.sendSms("15546128685", "5683"));
    }
}
