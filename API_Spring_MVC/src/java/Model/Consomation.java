/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author TANJONA fetrasoa
 */
@Entity
public class Consomation extends BaseModel {
 private  String online;
 private String switchs;
 private double actual_power;
 private double actual_current;

    public Consomation(String online, String switchs, double actual_power, double actual_current) {
        this.online = online;
        this.switchs = switchs;
        this.actual_power = actual_power;
        this.actual_current = actual_current;
    }

    public Consomation() {
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getSwitchs() {
        return switchs;
    }

    public void setSwitchs(String switchs) {
        this.switchs = switchs;
    }

    public double getActual_power() {
        return actual_power;
    }

    public void setActual_power(double actual_power) {
        this.actual_power = actual_power;
    }

    public double getActual_current() {
        return actual_current;
    }

    public void setActual_current(double actual_current) {
        this.actual_current = actual_current;
    }  
    
}
