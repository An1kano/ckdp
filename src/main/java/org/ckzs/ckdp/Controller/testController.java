package org.ckzs.ckdp.Controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class testController {
    @Operation(summary = "测试Token是否可用")
    @PostMapping("/tokenTest")
    public String test(){
        return "test";
    }
}
