package Api;

import org.json.JSONArray;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class SpotifyHandler implements ApiWrapper {
    private String clientId = "09485d17989d4058a4c112f80196c12c";
    private String clientSecret = "f0ac2909f1314bc685e0a1259764d501";
    private String redirectUri = "https://github.com/PhilippeMarcotte";

    public static void main(String args[])
    {
        SpotifyHandler handler = new SpotifyHandler();
    }


    public SpotifyHandler() {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", clientId);
            parameters.put("response_type", "token");
            parameters.put("redirect_uri", redirectUri);
            parameters.put("show_dialog", "true");
            URL url = new URL("https://accounts.spotify.com/authorize?" + ParameterStringBuilder.getParamsString(parameters));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setInstanceFollowRedirects(false);
            con.setDoOutput(true);

            int status = con.getResponseCode();
            System.out.println(con.getResponseMessage());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println(content.toString());
            con.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JSONArray searchTrack(String searchEntry) {
        return null;
    }

    public void readTrack(String trackId) {

    }
}

class ParameterStringBuilder {
    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
