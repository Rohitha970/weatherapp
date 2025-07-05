import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.JSONObject;

public class WeatherApp {

    // ANSI Colors for better UI
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String RED = "\u001B[31m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(BLUE + "🌍 Welcome to Real-Time Weather CLI App!" + RESET);

        while (true) {
            System.out.print(YELLOW + "\nEnter latitude (e.g., 13.08): " + RESET);
            double latitude = scanner.nextDouble();

            System.out.print(YELLOW + "Enter longitude (e.g., 80.27): " + RESET);
            double longitude = scanner.nextDouble();

            getWeather(latitude, longitude);

            System.out.print(CYAN + "\nDo you want to check another location? (y/n): " + RESET);
            String again = scanner.next();
            if (!again.equalsIgnoreCase("y")) break;
        }

        System.out.println(GREEN + "\nThank you for using WeatherApp!" + RESET);
        scanner.close();
    }

    public static void getWeather(double lat, double lon) {
        String apiUrl = String.format(
            "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current_weather=true",
            lat, lon);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONObject current = json.getJSONObject("current_weather");

                System.out.println(GREEN + "\n🌤️ Current Weather Data:" + RESET);
                System.out.println(CYAN + "Temperature   : " + current.getDouble("temperature") + " °C");
                System.out.println("Wind Speed    : " + current.getDouble("windspeed") + " km/h");
                System.out.println("Wind Direction: " + current.getDouble("winddirection") + "°");
                System.out.println("Weather Code  : " + current.getInt("weathercode"));
                System.out.println("Time          : " + current.getString("time") + RESET);
            } else {
                System.out.println(RED + "Failed to get weather data. HTTP Code: " + response.statusCode() + RESET);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(RED + "❌ Error: " + e.getMessage() + RESET);
        }
    }
}
