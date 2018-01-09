package com.questioner.service.abs;

import com.alibaba.fastjson.JSONObject;

public interface AuthService {
    String login(String loginUsername, String password);
    String refresh(String oldToken);
    boolean verify(String loginUsername, String password, JSONObject result);
}
