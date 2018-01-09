package com.questioner.util;

public class WebSocketUtil {
    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    public static String getValueFormQueryParams(String queryParams, String key) {
        String[] params = queryParams.split("&");
        for (String param: params) {
            String[] entry = param.split("=");
            if (entry.length == 2 && entry[0].equals(key)) {
                return entry[1];
            }
        }
        return null;
    }
}
