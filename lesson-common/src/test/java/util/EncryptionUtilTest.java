package util;

import com.cindy.util.EncryptionUtil;
import org.junit.Test;

/**
 * @author cindy
 */
public class EncryptionUtilTest {

    @Test
    public void testEncryptPhone() {
        System.out.println(EncryptionUtil.encryptPhone("15546128685"));
    }

    @Test
    public void testEncryptCard() {
        System.out.println(EncryptionUtil.encryptIdCard("230107199999990210"));
    }

    @Test
    public void testEncryptPassword() {
        System.out.println(EncryptionUtil.encryptPassword("admin"));
    }
}
