package com.gznu.hzc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gznu.hzc.model.domain.User;
import com.gznu.hzc.model.request.SignInRequest;
import com.gznu.hzc.model.request.UserLoginRequest;
import com.gznu.hzc.model.vo.UserVo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 黄中才
* @description 针对表【user】的数据库操作Service
* @createDate 2024-06-05 19:43:56
*/
public interface UserService extends IService<User> {
    /**
     * 注册
     * @param signInRequest 注册封装类
     * @return 用户id
     */
    Long signIn(SignInRequest signInRequest);

    /**
     * 用户登录
     * @param userLoginRequest 请求封装类
     * @param request 请求
     * @return 用户信息
     */
    UserVo userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user 未脱敏用户
     * @return 脱敏后的用户信息
     */
    UserVo cleanUser(User user);

    /**
     * 通过昵称查询
     * @param username 用户昵称
     * @return 返回用户列表
     */
    List<UserVo> searchUsers(String username);
}
