package com.cindy.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Cindy
 */
public class EncryptionUtil {

    private final static int PHONE_NUMBER_DIGITS = 11;
    private final static int CARD_NUMBER_DIGITS = 18;

    public static String encryptPhone(String phone) {
        if (phone == null) {
            throw new RuntimeException("phone is null..");
        }
        if (phone.length() != PHONE_NUMBER_DIGITS) {
            throw new RuntimeException("wrong phone number format..");
        }
        return phone.replaceAll("(\\d{3})\\d*(\\d{4})", "$1****$2");
    }

    public static String encryptIdCard(String idCard) {
        if (idCard == null) {
            throw new RuntimeException("idCard is null..");
        }
        if (idCard.length() != CARD_NUMBER_DIGITS) {
            throw new RuntimeException("wrong id card number format..");
        }
        return idCard.replaceAll("(\\d{6})\\d*(\\d{3})", "$1*********$2");
    }

    /**
     * 使用MD5算法对密码进行加密
     *
     * <h1>对称加密算法</h1>
     * <p>代表：3DES / AES / DES等
     * <p>举例：将 `a` 变成 `4`，将 `b` 变成 `5`，于是密码 `ab` 被加密成了 `45`。
     * <p>总结：加密规则规律固定，了解规律即可破解，安全性极低。
     *
     * <h1>单向HASH加密算法</h1>
     * <p>代表：MD5 / SHA1等
     * <p>举例：将 `a` 变为 `#`，将 `b` 变成 `1`，于是密码 `ab` 被加密成了 `#1`。
     * <p>总结：HASH算法无规律，无法直接反向破解，但可通过建立彩虹表进行查表破解，如：
     * <p>我的密码经过MD5加密后变成 `Q!#FV!#0G!`
     * <p>你的密码经过MD5加密后也是 `Q!#FV!#0G!`
     * <p>于是你自然知道我的密码是什么了，虽然破解费点时间，但也不是绝对的安全。
     *
     * <h1>浮动HASH加密算法</h1>
     * <p>代表：BCRYPT：
     * <p>举例：将 `a` 变为 `k`，将 `b` 变成 `6`，于是密码 `ab` 被加密成了 `k6`：
     * <p>然后再加上一些随机salt（盐份），变成 `k6#$`
     * <p>由于每次添加的slat都是随机的，所以彩虹表暴力破解也无能为力。
     * <p>总结：BCRYPT是HASH算法的升级版，将salt随机混入加密后的密码，验证时也无需单独提供之前的salt，从而无需单独处理salt问题，是目前最安全的算法。
     *
     * @param password 原密码
     * @return MD5加密后的密码
     */
    @SneakyThrows
    public static String encryptPassword(String password) {
        if(StringUtils.isBlank(password)){
            throw new RuntimeException("参数为空");
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] array = md5.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        for (byte item : array) {
            builder.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return builder.toString().toUpperCase();
    }
}
