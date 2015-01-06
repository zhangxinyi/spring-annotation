package com.tcl.mie;

import com.tcl.mie.annotation.HttpClient;
import com.tcl.mie.appmgr.model.DeviceInfo;

/**
 * @author	xinyizhang
 * @Date	2015-1-6 下午2:55:31 
 */
@HttpClient(url="${http.url}")
public interface DeviceInfoApi
{
    public abstract DeviceInfo addDeviceInfo(DeviceInfo deviceinfo);
}
