package io.github.dhohmann.ungit.utils;

import io.github.dhohmann.ungit.Ungit;

public class WrapperUtils {

	private WrapperUtils() {

	}

	private static Ungit ungitInstance;

	public static void setUngit(Ungit ungit) {
		if (ungitInstance == null) {
			ungitInstance = ungit;
		}
	}
}
