/**
 * @AUTHOR:黄中才
 * @TIME:2024/06/05：23:43
 * @PROJECT_NAME:hzc
 */

package com.gznu.hzc.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 个人简介
     */
    private String userInfo;
    /**
     * 用户角色
     */
    private int role;
    /**
     * 星球编号
     */
    private int code;

    /**
     * 创建时间
     */
    private Date createTime;


}
