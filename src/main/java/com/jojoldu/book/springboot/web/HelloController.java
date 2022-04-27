package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController : 컨트롤러를 Json을 반환하는 컨트롤러로 변환, 예전 @Requestbody를 각 메소드마다 선언했던 것을 한번에 사용할 수 있게 해줌
@RestController
public class HelloController {
    // @GetMapping : Http Method인 Get의 요청을 받을 수 있는 API를 만들어 줌, @RequestMapping으로 사용되었던 것을 대신 사용.
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount){
        return new HelloResponseDto(name, amount);
    }
}
