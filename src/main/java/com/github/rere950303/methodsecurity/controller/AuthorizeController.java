package com.github.rere950303.methodsecurity.controller;

import com.github.rere950303.methodsecurity.annotation.PreAuthorize;
import com.github.rere950303.methodsecurity.entity.Entity1;
import com.github.rere950303.methodsecurity.entity.Entity2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizeController {

    @GetMapping("/preauthorize1/{memberId}")
    @PreAuthorize("#entity1.id == #memberId")
    public String preAuthorize1(@RequestBody Entity1 entity1, @PathVariable Long memberId) {
        return "preAuthorize";
    }

    @GetMapping("/preauthorize2/{memberId}")
    @PreAuthorize("#entity2.id == #memberId")
    public String preAuthorize2(@RequestBody Entity2 entity2, @PathVariable Long memberId) {
        return "preAuthorize";
    }
}
