package com.example.util;

public class JScript {
	public static String href(String alertMessage, String locationPath) {

		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("   alert('" + alertMessage + "');");
		sb.append("   location.href = '" + locationPath + "';");
		sb.append("</script>");

		return sb.toString();
	}

	public static String back(String alertMessage) {

		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("   alert('" + alertMessage + "');");
		sb.append("   history.back();");
		sb.append("</script>");

		return sb.toString();
	}
}
