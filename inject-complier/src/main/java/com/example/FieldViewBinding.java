package com.example;

import javax.lang.model.type.TypeMirror;

/**
 * Created by TSH on 2017/3/23.
 */

public class FieldViewBinding {
	private String name; // mTextView 成员变量名
	private TypeMirror typeMirror ; //  TextView 类类型
	private int resId ;// R.id.textview

	public FieldViewBinding(String name, TypeMirror typeMirror, int resId) {
		this.name = name;
		this.typeMirror = typeMirror;
		this.resId = resId;
	}

	public String getName() {
		return name;
	}

	public TypeMirror getTypeMirror() {
		return typeMirror;
	}

	public int getResId() {
		return resId;
	}
}
