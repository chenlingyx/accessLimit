package com.cl.apiprotect.demo.controller;

import com.cl.apiprotect.demo.domain.eo.User;
import com.cl.apiprotect.demo.rabbitmq.MQSender;
import com.cl.apiprotect.demo.redis.RedisService;
import com.cl.apiprotect.demo.redis.UserKey;
import com.cl.apiprotect.demo.result.CodeMsg;
import com.cl.apiprotect.demo.result.Result;
import com.cl.apiprotect.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private UserService userService;

    @Autowired
    private  RedisService redisService;

    @Autowired
    private  MQSender sender;

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> home() {
        return Result.success("Hello，world");
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/hello/themaleaf")
    public String themaleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("1111");
        //UserKey:id1
        redisService.set(UserKey.getById, "" + 1, user);
        return Result.success(true);
    }

}
