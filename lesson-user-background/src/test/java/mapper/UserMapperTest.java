package mapper;

import com.cindy.UserBackgroundApp;
import com.cindy.entity.User;
import com.cindy.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user = new User();
        user.setUsername("赵四");
        user.setPassword("123");
        user.setRealName("赵国强");
        user.setNickName("亚洲舞王");
        user.setGender(1);
        user.setAge(58);
        user.setIdCard("230107199999990210");
        user.setAvatar("default-user-avatar.jpeg");
        user.setPhone("13833333333");
        user.setInfo("是一个会跳舞的人");
        user.setCreateTime(new Date());
        user.setLastModify(new Date());
        System.out.println(userMapper.insert(user) > 0 ? "成功" : "失败");
        System.out.println(user);
    }

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(1);
        user.setPassword("123");
        user.setNickName("亚洲舞王");
        user.setGender(1);
        user.setAge(58);
        user.setIdCard("230107199999990210");
        user.setAvatar("default-user-avatar.jpeg");
        user.setPhone("13833333333");
        user.setInfo("是一个会跳舞的人");
        System.out.println(userMapper.updateById(user) > 0 ? "成功" : "失败");
    }

    @Test
    public void testSelectById() {
        System.out.println(userMapper.selectById(2));
    }

    @Test
    public void testSelectByUsernameAndPassword() {
        System.out.println(userMapper.selectByUsernameAndPassword("1", "2"));
    }

    @Test
    public void testDeleteById() {
        System.out.println(userMapper.deleteById(2) > 0 ? "成功" : "失败");
    }
}
