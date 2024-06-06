/**
 * @AUTHOR:黄中才
 * @TIME:2024/06/05：21:00
 * @PROJECT_NAME:hzc
 */

package com.gznu.hzc.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SignInRequest implements Serializable {
    private String userAccount;
    private String userPassword;
    private String confirmPassword;
    private int planetNumber;

}
