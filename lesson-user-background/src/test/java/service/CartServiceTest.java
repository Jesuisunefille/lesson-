package service;

import com.cindy.UserBackgroundApp;
import com.cindy.param.CartClearParam;
import com.cindy.param.CartDeleteParam;
import com.cindy.param.CartInsertParam;
import com.cindy.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserBackgroundApp.class)
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    public void testInsertOrUpdate() {
        CartInsertParam cartInsertParam_01 = new CartInsertParam(1, 1, "1号视频", "1号封面", "1号作者", 111.11);
        CartInsertParam cartInsertParam_02 = new CartInsertParam(1, 2, "2号视频", "2号封面", "2号作者", 222.11);
        CartInsertParam cartInsertParam_03 = new CartInsertParam(1, 3, "3号视频", "3号封面", "3号作者", 333.11);
        System.out.println(cartService.insertOrUpdate(cartInsertParam_01) > -1 ? "添加或更新成功" : "添加失败");
        System.out.println(cartService.insertOrUpdate(cartInsertParam_02) > -1 ? "添加或更新成功" : "添加失败");
        System.out.println(cartService.insertOrUpdate(cartInsertParam_03) > -1 ? "添加或更新成功" : "添加失败");
    }

    @Test
    public void testSelectByUserId() {
        Map<String, String> result = cartService.selectByUserId(1);
        result.entrySet().forEach(System.out::println);
    }

    @Test
    public void testDeleteByUserIdAndVideoIds() {
        CartDeleteParam cartDeleteParam = new CartDeleteParam();
        cartDeleteParam.setUserId(1);
        cartDeleteParam.setVideoIds(new Integer[]{2, 3});
        System.out.println(cartService.deleteByUserIdAndVideoIds(cartDeleteParam) > 0 ? "删除成功" : "删除失败");
    }

    @Test
    public void testDeleteByUserId() {
        CartClearParam cartClearParam = new CartClearParam();
        cartClearParam.setUserId(1);
        System.out.println(cartService.deleteByUserId(cartClearParam) > -1 ? "清空成功" : "key不存在或清空失败");
    }

}
