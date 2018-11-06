package com.judge.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judge.demo.Service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/judge/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JSONObject Login(@RequestParam String account,@RequestParam String password){
        JSONObject object = new JSONObject();
        if(userService.Login(account,password))
            object.put("code",1);
        else
            object.put("code",0);
        return object;
    }
}
