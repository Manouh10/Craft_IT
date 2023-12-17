/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Cors.CorsFilter;
import Dao.HiibernateDao;
import Model.SceneConsomation;
import Service.SceneTempsService;
import Model.SceneTemps;
import Service.ApiService;
import Util.VariableAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author TANJONA fetrasoa
 */
@RestController
@RequestMapping(value = "/scene")
public class SceneCtrl {
    
    private CorsFilter filtre = new CorsFilter();
    private HiibernateDao dao = new HiibernateDao();

    /**
     *
     * @param temps
     * @param serie
     * @param action
     * @param type
     * @param response
     * @return
     */
    @RequestMapping(value = "/creer", method = RequestMethod.GET)
    public String saveScene(@RequestParam("temps") String temps, @RequestParam("serie") String serie, @RequestParam("action") String action, @RequestParam("type") String type, HttpServletResponse response) {
        filtre.doFilterInternal(response);
        Map<String, Object> map = new HashMap<>();
        try {
            SceneTemps s = new SceneTemps(temps, serie, action, type);
            dao.save(s);
            map.put("resultat", "ok");
            SceneTempsService reveil = new SceneTempsService();
            reveil.setReveil();
            String res = "ok";
            return "{\"resultat\":\"" + res + "\"}";
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            String res = "ok";
            return "{\"resultat\":\"" + res + "\"}";
        }
    }

    /**
     *
     * @param serie
     * @param action
     * @param kwh
     * @param amp
     * @param response
     * @return
     */
    @RequestMapping(value = "/consommation/creer", method = RequestMethod.GET)
    public String consomationScene(@RequestParam("serie") String serie, @RequestParam("action") String action, @RequestParam("maxKwh") double kwh, @RequestParam("maxA") double amp, HttpServletResponse response) {
        filtre.doFilterInternal(response);
        Map<String, Object> map = new HashMap<>();
        try {
            SceneConsomation s = new SceneConsomation(action, serie, kwh, amp, 0);
            List<SceneConsomation> liste = dao.getAll(new SceneConsomation());
           
            if (liste.size() > 0) {
                for (SceneConsomation sceneConsomation : liste) {
                    sceneConsomation.setMaxKwh(kwh);
                    sceneConsomation.setEtat(0);  
                    dao.update(sceneConsomation);
                    if (sceneConsomation.getMaxKwh()<=kwh) {
                         ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.onDevice);
                    }else{
                         ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.offDevice);
                    }
                }
            } else {
                dao.save(s);
                  ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.onDevice);
               
            }
            SceneTempsService reveil = new SceneTempsService();
            reveil.setReveil();
            String res = "ok";
            return "{\"resultat\":\"" + res + "\"}";
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            String res = "ok";
            return "{\"resultat\":\"" + res + "\"}";
        }
    }

    /**
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/consommation/getMaxkwh", method = RequestMethod.GET)
    public String getMaxkwhconsomationScene(HttpServletResponse response) {
        filtre.doFilterInternal(response);
        Map<String, Object> map = new HashMap<>();
        double maxKwh = 0;
        try {
            
            List<SceneConsomation> liste = dao.getAll(new SceneConsomation());
            if (liste.size() > 0) {
                for (SceneConsomation sceneConsomation : liste) {
                    sceneConsomation.setEtat(0);
                    maxKwh = sceneConsomation.getMaxKwh();
                }
            }
            
            return "{\"resultat\":\"" + maxKwh + "\"}";
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            String res = "ok";
            return "{\"resultat\":\"" + maxKwh + "\"}";
        }
    }

    /**
     *
     * @param serie
     * @param response
     * @return
     */
    @RequestMapping(value = "/lister", method = RequestMethod.GET)
    public List listerScene(@RequestParam("serie") String serie, HttpServletResponse response) {
        filtre.doFilterInternal(response);
        Map<String, Object> map = new HashMap<>();
        try {
            ArrayList<Criterion> conditions = new ArrayList<>();
            conditions.add(Restrictions.eq("serie", serie));
            
            return dao.getAllByCondition(new SceneTemps(), conditions);
        } catch (Exception e) {
            map.put("resultat", "ko");
            return null;
        }
    }

    /**
     *
     * @param serie
     * @param response
     * @return
     */
    @RequestMapping(value = "/consommation/lister", method = RequestMethod.GET)
    public List lconsommationListerScene(@RequestParam("serie") String serie, HttpServletResponse response) {
        filtre.doFilterInternal(response);
        Map<String, Object> map = new HashMap<>();
        try {
            ArrayList<Criterion> conditions = new ArrayList<>();
            conditions.add(Restrictions.eq("serie", serie));
            
            return dao.getAllByCondition(new SceneTemps(), conditions);
        } catch (Exception e) {
            map.put("resultat", "ko");
            return null;
        }
    }

    /**
     *
     * @param serie
     * @return
     */
    @RequestMapping(value = "/supprimer", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> deleteScene(@RequestParam("serie") String serie) {
        Map<String, Object> map = new HashMap<>();
        try {
            ArrayList<Criterion> conditions = new ArrayList<>();
            conditions.add(Restrictions.eq("serie", serie));
            List<SceneTemps> l = dao.getAllByCondition(new SceneTemps(), conditions);
            if (l.size() > 0) {
                SceneTemps s = l.get(0);
                dao.delete(s);
            }
            
            map.put("resultat", "ok");
            SceneTempsService reveil = new SceneTempsService();
            reveil.setReveil();
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("resultat", "ko");
            return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
        }
    }
}
