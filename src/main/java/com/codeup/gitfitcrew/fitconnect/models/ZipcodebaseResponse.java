package com.codeup.gitfitcrew.fitconnect.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ZipcodebaseResponse {
    private ZipcodebaseQuery query;
    private List<ZipcodebaseResult> results;
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ZipcodebaseQuery {
        private int code;
        private String unit;
        private double radius;
        private String country;
    }
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ZipcodebaseResult {
        private int code;
        private String city;
        private String state;
        private double distance;
    }
}
