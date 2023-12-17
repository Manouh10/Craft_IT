/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javax.persistence.Entity;

/**
 *
 * @author TANJONA fetrasoa
 */
@Entity
public class SceneConsomation extends BaseModel{
   private  String action;
   private String serie;
   private double maxKwh;
   private double maxA;
   private int etat;

    public SceneConsomation(String action, String serie, double maxKwh, double maxA, int etat) {
        this.action = action;
        this.serie = serie;
        this.maxKwh = maxKwh;
        this.maxA = maxA;
        this.etat = etat;
    }

    public SceneConsomation() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public double getMaxKwh() {
        return maxKwh;
    }

    public void setMaxKwh(double maxKwh) {
        this.maxKwh = maxKwh;
    }

    public double getMaxA() {
        return maxA;
    }

    public void setMaxA(double maxA) {
        this.maxA = maxA;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

   
   
   
}
