package elasticsearch;

import com.cindy.util.JwtUtil;
import org.junit.Test;

public class JwUtilTest {
    @Test
    public void testBuild(){
        System.out.println(JwtUtil.build(29,"昵称","头像.jpg"));
    }

    @Test
    public void testVerify(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLnlKjmiLfnmbvlvZXorqTor4EiLCJuaWNrTmFtZSI6IuaYteensCIsImlzcyI6IkNpbmR5IiwiaWQiOjI5LCJhdmF0YXIiOiLlpLTlg48uanBnIiwiZXhwIjoxNjczMzI2NDg4LCJpYXQiOjE2NzI3MjE2ODh9.bKC01w0h_gqQprFoCXMq8zyr1dgtcpC9SsqgtWBD2hc";
        System.out.println(JwtUtil.verify(token));
    }
}
