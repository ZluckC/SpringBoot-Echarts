/**
 * @AUTHOR:黄中才
 * @TIME:2024/06/06：10:37
 * @PROJECT_NAME:hzc
 */

package com.gznu.hzc.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String userAccount;
    private String userPassword;
}
