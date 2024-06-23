package com.chzzk.study.learn_spring_boot.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("name")
public class WelcomeController {

//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    private AuthenticationService authenticationService;
//
//    public LoginController(AuthenticationService authenticationService){
//        super();
//        this.authenticationService = authenticationService;
//    }

//    @RequestMapping("login-test")
//    public String gotoLoginTestPage(@RequestParam String name, ModelMap model) {
//        model.put("name", name);
//
//        logger.debug("Request param is {} by logger", name);
//        logger.info("Request param is {} by logger", name);
//
//        // NOT RECOMMENDED FOR PROD CODE
//        System.out.println("Request param is " + name);
//
//        // 이렇게 하면 @ResponseBody Spring MVC -> 여기 있는 것을 바로 리턴
//        return "loginTest";
//    }

    // login
    // GET, POST 모두 기능하고 있는데 어떻게 이게 GET 요청만 처리하도록 할 수 있을까?
    // 요청 메서드를 추가 파라미터로 넣어주면 됩니다.
    // gotoLoginPage -> gotoWelcomePage 수정
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String gotoWelcomePage(ModelMap model) {
        model.put("name", getLoggedinUsername());
        return "welcome";
    }

    private String getLoggedinUsername() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

//    @RequestMapping(value = "login", method = RequestMethod.POST)
//    public String gotoWelcomePage(@RequestParam String name, @RequestParam String password, ModelMap model) {
//
//        if (authenticationService.authenticate(name, password)) {
//            model.put("name", name);
//            // model.put("password", password);
//
//            return "welcome";
//        }
//
//        model.put("ErrorMessage", "Invalid Credentials! Please try again");
//
//        return "login";
//    }
}
