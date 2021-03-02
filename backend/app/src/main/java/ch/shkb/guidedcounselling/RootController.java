package ch.shkb.guidedcounselling;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class RootController {

    @GetMapping
    public String getRootMessage(){
        return "Guided Counselling Backend";
    }

}
