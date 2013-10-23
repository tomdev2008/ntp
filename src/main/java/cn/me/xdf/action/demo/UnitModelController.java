package cn.me.xdf.action.demo;

import cn.me.xdf.model.demo.UnitModel;
import cn.me.xdf.service.base.AttMainService;
import cn.me.xdf.service.demo.UnitModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-23
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/demo/unit")
public class UnitModelController {

    @Autowired
    private UnitModelService unitModelService;

    @Autowired
    private AttMainService attMainService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String init() {
        return "/demo/unit/index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(UnitModel unitModel) {
        unitModelService.save(unitModel);
        attMainService.saveAttMainMachine(unitModel);
        List<String> attIds = unitModel.getAttIds();
        if (attIds != null) {
            for (String attId : attIds) {
                System.out.println("attId===" + attId);
            }
        }
        return "redirect:/demo/unit/index";
    }
}
