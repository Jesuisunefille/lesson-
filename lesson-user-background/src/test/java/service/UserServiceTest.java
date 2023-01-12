package service;

import com.cindy.UserBackgroundApp;
import com.cindy.param.*;
import com.cindy.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testRegister() {
        UserRegisterParam userRegisterParam = new UserRegisterParam();
        userRegisterParam.setUsername("lucky");
        userRegisterParam.setPassword("lucky");
        userRegisterParam.setRealName("七七");
        userRegisterParam.setIdCard("237700000000000000");
        userRegisterParam.setPhone("18770000000");
        System.out.println(userService.register(userRegisterParam) > 0 ? "成功" : "失败");
    }

    @Test
    public void testLogin() {
        UserLoginParam userLoginParam = new UserLoginParam();
        userLoginParam.setUsername("zhao-si");
        userLoginParam.setPassword("123");
        System.out.println(userService.login(userLoginParam));
    }

    @Test
    public void testSelectById() {
        System.out.println(userService.selectById(1));
        System.out.println(userService.selectById(999));
    }

    @Test
    public void testSelectByPhone(){
        System.out.println(userService.selectByPhone("18770000000"));
    }

    @Test
    public void testUpdateById() {
        UserUpdateParam userUpdateParam = new UserUpdateParam();
        userUpdateParam.setId(1);
        userUpdateParam.setNickName("随机昵称...");
        System.out.println(userService.updateById(userUpdateParam) > 0 ? "用户修改成功" : "用户修改失败");
    }

    @Test
    public void testGetVerificationCode() {
        System.out.println(userService.getVerificationCode("15546128685"));
    }

    @Test
    public void testLoginByPhone() {
        UserLoginByPhoneParam userLoginByPhoneParam = new UserLoginByPhoneParam();
        userLoginByPhoneParam.setPhone("15546128685");
        userLoginByPhoneParam.setVerificationCode("EfiE");
        System.out.println(userService.loginByPhone(userLoginByPhoneParam));
    }

    @Test
    public void testUpdateByUserId() {
        UserUpdateParam userUpdateParam = new UserUpdateParam();
        userUpdateParam.setId(1);
        userUpdateParam.setNickName("随机昵称...");
        System.out.println(userService.updateByUserId(userUpdateParam) > 0 ? "用户修改成功" : "用户修改失败");
    }

    @Test
    public void testUpdatePasswordByUserId() {
        UserUpdatePasswordParam userUpdatePasswordParam = new UserUpdatePasswordParam();
        userUpdatePasswordParam.setId(6);
        userUpdatePasswordParam.setOldPassword("123456");
        userUpdatePasswordParam.setNewPassword("111111");
        System.out.println(userService.updatePasswordByUserId(userUpdatePasswordParam) > 0 ? "成功" : "失败");
    }

    @Test
    public void testDeleteByUserId() {
        UserDeleteParam userDeleteParam = new UserDeleteParam();
        userDeleteParam.setUserId(1);
        System.out.println(userService.deleteByUserId(userDeleteParam) > 0 ? "成功" : "失败");
    }

}
