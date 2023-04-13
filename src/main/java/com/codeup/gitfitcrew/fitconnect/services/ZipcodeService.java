package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.ZipcodebaseResponse;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZipcodeService {
    @Value("${zipcodebase-api-key}")
    private String zipcodebaseApiKey;

    @Autowired
    UserRepository userDao;

    public List<Integer> getZipcodesWithinMilesRadiusFrom(int zipcode, double miles) {
        List<Integer> zipcodes = new ArrayList<>();
        try {
            URL url = new URL("https://app.zipcodebase.com/api/v1/radius");
            UriComponents uriComponents = UriComponentsBuilder.fromUri(url.toURI())
                    .queryParam("code", String.valueOf(zipcode))
                    .queryParam("radius", String.valueOf(miles))
                    .queryParam("country", "us")
                    .queryParam("unit", "miles").build();
            HttpURLConnection connection = (HttpURLConnection) uriComponents.toUri().toURL().openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","text/plain");
            connection.setRequestProperty("apikey", zipcodebaseApiKey);
            connection.setRequestMethod("GET");
            connection.getResponseCode();
            String jsonResponse = new String(connection.getInputStream().readAllBytes());

            Gson gson = new Gson();
            ZipcodebaseResponse response = gson.fromJson(jsonResponse, ZipcodebaseResponse.class);
            response.getResults().forEach(result -> zipcodes.add(result.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipcodes;
    }
}
