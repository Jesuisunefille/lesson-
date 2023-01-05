package util;

import com.cindy.util.JedisSentinelUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisSentinelUtilTest {

    @Test
    public void testGetJedis() {
        Jedis jedis = JedisSentinelUtil.getJedis();
        System.out.println(jedis.ping());
        JedisSentinelUtil.closeJedis(jedis);
    }
}
