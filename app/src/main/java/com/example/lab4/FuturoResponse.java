package com.example.lab4;

import java.util.List;

public class FuturoResponse {
    public Location location;
    public Forecast forecast;

    public static class Location {
        public String name;
        public String region;
        public String country;
    }

    public static class Forecast {
        public List<ForecastDay> forecastday;
    }

    public static class ForecastDay {
        public String date;
        public List<Hour> hour;

        public static class Hour {
            public String time;
            public double temp_c;
            public int humidity;
            public double precip_mm;
            public double wind_kph;
            public double uv;
            public Condition condition;
            public int chance_of_rain;
        }

        public static class Condition {
            public String text;
            public String icon;
        }
    }
}
