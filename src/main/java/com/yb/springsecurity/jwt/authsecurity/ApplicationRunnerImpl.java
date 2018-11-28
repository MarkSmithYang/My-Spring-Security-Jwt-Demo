package com.yb.springsecurity.jwt.authsecurity;

import com.alibaba.fastjson.JSONArray;
import com.yb.springsecurity.jwt.model.Module;
import com.yb.springsecurity.jwt.model.Permission;
import com.yb.springsecurity.jwt.repository.ModuleRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author yangbiao
 * @Description:CommandLineRunner,ApplicationRunner
 * 接口是在容器启动成功后的最后一步回调(类似于开机自启动)
 * @date 2018/11/20
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    private static Set<String> SYS_PERMISSIONS = null;
    private static Set<String> SYS_MODULES = null;
    private static JSONArray MODULE_PERMISSIONS = null;

    @Autowired
    private ModuleRepository moduleRepository;

    /**
     * 主要是通过容器启动它也跟着启动这个机制,完成我们想要加载的东西,这里方法参数没有什么用
     * 目前这里还实践怎么好用
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Module> all = moduleRepository.findAll();
        if(CollectionUtils.isNotEmpty(all)){
            all.forEach(s->{
                Set<Permission> permissions = s.getPermissions();
                if(CollectionUtils.isNotEmpty(permissions)){
                    permissions.forEach(a-> System.err.println(s.getModule()+"==="+a.getPermission()));
                }
            });
        }
    }
}
