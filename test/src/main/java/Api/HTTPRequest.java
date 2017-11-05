package Api;

import Api.Exceptions.WebApiAuthenticationException;
import Api.Exceptions.WebApiException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philippe on 11/2/2017.
 */
public class HTTPRequest {
    private String strUrl;
    private Map<String, String> urlParameters = new HashMap<>();
    private String requestMethod = "GET";
    private Map<String, String> requestProperties = new HashMap<>();
    private boolean doOutput = true;
    private boolean doInput = true;
    private HttpURLConnection connection;

    public HTTPRequest(String url) {
        this.strUrl = url;
    }

    public void putURLParameter(String parameter, String value) {
        urlParameters.put(parameter, value);

        this.putContentType("application/json");
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void putContentType(String contentType) { putRequestProperty("Content-Type", contentType); }

    public void setDoOutput(boolean doOutput) {
        this.doOutput = doOutput;
    }

    public void setDoInput(boolean doInput) {
        this.doInput = doInput;
    }

    public void putRequestProperty(String property, String value) {
        requestProperties.put(property, value);
    }

    private String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
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

    public String buildUrl() throws UnsupportedEncodingException {
        return strUrl + (urlParameters.isEmpty() ? "" : "?" + getParamsString(urlParameters));
    }

    public void makeConnection() throws IOException {
        URL url = new URL(buildUrl());

        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(requestMethod);

        connection.setDoOutput(doOutput);

        for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    public String fetchResponse() throws IOException {
        return fetchResponse(connection.getInputStream());
    }

    public String fetchErrorResponse() throws IOException {
        return fetchResponse(connection.getErrorStream());
    }

    private String fetchResponse(InputStream inputStream) throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    public String getResponse() throws WebApiException {
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {
                return fetchResponse();
            } else {
                throw new WebApiException(fetchErrorResponse());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
