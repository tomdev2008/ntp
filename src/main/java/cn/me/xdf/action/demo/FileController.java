package cn.me.xdf.action.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-22
 * Time: 下午1:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/demo/file")
public class FileController {


    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public void init() {

    }

}
