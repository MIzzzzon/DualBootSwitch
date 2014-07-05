package com.pexcn.dualbootswitch;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

public class CallNative {
	public native String getBootmode();
	public native String setBootmode(String bootmode);
	
    static {
		System.loadLibrary("dualbootswitch");
	}
    
    public void gainMiscAccess() throws Exception {
		CommandCapture command = new CommandCapture(0,
				"busybox chmod 0777 /dev/block/platform/msm_sdcc.1/by-name/misc");
		RootTools.getShell(true).add(command).waitForFinish();
	}
	
	public void reboot(String mode) {
		CommandCapture command = new CommandCapture(0, "reboot "+ mode);
		try {
			RootTools.getShell(true).add(command).waitForFinish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
