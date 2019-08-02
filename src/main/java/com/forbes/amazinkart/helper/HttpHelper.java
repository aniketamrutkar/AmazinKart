package com.forbes.amazinkart.helper;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
  /**
   *
   * @param url
   * @return
   * @throws Exception
   */
  public static String get(String url) throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
  	con.setRequestProperty("User-Agent", "test-code");
    int responseCode = con.getResponseCode();
    if (responseCode >= 400) {
      InputStream errorStream = con.getErrorStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(errorStream));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      throw new Exception(response.toString());
    }

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }

    in.close();

    return response.toString();
  }
}
