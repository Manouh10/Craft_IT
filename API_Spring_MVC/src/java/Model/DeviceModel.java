/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author TANJONA fetrasoa
 */
public class DeviceModel {
    private String developerId="";
    private String email="";
    private String deviceId="";
    private String switch_status="";

    public DeviceModel(String developerId,String email,String deviceId,String switch_status) {
        this.developerId=developerId;
        this.deviceId=developerId;
        this.email=email;
        this.switch_status=switch_status;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSwitch_status() {
        return switch_status;
    }

    public void setSwitch_status(String switch_status) {
        this.switch_status = switch_status;
    }
  
    
}
