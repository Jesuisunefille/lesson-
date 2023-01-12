package com.cindy.mapper;

import com.cindy.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cindy
 */
@Repository
public interface UserMapper {

    String SELECT_ALL = "SELECT u.id, u.username, u.password, u.real_name, u.nick_name, u.gender, " +
            "u.age, u.id_card, u.avatar, u.phone, u.info, u.create_time, u.last_modify " +
            "FROM lesson.user u ";

    /**
     * 单增User记录
     *
     * @param user 用户实体
     * @return 影响条目数
     */
    @Insert("INSERT INTO lesson.user (username, password, real_name, nick_name, gender, " +
            "age, id_card, avatar, phone, info, create_time, last_modify) " +
            "VALUES (#{username}, #{password}, #{realName}, #{nickName}, #{gender}, " +
            "#{age}, #{idCard}, #{avatar}, #{phone}, #{info}, #{createTime}, #{lastModify})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * <h2>按主键单改User记录</h2>
     * <p>其中ID，USERNAME和CREATE_TIME字段不允许修改
     * <p>其中REAL_NAME和ID_CARD在单独业务中进行修改
     * <p>其中PASSWORD在单独业务中进行修改
     * <p>其中PHONE在单独业务中进行修改
     *
     * @param user 用户实体
     * @return 影响条目数
     */
    @Update("<script>" +
            "UPDATE lesson.user" +
            "<set> " +
            "<if test='password != null'> password = #{password}, </if>" +
            "<if test='nickName != null'> nick_name = #{nickName}, </if>" +
            "<if test='gender != null'> gender = #{gender}, </if>" +
            "<if test='phone != null'> phone = #{phone}, </if>" +
            "<if test='age != null'> age = #{age}, </if>" +
            "<if test='avatar != null'> avatar = #{avatar}, </if>" +
            "<if test='info != null'> info = #{info}, </if>" +
            "last_modify = now()" +
            "</set>" +
            "<where>" +
            "<if test='id != null'> id = #{id} </if> OR 1 = 2" +
            "</where>" +
            "</script>")
    int updateById(User user);

    /**
     * 按主键单删User记录
     *
     * @param id User表主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.user" +
            "<where>" +
            "<if test='_parameter != null'> id = #{param1} </if> " +
            "OR 1 = 2 " +
            "</where>" +
            "</script>")
    int deleteById(Integer id);

    /**
     * 按主键单查User记录
     *
     * @param id User表主键
     * @return 单条User记录
     */
    @Select("<script>" +
            SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> u.id = #{param1} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    User selectById(Integer id);

    /**
     * 全查User记录
     *
     * @return 全部User记录
     */
    @Select(SELECT_ALL)
    List<User> list();

    /**
     * 按账号密码单查User记录
     *
     * @param username 登录账号
     * @param password 登录密码
     * @return 单条User记录
     */
    @Select("<script>" +
            SELECT_ALL +
            "<where>" +
            "<if test='arg0 != null and arg1 != null'> u.username = #{param1} AND u.password = #{param2} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    User selectByUsernameAndPassword(String username, String password);

    /**
     * 按手机号码单查User记录
     *
     * @param phone 手机号码
     * @return 单条User记录
     */
    @Select("<script>" +
            SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> u.phone = #{param1} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    User selectByPhone(String phone);


    /**
     * <h2>按主键单改User记录</h2>
     * <p>其中ID，USERNAME和CREATE_TIME字段不允许修改
     * <p>其中REAL_NAME和ID_CARD在单独业务中进行修改
     * <p>其中PASSWORD在单独业务中进行修改
     * <p>其中PHONE在单独业务中进行修改
     *
     * @param user 用户实体
     * @return 影响条目数
     */
    @Update("<script>" +
            "UPDATE lesson.user" +
            "<set> " +
            "<if test='password != null'> password = #{password}, </if>" +
            "<if test='nickName != null'> nick_name = #{nickName}, </if>" +
            "<if test='gender != null'> gender = #{gender}, </if>" +
            "<if test='phone != null'> phone = #{phone}, </if>" +
            "<if test='age != null'> age = #{age}, </if>" +
            "<if test='avatar != null'> avatar = #{avatar}, </if>" +
            "<if test='info != null'> info = #{info}, </if>" +
            "last_modify = now()" +
            "</set>" +
            "<where>" +
            "<if test='id != null'> id = #{id} </if> OR 1 = 2" +
            "</where>" +
            "</script>")
    int updateByUserId(User user);


    /**
     * 按用户主键单删User记录
     *
     * @param userId User表主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.user" +
            "<where>" +
            "<if test='_parameter != null'> id = #{param1} </if> " +
            "OR 1 = 2 " +
            "</where>" +
            "</script>")
    int deleteByUserId(Integer userId);
}
