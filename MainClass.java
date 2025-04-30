package com.wetherApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONObject;

public class MainClass {

    private static final String API_KEY = "5e5a6451db26cd3bb4c189b7d6649e2c";  // Replace with your API key

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine().trim();

        try {
            // URL encode the city name to handle spaces and special characters
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    encodedCity + "&appid=" + API_KEY + "&units=metric";

            // Create the URL and open the connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            // Check if the response code is OK
            if (responseCode != 200) {
                System.out.println("Error: City not found or API error");
                return;
            }

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonData = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
            reader.close();

            // Parse the response JSON
            JSONObject obj = new JSONObject(jsonData.toString());

            String cityName = obj.getString("name");
            double temp = obj.getJSONObject("main").getDouble("temp");
            String weather = obj.getJSONArray("weather").getJSONObject(0).getString("description");
            int humidity = obj.getJSONObject("main").getInt("humidity");
            double windSpeed = obj.getJSONObject("wind").getDouble("speed");

            // Display the weather details
            System.out.println("\nWeather in " + cityName + ":");
            System.out.println("Temperature: " + temp + "°C");
            System.out.println("Condition: " + weather);
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + windSpeed + " m/s");

        } catch (Exception e) {
            // Handle any errors
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
