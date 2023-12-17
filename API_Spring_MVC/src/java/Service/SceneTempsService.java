/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;


import Dao.HiibernateDao;
import Model.SceneTemps;
import Model.Temps;
import Util.VariableAPI;
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
public class SceneTempsService {

//    private ApiService apiService = new ApiService();
    private HiibernateDao dao = new HiibernateDao();

    public void setReveil() {
        List<Temps> listeT = new ArrayList<>();
        List<SceneTemps> liste = dao.getAll(new SceneTemps());
        for (SceneTemps scene : liste) {
            if (scene.getType().equals("timer")) {
                if (scene.getAction().equals("on")) {
                    ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.onDevice);
                    scene.setAction("off");
                    dao.update(scene);
                } else {
                    ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.offDevice);
                    scene.setAction("on");
                    dao.update(scene);
                }
            }
            System.out.println(scene.getTemps());
            String[] temps = scene.getTemps().split(":");
            Temps t = new Temps(Integer.valueOf(temps[0]), Integer.valueOf(temps[1]), Integer.valueOf(temps[2]), scene.getTemps());
            listeT.add(t);
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        for (Temps temp : listeT) {
            System.out.println("t=" + temp.getHoraire());
            long initialDelay = calculerDelai(temp.getHeure(), temp.getMinute(), temp.getSeconde());
            service.scheduleAtFixedRate(new TacheDuJour(temp.getHoraire()), initialDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
        }

    }

    private static long calculerDelai(int heure, int minute, int seconde) {
        long tempsActuel = System.currentTimeMillis();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, heure);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, seconde);
        if (calendar.getTimeInMillis() < tempsActuel) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }
        return calendar.getTimeInMillis() - tempsActuel;
    }

    static class TacheDuJour implements Runnable {

        String temps;

        public TacheDuJour(String t) {
            System.out.println("constructeur=" + t);
            this.temps = t;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println("qskdlmshdklqhlfhqslf");
                ArrayList<Criterion> conditions = new ArrayList<>();
                conditions.add(Restrictions.eq("temps", temps));
                HiibernateDao daoo = new HiibernateDao();
                List<SceneTemps> listeSceneTemps = daoo.getAllByCondition(new SceneTemps(), conditions);
                if (listeSceneTemps.size() > 0) {
                    for (SceneTemps scene : listeSceneTemps) {
                        if (scene.getAction().equals("on")) {
                            ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.onDevice);
                        } else {

                            ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.offDevice);

                        }
                        if (scene.getType().equals("timer")) {
                            daoo.delete(scene);
                        }
                    }
                }

            }
        }

    }
}
