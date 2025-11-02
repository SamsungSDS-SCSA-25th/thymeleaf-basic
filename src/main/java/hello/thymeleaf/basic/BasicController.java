package hello.thymeleaf.basic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

    /**
     * 이스케이프 vs 언이스케이프
     */

    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("message", "<b>Hello Thymeleaf!</b>"); // Add a message attribute to the model
        return "basic/text-basic";
    }

    @GetMapping("/text-unescaped")
    public String textUnescaped(Model model) {
        model.addAttribute("message", "<b>Hello Thymeleaf!</b>");
        return "basic/text-unescaped";
    }

    /**
     * springEL 표현 + 지역변수
     * 변수를 thymeleaf에서 어떻게 사용하는지?
     */

    @GetMapping("/variable")
    public String variable(Model model) {

        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        // ArrayList
        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        // HashMap
        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        // model에 attribute 전달
        model.addAttribute("user", userA);
        model.addAttribute("userList", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    /**
     * Thymeleaf에서 http(servlet, session), bean 사용하기
     */

    @GetMapping("/basic-objects")
    public String basicObjects(Model model, HttpServletRequest request,
                               HttpServletResponse response, HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());
        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello " + data;
        }
    }

    /**
     * Thymeleaf에서 날짜/시간 유틸리티 사용하기
     */

    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    /**
     * URL 링크 사용하기
     */

    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    /**
     * 리터럴
     */

    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "Thymeleaf!");
        return "basic/literal";
    }

    /**
     * 연산
     */

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring!");
        return "basic/operation";
    }

    @Data
    @AllArgsConstructor
    static class User {
        private String username;
        private int age;
    }

}
