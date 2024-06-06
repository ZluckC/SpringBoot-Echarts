package com.gznu.hzc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gznu.hzc.mapper.UserMapper;
import com.gznu.hzc.model.domain.User;
import com.gznu.hzc.model.request.SignInRequest;
import com.gznu.hzc.model.request.UserLoginRequest;
import com.gznu.hzc.model.vo.UserVo;
import com.gznu.hzc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gznu.hzc.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @author 黄中才
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-06-05 19:43:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值
     */
    private static final String SALT = "HZC";


    @Resource
    private UserMapper userMapper;

    @Override
    public Long signIn(SignInRequest signInRequest) {
        User user = new User();
        if (BeanUtil.isEmpty(signInRequest)) {
            return null;
        } else {
            //账号不小于4位
            if (signInRequest.getUserAccount().length() < 4) {
                return null;
            }
            //密码不小于6位
            if (signInRequest.getUserPassword().length() < 6) {
                return null;
            }

            //账号名不能包含特殊字符
            String pattern = "^[a-z0-9]+$";
            String testString = signInRequest.getUserAccount();
            Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = compiledPattern.matcher(testString);

            if (!matcher.matches()) {
                throw new RuntimeException("账户包含特殊字符");
            }
            //密码和确认密码相同
            if (!signInRequest.getUserPassword().equals(signInRequest.getConfirmPassword())) {
                return null;
            }
            //密码采用加密算法
            String newPassword = DigestUtils.md5DigestAsHex((SALT + signInRequest.getUserPassword()).getBytes());

            user.setUserAccount(signInRequest.getUserAccount());
            user.setUserPassword(signInRequest.getUserPassword());
            user.setCode(signInRequest.getPlanetNumber());
            user.setUserPassword(newPassword);

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", signInRequest.getUserAccount());
            Long count = userMapper.selectCount(queryWrapper);
            if (count >= 1) {
                throw new RuntimeException("账户名已重复");
            }
        }
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return null;
        }
        return user.getId();
    }

    @Override
    public UserVo userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 1.判空
        if (BeanUtil.isEmpty(userLoginRequest)) {
            throw new InvalidParameterException("参数为空");
        }
        // 2.账号不能包含特殊字符
        String pattern = "^[a-z0-9]+$";
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(userLoginRequest.getUserAccount());
        if (!matcher.matches()) {
            throw new InvalidParameterException("账户包含特殊字符");
        }

        // 3.校验密码输入是否正确（查询数据库也要用加密后的密码进行查询）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userLoginRequest.getUserPassword()).getBytes());
        queryWrapper.eq("userAccount", userLoginRequest.getUserAccount()).eq("userPassword", newPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (BeanUtil.isEmpty(user)) {
            throw new RuntimeException("账号不存在");
        }

        // 4.返回用户信息（脱敏）
        UserVo cleanUserVo = this.cleanUser(user);

        // 5.记录用户登录状态，存储在服务器上
        request.getSession().setAttribute(USER_LOGIN_STATUS, user);

        return cleanUserVo;
    }

    @Override
    public UserVo cleanUser(User user) {
        if (BeanUtil.isEmpty(user)) {
            throw new RuntimeException("user is null");
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public List<UserVo> searchUsers(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        List<User> userList = this.list(queryWrapper);
        List<UserVo> userVoList = new ArrayList<>();
        userList.forEach(user -> {
            UserVo userVo = cleanUser(user);
            userVoList.add(userVo);
        });
        if (BeanUtil.isEmpty(userVoList)) {
            return null;
        }
        return userVoList;
    }
}




