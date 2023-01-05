package util;

import com.cindy.util.NullUtil;
import org.junit.Test;

/**
 * @author cindy
 */
public class NullUtilTest {

    @Test
    public void testHasBlank() {
        System.out.println(NullUtil.hasBlank("admin", "zhaosi", "", "liuneng") ?
                "存在空字符串" : "不存在空字符串");
    }

    @Test
    public void testHasNull() {
        System.out.println(NullUtil.hasNull("admin", "zhaosi", null, "liuneng") ?
                "存在null" : "不存在null");
    }
}
