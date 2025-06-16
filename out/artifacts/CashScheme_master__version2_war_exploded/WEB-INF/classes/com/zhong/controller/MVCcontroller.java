package com.zhong.controller;


import com.zhong.SearchProtocol_Split_memory;
import com.zhong.Search_Client_1_Output;
import com.zhong.t_item;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import it.unisa.dia.gas.jpbc.Pairing;
import javax.jws.WebParam;
import java.util.ArrayList;

@Controller
public class MVCcontroller {
    @RequestMapping("/singlesearch")
    public String SingleSearch(String w,Model model) throws Exception {

        ArrayList<t_item> ress=SearchProtocol_Split_memory.SingleSearchTest_get(w);
        model.addAttribute("res",ress);
        return "result";
    }
    @RequestMapping("/ss")
    public String ss(Model model) throws Exception {
        ArrayList<t_item> ress=SearchProtocol_Split_memory.SingleSearchTest_get("clubnumber11");
        model.addAttribute("res",ress);
        return "result";
    }
    @RequestMapping("/tt")
    public String test(Model model){
        Pairing p= PairingFactory.getPairing("com/zhong/params/curves/a.properties");
        model.addAttribute("res",p);
        return "result";
    }

}
