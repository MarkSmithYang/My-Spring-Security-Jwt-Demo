package com.yb.springsecurity.jwt.controller;

import com.yb.springsecurity.jwt.common.ResultInfo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangbiao
 * @Description:控制层代码
 * @date 2018/11/19
 */
@RestController
@CrossOrigin//处理跨域
public class SecurityJwtController {

    @GetMapping("/yes")
    public ResultInfo<List<String>> yes(){
        return ResultInfo.success(new ArrayList<String>(){{add("yes");}});
    }

    @GetMapping("/hello")
    public ResultInfo<List<String>> hello(){
        return ResultInfo.success(new ArrayList<String>(){{add("hello");}});
    }

    @GetMapping("/world")
    public ResultInfo<List<String>> world(){
        return ResultInfo.success(new ArrayList<String>(){{add("world");}});
    }

    @GetMapping("/users")
    public ResultInfo<List<String>> users(){
        return ResultInfo.success(new ArrayList<String>(){{add("rose");add("jack");add("mark");}});
    }
}
