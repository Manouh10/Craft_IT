/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;


import Dao.HiibernateDao;
import Model.Consomation;
import Util.VariableAPI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author TANJONA fetrasoa
 */
public class ConsomationService {

    private HiibernateDao dao = new HiibernateDao();

    public void getdata() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable maTache = () -> {
            String resultat = ApiService.getAPI(VariableAPI.ConsomationDeviceUrl);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(resultat).getAsJsonObject();

            String online = jsonObject.getAsJsonObject("result").getAsJsonPrimitive("online").getAsString();
            String switchs = jsonObject.getAsJsonObject("result").getAsJsonObject("status").getAsJsonPrimitive("switch").getAsString();
            double actuelPower = jsonObject.getAsJsonObject("result").getAsJsonObject("status").getAsJsonPrimitive("actual_power").getAsDouble();
            double actualCurrent = jsonObject.getAsJsonObject("result").getAsJsonObject("status").getAsJsonPrimitive("actual_current").getAsDouble();

            if ((int) actuelPower > 0) {
                Consomation consomation = new Consomation(online, switchs, actuelPower, actualCurrent);
                int res = dao.save(consomation);
            }
        };

        // Planifier la tâche avec un délai initial de 0 secondes et une période de 5 secondes
        scheduler.scheduleAtFixedRate(maTache, 0, 1, TimeUnit.MINUTES);

    }
}
