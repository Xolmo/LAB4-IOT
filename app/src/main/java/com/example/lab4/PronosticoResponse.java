package com.example.lab4;

import java.util.List;

public class PronosticoResponse {
    public Location location;
    public Forecast forecast;

    public static class Location {
        public String name;
        public String region;
        public String country;
        public String tz_id;
    }

    public static class Forecast {
        public List<ForecastDay> forecastday;
    }

    public static class ForecastDay {
        public String date;
        public Day day;

        public static class Day {
            public double maxtemp_c;
            public double mintemp_c;
            public double avghumidity;
            public double uv;
            public Condition condition;
        }

        public static class Condition {
            public String text;
            public String icon;
        }
    }
}

