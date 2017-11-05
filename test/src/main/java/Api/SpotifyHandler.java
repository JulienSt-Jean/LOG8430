package Api;

import Api.Exceptions.WebApiAuthenticationException;
import Api.Exceptions.WebApiException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SpotifyHandler implements ApiWrapper {
    public static void main(String args[]) {
        SpotifyHandler handler = new SpotifyHandler();
    }

    private String userId;

    private SpotifyHTTPRequestBuilder httpRequestBuilder = new SpotifyHTTPRequestBuilder();

    private enum ApiError {
        ACCESS_TOKEN_EXPIRED,
        UNKNOWN
    }

    public SpotifyHandler(){
        String response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildUserProfileRequest());
        setUserId(response);
    }

    public JSONArray searchTrack(String searchEntry) {
        return null;
    }

    public void readTrack(String trackId) {

    }

    private void generateAccessToken(){
        HTTPRequest request = httpRequestBuilder.buildAccessTokenRequest();

        try {
            request.makeConnection();
            Files.write(Paths.get("./tokens"), request.getResponse().getBytes());
            httpRequestBuilder.loadToken();
        } catch (WebApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAccessToken(){
        HTTPRequest request = httpRequestBuilder.buildRefreshAccessTokenRequest();

        JsonObject jsonResponse = null;
        try {
            request.makeConnection();
            jsonResponse = new JsonParser().parse(request.getResponse()).getAsJsonObject();
        } catch (WebApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonResponse.addProperty("refresh_token", httpRequestBuilder.getRefreshToken());

        try {
            Files.write(Paths.get("./tokens"), jsonResponse.toString().getBytes());
            httpRequestBuilder.loadToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String executeRequestWithRetryOnExpiredToken(HTTPRequest request){
        while(true) {
            try {
                request.makeConnection();
                System.out.println(request.getResponseCode());
                return request.getResponse();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (WebApiException e) {
                ApiError error = testErrorResponse(e.getMessage());
                if (error == ApiError.ACCESS_TOKEN_EXPIRED){
                    refreshAccessToken();
                }
                else {
                    e.printStackTrace();
                    break;
                }
            }
        }

        return "";
    }

    private ApiError testErrorResponse(String response){
        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();

        switch(jsonResponse.get("error").getAsJsonObject().get("message").getAsString()){
            case "The access token expired":
                return ApiError.ACCESS_TOKEN_EXPIRED;
            default:
                return ApiError.UNKNOWN;
        }
    }

    private void setUserId(String response){
        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();
        userId = jsonResponse.get("userId").getAsString();
    }

    public
}
