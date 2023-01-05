package util;

import com.cindy.util.JedisStandaloneUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisStandaloneUtilTest {

    @Test
    public void testGetJedis() {
        Jedis jedis = JedisStandaloneUtil.getJedis();
        System.out.println(jedis.ping());
        JedisStandaloneUtil.closeJedis(jedis);
    }

}
