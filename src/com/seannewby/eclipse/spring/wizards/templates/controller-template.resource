package ${myControllerPackage};

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("VIEW")
public class IndexController {
	
	@RequestMapping
	public String showIndex(){
		return "index";
	}

}	