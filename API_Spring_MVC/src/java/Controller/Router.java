package Controller;

import Service.ConsomationService;
import Service.SceneConsomationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author TANJONA fetrasoa
 */
@RestController
public class Router {
    /**
     * 
     * @return 
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
  public ModelAndView page(){
      SceneConsomationService pc=new SceneConsomationService();
      pc.getdata();
      return new ModelAndView("index");
  }  
}
