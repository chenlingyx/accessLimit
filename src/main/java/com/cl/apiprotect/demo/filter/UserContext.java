package com.cl.apiprotect.demo.filter;

import com.cl.apiprotect.demo.domain.eo.MiaoshaUser;

/***
 * @ClassName: UserContext
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 11:15
 * @version : V1.0.0
 */
public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }
}
