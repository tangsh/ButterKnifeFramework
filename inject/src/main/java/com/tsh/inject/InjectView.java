package com.tsh.inject;

import android.app.Activity;
import android.view.View;

/**
 * Created by TSH on 2017/3/23.
 */

public class InjectView {
	public static void bind(Activity activity) {
		String className = activity.getClass().getName();
		try {
			Class<?> viewBindClass = Class.forName(className + "$$ViewBinder");
			ViewBinder viewBinder = (ViewBinder) viewBindClass.newInstance();
			viewBinder.bind(activity);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
