package com.cl.apiprotect.demo.controller;

import com.cl.apiprotect.demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * @ClassName: AccessLimitController
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/13 23:39
 * @version : V1.0.0
 */
@Controller
public class AccessLimitController {


    //@AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping("/accessLimit")
    @ResponseBody
    public Result<String> fangshua(){
        return Result.success("请求成功");
    }
}
