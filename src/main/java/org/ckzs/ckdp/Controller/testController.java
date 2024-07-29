package org.ckzs.ckdp.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class testController {
    @PostMapping("/tokenTest")
    public String test(){
        return "test";
    }
}
