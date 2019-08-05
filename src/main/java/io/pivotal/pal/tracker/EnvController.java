package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String port;
    private String memory;
    private String instance;
    private String address;
    public EnvController(@Value("${PORT:NOT SET}") String port,
                         @Value("${MEMORY_LIMIT:NOT SET}") String memory,
                         @Value("${CF_INSTANCE_INDEX:NOT SET}") String instance,
                         @Value("${CF_INSTANCE_ADDR:NOT SET}") String address){
        this.port = port;
        this.memory = memory;
        this.instance = instance;
        this.address = address;
    }

    @GetMapping("/env")
    public Map<String,String> getEnv(){
        Map<String,String> env = new HashMap<>();
        env.put("PORT",this.port);
        env.put("MEMORY_LIMIT",this.memory);
        env.put("CF_INSTANCE_INDEX",this.instance);
        env.put("CF_INSTANCE_ADDR",this.address);
        return env;
    }
}
