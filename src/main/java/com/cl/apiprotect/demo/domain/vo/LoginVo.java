package com.cl.apiprotect.demo.domain.vo;

import com.cl.apiprotect.demo.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/***
 * @ClassName: LoginVo
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:13
 * @version : V1.0.0
 */
@Data
public class LoginVo {


    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;
}
