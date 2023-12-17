/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Service.ApiService;
import Dao.HiibernateDao;
import Model.SceneConsomation;
import Util.VariableAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author TANJONA fetrasoa
 */
public class SceneConsomationService {

    private HiibernateDao dao = new HiibernateDao();

    public void getdata() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable maTache = () -> {
            String resultat = ApiService.getAPI(VariableAPI.ConsomationDeviceUrl);
            String resultat1 = ApiService.getAPI(VariableAPI.statisticDeviceUrl);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(resultat).getAsJsonObject();
            double actualCurrent = jsonObject.getAsJsonObject("result").getAsJsonObject("status").getAsJsonPrimitive("actual_current").getAsDouble();
            jsonObject = parser.parse(resultat1).getAsJsonObject();
            String actualkwh = jsonObject.getAsJsonObject("result").getAsJsonPrimitive("202312").getAsString();

            System.out.println(actualCurrent + " et " + actualkwh);

            ArrayList<Criterion> conditions = new ArrayList<>();
            conditions.add(Restrictions.eq("etat", 0));
            List<SceneConsomation> listScene = dao.getAllByCondition(new SceneConsomation(), conditions);
            if (listScene.size() > 0) {
                for (SceneConsomation sceneConsomation : listScene) {
                    double doubleNumber = Double.parseDouble(actualkwh);
                    if (sceneConsomation.getMaxKwh() <= doubleNumber) {
                        System.out.println("sceneConsomation.getMaxKwh()===" + sceneConsomation.getMaxKwh() + " doubleNumber=====" + doubleNumber);
                        ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.offDevice);
                        sceneConsomation.setEtat(1);
                        dao.update(sceneConsomation);

                    } 
                }
            }

        };

        // Planifier la tâche avec un délai initial de 0 secondes et une période de 5 secondes
        scheduler.scheduleAtFixedRate(maTache, 0, 10, TimeUnit.SECONDS);

    }
}
