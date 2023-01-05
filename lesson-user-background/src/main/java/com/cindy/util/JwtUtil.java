package com.cindy.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cindy
 */
public class JwtUtil {

    /**
     * token令牌秘钥：服务端密钥值随意，不要泄露
     */
    private static final String SECRET_KEY = "my-secret";

    /**
     * 过期时间判定阈值，单位毫秒：超过30分钟视为即将过期，单位毫秒
     */
    private static final long EXPIRATION_TIME_THRESHOLD = 1000 * 60 * 30L;

    /**
     * 根据用户的部分信息生成对应的token字符串
     * <p>
     * 默认会将用户的主键，昵称和头像设置到token字符串的payload中
     *
     * @param id       用户的主键
     * @param nickName 用户的昵称
     * @param avatar   用户的头像
     * @return token字符串
     */
    public static String build(Integer id, String nickName, String avatar) {
        if (NullUtil.hasNull(id, nickName, avatar)) {
            throw new RuntimeException("必要参数为空");
        }
        //获取'JWTCreator.Builder'类型的builder内部类构建器
        JWTCreator.Builder builder = JWT.create();
        //在payload中设置自定义项，如主键，昵称，头像等
        builder.withClaim("id", id);
        builder.withClaim("nickName", nickName);
        builder.withClaim("avatar", avatar);
        //在payload中设置Token主题
        builder.withSubject("用户登录认证");
        //在payload中设置Token发行作者
        builder.withIssuer("Cindy");
        //在payload中设置Date类型的Token发行时间
        builder.withIssuedAt(new Date());
        //在payload中设置Date类型的Token过期时间
        builder.withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7L)));
        //通过HS256算法和密钥设置签名
        return builder.sign(Algorithm.HMAC256(SECRET_KEY));
    }

    /**
     * 验证token字符串，验证失败直接抛出异常，
     * 验证成功后判断当前token是否即将过期，若是则生成新的token并返回，否则返回原token值
     *
     * @param token token字符串
     * @return token信息，若expireSoon为true，表示token即将过期，反之表示token不需要刷新
     */
    public static Map<String, Object> verify(String token) {
        try {

            // 校验参数
            if (null == token) {
                throw new RuntimeException("token字符串不能为空");
            }

            // 通过秘钥验证token字符串，验证失败直接抛出异常
            DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);

            // 若token过期时间小于指定阈值，则重新生成token并返回
            if (decodedJwt.getExpiresAt().getTime() - System.currentTimeMillis() < EXPIRATION_TIME_THRESHOLD) {
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("expireSoon", true);
                resultMap.put("newToken", JwtUtil.build(
                        decodedJwt.getClaim("id").asInt(),
                        decodedJwt.getClaim("nickName").asString(),
                        decodedJwt.getClaim("avatar").asString()));
                return resultMap;
            }

            // 若token过期时间大于指定阈值，不需要重新生成token
            return Collections.singletonMap("expireSoon", false);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("token验证失败");
        }
    }
}