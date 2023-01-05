package com.cindy.service;

import com.cindy.entity.User;
import com.cindy.param.*;

import java.util.List;

/**
 * @author cindy
 */
public interface UserService {

    /**
     * <h2>注册个人账号</h2>
     * <p> 01. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 02. 组装User实体：为 `gender/age/avatar/info/createTime/lastModify` 等属性设置默认值。
     * <p> 03. 使用 `BuildUtil.buildNickName()` 生成 `nickName` 随机用户昵称。
     * <p> 04. 使用 `EncryptionUtil.encryptPassword()` 对 `password` 密码进行MD5加密。
     * <p> 05. 调用Mapper接口添加用户记录，返回操作影响条目数。
     *
     * @param userRegisterParam 用户注册业务实体参数
     * @return 影响条目数
     */
    int register(UserRegisterParam userRegisterParam);

    /**
     * <h2>登录个人账号</h2>
     * <p> 01. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 02. 使用 `EncryptionUtil.encryptPassword()` 对 `password` 密码进行MD5加密。
     * <p> 03. 调用Mapper接口按账号密码查询User记录。
     * <p> 04. 登录失败：直接返回null值。
     * <p> 05. 登录成功：将电话号码和身份证号脱敏后返回。
     *
     * @param userLoginParam 用户登录业务实体参数
     * @return 登录成功返回该用户记录，失败返回null
     */
    User login(UserLoginParam userLoginParam);

    /**
     * <h2>按主键查询个人信息</h2>
     * <p> 01. 调用Mapper接口按主键查询User记录。
     * <p> 02. 查询失败：直接返回null值。
     * <p> 03. 查询成功：将电话号码和身份证号脱敏后返回。
     *
     * @param id User表主键
     * @return 返回该用户记录，查询失败返回null
     */
    User selectById(Integer id);

    /**
     * <h2>按手机号码查询个人信息</h2>
     * <p> 01. 调用Mapper接口按主键查询User记录。
     * <p> 02. 查询失败：直接返回null值。
     * <p> 03. 查询成功：将电话号码和身份证号脱敏后返回。
     *
     * @param phone 手机号码
     * @return 返回该用户记录，查询失败返回null
     */
    User selectByPhone(String phone);

    /**
     * <h2>修改个人信息</h2>
     * <p>其中ID，USERNAME和CREATE_TIME字段不允许修改
     * <p>其中REAL_NAME和ID_CARD在单独业务中进行修改
     * <p>其中PASSWORD在单独业务中进行修改
     * <p>其中PHONE在单独业务中进行修改
     * <p> 01. 判断必填属性：若主键为null，则直接抛出参数异常。
     * <p> 02. 调用Mapper接口按主键修改用户记录，返回操作影响条目数。
     *
     * @param userUpdateParam 用户修改业务实体参数
     * @return 影响条目数
     */
    int updateById(UserUpdateParam userUpdateParam);

    /**
     * <h2>修改个人密码</h2>
     * <p> 01. 调用Mapper接口检查该用户是否存在，不存在则抛异常。
     * <p> 02. 调用Mapper接口检查该用户的原密码是否正确，密码需要加密后比对，不正确则抛异常。
     * <p> 03. 调用Mapper接口修改该用户的密码为新密码，新密码需要加密和入库。
     *
     * @param userUpdatePasswordParam 用户修改密码业务实体参数
     * @return 影响条目数
     */
    int updatePasswordById(UserUpdatePasswordParam userUpdatePasswordParam);

    /**
     * <h2>注销个人账号</h2>
     * <p>
     * <p> 01. 方法需要添加 `@Transactional(rollbackFor = Exception.class)` 本地事务保护。
     * <p> 02. 调用Mapper接口检查该用户是否存在，不存在则抛异常。
     * <p> 03. 调用Mapper接口查询该用户关联的全部VideoOrder表记录。
     * <p> 04. 调用Mapper接口删除该用户关联的VideoOrder表记录，若不存在则略过。
     * <p> 05. 调用Mapper接口删除该用户关联的Order表记录，若不存在则略过。
     * <p> 06. 调用Mapper接口删除该用户的User表记录。
     *
     * @param userDeleteParam 注销用户的Param实体
     * @return 影响条目数
     */
    int deleteById(UserDeleteParam userDeleteParam);

    /**
     * 修改个人头像
     *
     * @param userUpdateAvatarDTO User表修改头像的DTO实体
     * @return 影响条目数
     */
    int updateAvatarById(UserUpdateAvatarParam userUpdateAvatarDTO);

    /**
     * 全查用户记录
     *
     * @return 全部用户的User记录
     */
    List<User> list();


    /**
     * <h2>获取一个4位的随机验证码</h2>
     * <p> 01. 调用Mapper接口检查手机号码是否存在，不存在则抛出异常。
     * <p> 02. 使用 `BuildUtil.buildVerificationCode(4)` 随机生成4位验证码。
     * <p> 03. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 04. 组装Key值：将手机号作为key值。
     * <p> 05. 组装Value值：将随机生成的4位验证码作为Value值。
     * <p> 06. 调用 `jedis.setex(key, field, time)` 将验证码存入Redis，时效5分钟。
     * <p> 07. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     * <p> 08. 调用 `SmsUtil.sendSms()` 向指定手机号码发送验证码并将验证码响应给客户端。
     *
     * @param phone 手机号码
     * @return 4位的随机验证码
     */
    String getVerificationCode(String phone);


    /**
     * <h2>按手机号码和验证码进行登录</h2>
     * <p> 01. 检查必要参数，若必要参数为空则直接抛出异常。
     * <p> 02. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 03. 调用 `jedis.get(key)` 判断验证码是否正确，错误直接抛出异常。
     * <p> 04. 调用Mapper接口返回手机号对应的用户信息。
     * <p> 05. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     *
     * @param userLoginByPhoneParam 用于用户按手机号码和验证码进行登录的Param实体参数
     * @return 登录失败返回null，登录成功返回对应的User信息
     */
    User loginByPhone(UserLoginByPhoneParam userLoginByPhoneParam);
}
