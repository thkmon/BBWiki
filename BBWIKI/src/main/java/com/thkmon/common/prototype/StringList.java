package com.thkmon.common.prototype;

import java.util.ArrayList;

public class StringList extends ArrayList<String> {
	public String get(int index) {
		if (super.get(index) == null) {
			return "";
		}
		return super.get(index);
	}
}
