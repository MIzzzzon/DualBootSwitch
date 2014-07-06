package com.pexcn.dualbootswitch;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class Main extends PreferenceActivity {
	public SwitchPreference switcher;
	public Preference reboot;
	public CallNative callNative = new CallNative();
	public int target = 0;
	public int flag = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getMode();
	}
	
	// 程序列表
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		if (preference == switcher) {		// 切换系统
			if (getMode() == 1) {
				callNative.setBootmode("boot-system1");
				switcher.setChecked(true);
				Toast.makeText(Main.this, R.string.settosystem2, Toast.LENGTH_SHORT).show();
			} else if (getMode() == 2) {
				callNative.setBootmode("boot-system0");
				switcher.setChecked(false);
				Toast.makeText(Main.this, R.string.settosystem1, Toast.LENGTH_SHORT).show();
			}
		} else if (preference == reboot) {		// 重新启动
			Builder reboot = new AlertDialog.Builder(this);
			reboot.setTitle(R.string.reboot);
			reboot.setSingleChoiceItems(R.array.reboot, 0, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {	// 单选列表 设置flag
					if (which == 0) {
						flag = which;
						callNative.setBootmode("boot-system0");
					}
					if (which == 1) {
						flag = which;
						callNative.setBootmode("boot-system1");
					}
					else if (which == 2) {
						flag = which;
					}
					else if (which == 3) {
						flag = which;
					}
				}
			});
			reboot.setPositiveButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {	// 确定按钮
					if (flag == 0) {
						callNative.setBootmode("boot-system0");
						callNative.reboot("");
					} else if (flag == 1) {
						callNative.reboot("");
					} else if (flag == 2) {
						callNative.reboot("recovery");
					} else if (flag == 3) {
						callNative.reboot("bootloader");
					}
				}
			});
			reboot.setNegativeButton(R.string.cancel, null);
			reboot.create().show();
		}
		return true;
	}
	
	// 初始化
	public void init() {
		addPreferencesFromResource(R.xml.main);
	    switcher = (SwitchPreference)findPreference("switcher");
	    reboot = findPreference("reboot");
	}
	
	// 获取ROOT
	public void getRoot() {
        try {
			callNative.gainMiscAccess();
			getMode();
		} catch (Exception e) {
			Toast.makeText(this, R.string.failed, Toast.LENGTH_LONG).show();
			switcher.setEnabled(false);
			reboot.setEnabled(false);
		}
	}
	
	// 获取当前系统的mode
	public int getMode() {
		String mode = callNative.getBootmode();
		if (mode.equals("boot-system0")) {
			switcher.setChecked(false);
			return 1;
		} else if (mode.equals("boot-system1")) {
			switcher.setChecked(true);
			return 2;
		}
		return 0;
	}
}
