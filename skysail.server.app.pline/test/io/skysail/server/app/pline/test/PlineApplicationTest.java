package io.skysail.server.app.pline.test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class PlineApplicationTest {

    private static final String SERVER_KEY = "AIzaSyBWjoXIwJKm6p30f25OoHPm3OKIsNDgd1s";
    private static final String DEVICE_TOKEN = "";

    @Test
    @Ignore
    public void testPushIOS() {
        String device_token = "YOUR_DEVICE_TOKEN";
        ApnsService service = APNS.newService()
                .withCert("PATH_TO_P12_CERTIFICATE", "PASSWORD_OF_CERTIFICATE")
                .withSandboxDestination()
                .build();
        String payload = APNS.newPayload()
                // .customFields(map)
                .alertBody("Hello, it's working")
                .sound("default")
                .build();

        // service.push(token, payload);
        System.out.println("The message has been hopefully sent...");
    }

    @Test
    public void testName() {
        String title = "My First Notification";
        String message = "Hello, I'm push notification";
        //sendPushNotification(title, message);
    }

    /**
     * Sends notification to mobile YOU DON'T NEED TO UNDERSTAND THIS METHOD
     */
    private static void sendPushNotification(String title, String message) throws Exception {
        String pushMessage = "{\"data\":{\"title\":\"" +
                title +
                "\",\"message\":\"" +
                message +
                "\"},\"to\":\"" +
                DEVICE_TOKEN +
                "\"}";
        // Create connection to send FCM Message request.
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        // Send FCM message content.
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(pushMessage.getBytes());
        System.out.println(conn.getResponseCode());
        System.out.println(conn.getResponseMessage());

    }
}
