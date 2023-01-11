package util;

import com.cindy.util.BloomFilterUtil;
import org.junit.Test;
public class BloomFilterUtilTest {
    @Test
    public void testBloomFilterUtil(){
        BloomFilterUtil.add("Java");
        BloomFilterUtil.add("MySQL");
        BloomFilterUtil.add("Oracle");
        BloomFilterUtil.add("Node");
        BloomFilterUtil.add("HTML");
        BloomFilterUtil.add("JavaScript");
        BloomFilterUtil.add("CSS");
        System.out.println(BloomFilterUtil.isContain("Java"));
        System.out.println(BloomFilterUtil.isContain("MySQL"));
        System.out.println(BloomFilterUtil.isContain("Oracle"));
        System.out.println(BloomFilterUtil.isContain("Node"));
        System.out.println(BloomFilterUtil.isContain("HTML"));
        System.out.println(BloomFilterUtil.isContain("JavaScript"));
        System.out.println(BloomFilterUtil.isContain("CSS"));
        System.out.println(BloomFilterUtil.isContain("Nginx"));
        System.out.println(BloomFilterUtil.isContain("Elasticsearch"));
    }
}
