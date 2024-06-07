/**
 * @AUTHOR:黄中才
 * @TIME:2024/06/05：22:05
 * @PROJECT_NAME:hzc
 */

package com.gznu.hzc.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gznu.hzc.model.domain.User;
import com.gznu.hzc.model.request.SignInRequest;
import com.gznu.hzc.model.request.UserLoginRequest;
import com.gznu.hzc.model.vo.UserVo;
import com.gznu.hzc.service.UserService;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gznu.hzc.constant.UserConstant.ADMIN;
import static com.gznu.hzc.constant.UserConstant.USER_LOGIN_STATUS;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/sign")
    public Long userSigintIn(@RequestBody SignInRequest signInRequest) {
        if (signInRequest == null) {
            return null;
        }
        return userService.signIn(signInRequest);
    }

    @PostMapping("/login")
    public UserVo userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (BeanUtil.isEmpty(userLoginRequest)) {
            return null;
        }
        return userService.userLogin(userLoginRequest, request);
    }

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request) {
        if (BeanUtil.isEmpty(request)) {
            return null;
        }
        return userService.logout(request);
    }

    @GetMapping("/search")
    public List<UserVo> searchUsers(String username, HttpServletRequest request) {
        Boolean isAdmin = isAdmin(request);
        if (!isAdmin) {
            return new ArrayList<>();
        }
        //TODO 测试流式处理
        return userService.searchUsers(username).stream().map(userVo -> {
            userVo.setUsername("Hhzc");
            return userVo;
        }).collect(Collectors.toList());
    }

//    TODO 搜索优化
    @GetMapping("/search/all")
    public List<UserVo> searchUserList(HttpServletRequest request) {
        Boolean isAdmin = isAdmin(request);
        if (!isAdmin) {
            return new ArrayList<>();
        }
        List<User> userList = userService.list();
        List<UserVo> userVoList = new ArrayList<>();
        userList.forEach(user -> {
            userVoList.add(userService.cleanUser(user));
        });
        return userVoList;
    }


    @DeleteMapping("/delete")
    public Boolean deleteUser(Long id, HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        Boolean isAdmin = isAdmin(request);
        if (!isAdmin) {
            return null;
        }
        return userService.removeById(queryWrapper);
    }

    @GetMapping("/current")
    public UserVo getCurrentUser(HttpServletRequest request) {
        if (BeanUtil.isEmpty(request)) {
            return null;
        }
        Boolean isAdmin = isAdmin(request);
        if (!isAdmin) {
            return null;
        }
        UserVo currentUserVo = userService.getCurrent(request);
        if (BeanUtil.isEmpty(currentUserVo)) {
            return null;
        }
        return currentUserVo;
    }

    public Boolean isAdmin(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObject;
        if (BeanUtil.isEmpty(user) || user.getRole() != ADMIN) {
            return false;
        }
        return true;
    }

}
