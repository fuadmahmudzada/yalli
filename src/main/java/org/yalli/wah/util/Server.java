package org.yalli.wah.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class Server {

    private static final String SERVER_URL = "https://yalli-back-end-7v7d.onrender.com/v1/events?title=&country=&&page=0&size=10";

    @Scheduled(fixedRate = 600000)
    public void pingServer() {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Ping successful: Server is alive");
            } else {
                System.out.println("Ping failed: Response code " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Ping failed: " + e.getMessage());
        }
    }
}
