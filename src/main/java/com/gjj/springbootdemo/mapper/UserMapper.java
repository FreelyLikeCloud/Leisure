package com.gjj.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gjj.springbootdemo.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Lord
* @description 针对表【users】的数据库操作Mapper
* @createDate 2024-03-26 16:06:10
* @Entity generator.domain.Users
*/
public interface UserMapper extends BaseMapper<User> {
    //sql->登录查询
    @Select("SELECT * FROM user WHERE account=#{account}")
    User login(@Param("account") String account);

    //sql->查询所有数据（管理员）
    @Select("SELECT * FROM user WHERE (IFNULL(account,'') LIKE CONCAT('%',#{account},'%')) LIMIT #{pageNum},#{pageSize}")
    List<User> selectUserLimit(@Param("account") String account, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    //sql->根据用户账号查询数据（用户）（前台）
    @Select("SELECT * FROM user WHERE account=#{account}")
    User selectUserByAccount(@Param("account") String account);

    //sql->根据用户 ID 查询数据（用户）
//    @Select("SELECT * FROM Users WHERE user_id=#{userId}")
//    Users selectUserById(@Param("userId") Integer userId);

    //sql->增加用户
    @Insert("INSERT INTO Users(account,password,avatar,name,role,phone,purse)" +
            "VALUES (#{account},#{password},#{avatar},#{name},#{role},#{phone},#{purse})")
    Boolean insertUsers(User user);

    //sql->更新用户钱包余额
    @Update("UPDATE user SET purse=#{purse} WHERE uid=#{userId}")
    Boolean updateUserPurse(@Param("purse") BigDecimal purse,@Param("userId") Integer userId);

    //sql->更新用户信息
//    @Update("UPDATE Users SET account=#{account},password=#{password},avatar=#{avatar},name=#{name},role=#{role},phone=#{phone},purse=#{purse} WHERE user_id=#{userId}")
//    Boolean updateUser(Users users);
    //mybatis的xml方式用sql->修改信息

    //sql->删除
    @Delete("DELETE FROM Users WHERE fav_id=#{favId}")
    Integer deleteFavorites(Integer favId);

    //sql->获取查询总条数
    @Select("SELECT COUNT(*) FROM user WHERE (IFNULL(name,'') LIKE CONCAT('%',#{name},'%'))")
    Integer userTotal(@Param("name") String name);

    //xml->批量删除
    Integer deleteBatchById(List<Integer> userIdList);
}




