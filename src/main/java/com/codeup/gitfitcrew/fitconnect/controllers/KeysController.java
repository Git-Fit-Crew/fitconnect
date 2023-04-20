package com.codeup.gitfitcrew.fitconnect.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Component
public class KeysController {
    @Value("${talk-js-api-key}")
    private String talkJsAppId;
    @Value("${google-maps-api-key}")
    private String googleMapApi;
    @Value("${filestack-api-key}")
    private String fileStackAPI;

    @GetMapping(value = "/keys", produces = "application/json")
    @ResponseBody
    public Object getKeys() {
        Map<String, String> keys = new HashMap<>();
        keys.put("googleMapApi", googleMapApi);
        keys.put("talkJsAppId", talkJsAppId);
        keys.put("fileStackAPI", fileStackAPI);
        return keys;
    }
}
