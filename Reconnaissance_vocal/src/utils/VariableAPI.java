/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author TANJONA fetrasoa
 */
public class VariableAPI {

    public static String onDevice = "{\"developerId\":\"-Nlm4vKk3psfiVmtLOhE\",\"email\":\"jrmanouhoseah@gmail.com\",\"deviceId\":\"bf02db56c7a8be675baaey\",\"switch_status\":\"ON\"}";
    public static String offDevice = "{\"developerId\":\"-Nlm4vKk3psfiVmtLOhE\",\"email\":\"jrmanouhoseah@gmail.com\",\"deviceId\":\"bf02db56c7a8be675baaey\",\"switch_status\":\"OFF\"}";
    public static String switchDeviceUrl = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_switch_device";
    public static String ConsomationDeviceUrl = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_check_deviceStatus?developerId=-Nlm4vKk3psfiVmtLOhE&email=jrmanouhoseah@gmail.com&deviceId=bf02db56c7a8be675baaey";
    public static String statisticDeviceUrl = "https://us-central1-boulou-functions-for-devs.cloudfunctions.net/boulou_get_deviceStatistics?developerId=-Nlm4vKk3psfiVmtLOhE&email=jrmanouhoseah@gmail.com&deviceId=bf02db56c7a8be675baaey&period_type=year&period_value=2023";

}
