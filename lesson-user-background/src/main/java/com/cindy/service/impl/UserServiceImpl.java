package com.cindy.service.impl;

import com.cindy.entity.User;
import com.cindy.entity.VideoOrder;
import com.cindy.mapper.OrderMapper;
import com.cindy.mapper.UserMapper;
import com.cindy.mapper.VideoOrderMapper;
import com.cindy.param.*;
import com.cindy.service.UserService;
import com.cindy.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;

/**
 * @author cindy
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 头像上传目标目录
     */
    private static final String AVATAR_DEST_DIR_PATH = "D:/idea/upload/";

    @Override
    public int register(UserRegisterParam userRegisterParam) {

        // 检查必填参数
        if (NullUtil.hasNull(
                userRegisterParam.getUsername(),
                userRegisterParam.getPassword(),
                userRegisterParam.getPhone(),
                userRegisterParam.getRealName(),
                userRegisterParam.getIdCard())) {
            throw new RuntimeException("必填参数为空");
        }

        // 组装User实体
        User user = new User();
        BeanUtils.copyProperties(userRegisterParam, user);
        user.setNickName(BuildUtil.buildNickName());
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        user.setAvatar("default-user-avatar.jpg");
        user.setInfo("该用户很懒，没留下任何信息..");
        user.setGender(2);
        user.setAge(16);
        user.setCreateTime(new Date());
        user.setLastModify(new Date());

        // 添加用户记录
        return userMapper.insert(user);
    }

    @Override
    public User login(UserLoginParam userLoginParam) {

        String username = userLoginParam.getUsername();
        String password = userLoginParam.getPassword();

        // 检查必填参数
        if (NullUtil.hasNull(username, password)) {
            throw new RuntimeException("必要参数为空");
        }

        // 对密码进行MD5加密
        password = EncryptionUtil.encryptPassword(password);

        // 按账号密码查询User记录
        return encryptUser(userMapper.selectByUsernameAndPassword(username, password));
    }

    @Override
    public User selectById(Integer id) {
        return encryptUser(userMapper.selectById(id));
    }

    @Override
    public User selectByPhone(String phone) {
        return encryptUser(userMapper.selectByPhone(phone));
    }

    @Override
    public int updateById(UserUpdateParam userUpdateParam) {
        if (null == userUpdateParam.getId()) {
            throw new RuntimeException("必要参数为空");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateParam, user);
        return userMapper.updateById(user);
    }

    @Override
    public int updatePasswordById(UserUpdatePasswordParam userUpdatePasswordParam) {

        Integer id = userUpdatePasswordParam.getId();
        String oldPassword = userUpdatePasswordParam.getOldPassword();
        String newPassword = userUpdatePasswordParam.getNewPassword();

        if (NullUtil.hasNull(id, oldPassword, newPassword)) {
            throw new RuntimeException("必要参数为空");
        }

        // 检查该用户是否存在，不存在则抛异常
        User user = userMapper.selectById(id);
        if (null == user) {
            throw new RuntimeException("用户不存在");
        }

        // 检查该用户的原密码是否正确，密码需要加密后比对
        oldPassword = EncryptionUtil.encryptPassword(oldPassword);
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("原密码有误");
        }

        // 修改该用户的密码为新密码，新密码需要加密和入库
        newPassword = EncryptionUtil.encryptPassword(newPassword);
        user.setPassword(newPassword);
        return userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(UserDeleteParam userDeleteParam) {

        Integer userId = userDeleteParam.getUserId();
        if (null == userId) {
            throw new RuntimeException("必要参数为空");
        }

        // 检查该用户是否存在，不存在则抛异常
        this.checkUserIsExists(userId);

        // 查询该用户关联的全部VideoOrder表记录
        List<VideoOrder> videoOrders = videoOrderMapper.selectByUserId(userId);
        if (null != videoOrders && !videoOrders.isEmpty()) {

            // 删除该用户关联的VideoOrder表记录，若不存在则略过
            videoOrderMapper.deleteByUserId(userId);

            // 删除该用户关联的Order表记录，若不存在则略过
            for (VideoOrder videoOrder : videoOrders) {
                orderMapper.deleteById(videoOrder.getOrderId());
            }
        }

        // 删除该用户的User表记录
        return userMapper.deleteById(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateAvatarById(UserUpdateAvatarParam userUpdateAvatarDTO) {
        Integer id = userUpdateAvatarDTO.getId();
        MultipartFile avatarFile = userUpdateAvatarDTO.getAvatarFile();
        if (NullUtil.hasNull(id, avatarFile)) {
            throw new RuntimeException("必要参数为空");
        }
        User user = this.checkUserIsExists(id);
        FileUtil.deleteAvatarFromOss(user.getAvatar());
        String avatar = FileUtil.uploadAvatarToOss(avatarFile);
        return this.updateUserAvatar(avatar, id);
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    /**
     * 调用数据接口按主键查询用户记录，若用户不存在则抛异常
     *
     * @param id User表主键
     * @return 若用户存在，则返回该用户完整的User表记录
     */
    private User checkUserIsExists(Integer id) {
        User result = userMapper.selectById(id);
        if (null == result) {
            throw new RuntimeException("用户不存在");
        }
        return result;
    }

    /**
     * 按主键修改User表的头像地址
     *
     * @param avatar 头像地址
     * @param id     User表主键
     * @return 影响条目数
     */
    private int updateUserAvatar(String avatar, Integer id) {
        User user = new User();
        user.setId(id);
        user.setAvatar(avatar);
        return userMapper.updateById(user);
    }

    /**
     * 将User实体数据中的手机号码和身份证号码脱敏后返回
     *
     * @param user 包含手机号码和身份证号码的User实体
     * @return 将User实体数据中的手机号码和身份证号码脱敏后的User实体
     */
    private User encryptUser(User user) {
        if (null == user) {
            return null;
        }
        String phone = user.getPhone();
        String idCard = user.getIdCard();
        if (!NullUtil.hasNull(phone, idCard)) {
            user.setPhone(EncryptionUtil.encryptPhone(phone));
            user.setIdCard(EncryptionUtil.encryptIdCard(idCard));
        }
        return user;
    }

    @Override
    public String getVerificationCode(String phone) {

        // 检查手机号是否存在
        User user = userMapper.selectByPhone(phone);
        if (null == user) {
            throw new RuntimeException("手机号不存在");
        }

        // 生成4位随机的验证码
        String verificationCode = BuildUtil.buildVerificationCode(4);

        // 获取一个单机的Jedis连接实例
        Jedis jedis = JedisStandaloneUtil.getJedis();

        // 将验证码存入Redis，时效5分钟
        jedis.setex(phone, 60 * 5, verificationCode);

        // 关闭Jedis连接以节省资源
        JedisStandaloneUtil.closeJedis(jedis);

        // 向指定手机号码发送验证码
        SmsUtil.sendSms(phone, verificationCode);

        // 将验证码响应给客户端
        return verificationCode;
    }

    @Override
    public User loginByPhone(UserLoginByPhoneParam userLoginByPhoneParam) {
        String phone = userLoginByPhoneParam.getPhone();
        String verificationCode = userLoginByPhoneParam.getVerificationCode();

        // 检查必要参数，若必要参数为空则直接抛出异常
        if (NullUtil.hasNull(phone, verificationCode)) {
            throw new RuntimeException("必要参数为空");
        }

        // 获取一个单机的Jedis连接实例
        Jedis jedis = JedisStandaloneUtil.getJedis();

        // 调用 `jedis.get(key)` 判断验证码是否正确，错误直接抛出异常
        String value = jedis.get(phone);
        if (!verificationCode.equals(value)) {
            throw new RuntimeException("验证码错误或已失效");
        }

        // 调用Mapper接口返回手机号对应的用户信息
        User user = userMapper.selectByPhone(phone);

        // 关闭Jedis连接以节省资源
        JedisStandaloneUtil.closeJedis(jedis);

        // 登录成功返回对应的User信息
        return user;
    }



}
