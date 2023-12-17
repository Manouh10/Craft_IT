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
public class SceneTemps extends BaseModel implements Serializable {

    private String temps;
    private String serie;
    private String action;
    private String type;

    public SceneTemps(String temps, String serie, String action, String type) {
        this.temps = temps;
        this.serie = serie;
        this.action = action;
        this.type = type;
    }

    public SceneTemps () {
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
