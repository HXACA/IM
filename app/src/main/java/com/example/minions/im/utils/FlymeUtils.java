package com.example.minions.im.utils;

//���Flyme
import android.os.Build;

import java.lang.reflect.Method;

public final class FlymeUtils {

	public static boolean isFlyme() {
		try {
			// Invoke Build.hasSmartBar()
			final Method method = Build.class.getMethod("hasSmartBar");
			return method != null;
		} catch (final Exception e) {
			return false;
		}
	}

}
