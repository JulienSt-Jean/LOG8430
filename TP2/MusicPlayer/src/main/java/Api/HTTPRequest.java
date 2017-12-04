package Api;

import Api.Exceptions.WebApiException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philippe on 11/2/2017.
 * Représente une requête HTTP
 */
public class HTTPRequest {
    private URL url;
    private Map<String, String> urlParameters = new HashMap<>();
    private String requestMethod = "GET";
    private Map<String, String> requestProperties = new HashMap<>();
    private boolean doOutput = true;
    private String body = "";
    private HttpURLConnection connection;

    /**
     * Constructeur
     * @param url URL de la requête
     */
    public HTTPRequest(URL url) {
        this.url = url;
        this.putContentType("application/json");
    }

    /**
     * Ajoute un paramètre à l'URL
     * @param parameter nom du paramètre
     * @param value valeur du paramètre
     */
    public void putURLParameter(String parameter, String value) {
        urlParameters.put(parameter, value);
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * Ajoute un contenu à la requête
     * @param contentType
     */
    public void putContentType(String contentType) { putRequestProperty("Content-Type", contentType); }

    /**
     * Ajoute une propriété à l'URL
     * @param property nom de la propriété
     * @param value valeur de la propriété
     */
    public void putRequestProperty(String property, String value) {
        requestProperties.put(property, value);
    }

    /**
     * Récupère les paramètres sous forme d'une unique chaîne de caractères
     * @param params paramètres sous forme de map
     * @return Liste des paramètres correctement formattés dans une chaîne de caractères
     * @throws UnsupportedEncodingException
     */
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

    /**
     * Construit une URL
     * @return
     * @throws UnsupportedEncodingException
     */
    public String buildUrl() throws UnsupportedEncodingException {
        return url + (urlParameters.isEmpty() ? "" : "?" + getParamsString(urlParameters));
    }

    /**
     * Ouvre une connexion HTTP
     * @throws IOException
     */
    public void makeConnection() throws IOException {
        URL url = new URL(buildUrl());

        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(requestMethod);

        connection.setDoOutput(doOutput);

        for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        if (!body.isEmpty()) {
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body);
            wr.close();
        }
    }

    /**
     * Récupère la réponse d'une requête
     * @return réponse sous forme de chaîne de caractères
     * @throws IOException
     */
    public String fetchResponse() throws IOException {
        return fetchResponse(connection.getInputStream());
    }

    /**
     * Récupère l'erreur retournée par une requête
     * @return erreur sous forme de chaîne de caractères
     * @throws IOException
     */
    public String fetchErrorResponse() throws IOException {
        return fetchResponse(connection.getErrorStream());
    }

    /**
     * Récupère la réponse d'une requête
     * @param inputStream
     * @return réponse sous forme de chaîne de caractères
     * @throws IOException
     */
    private String fetchResponse(InputStream inputStream) throws IOException {
        if(inputStream != null) {
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

        return "";
    }

    /**
     * Récupère la réponse d'une requête
     * @return
     * @throws WebApiException
     */
    public String getResponse() throws WebApiException {
        try {
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
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
