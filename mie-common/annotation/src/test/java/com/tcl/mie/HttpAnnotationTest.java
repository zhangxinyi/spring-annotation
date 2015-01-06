package com.tcl.mie;

import com.tcl.mie.appmgr.model.DeviceInfo;


/**
 * @author xinyizhang
 * @Date 2015-1-6 下午3:09:36
 */
public class HttpAnnotationTest {
	public static void main(String[] args) {
		DeviceInfoApi deviceInfoApi = (DeviceInfoApi)SpringHelper.getSingleBeanByType(DeviceInfoApi.class);
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setImei("api_imei001");
		deviceInfo.setImsi("testimsi001");
		try{
			DeviceInfo result = deviceInfoApi.addDeviceInfo(deviceInfo);
			System.out.println(result.getId());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
