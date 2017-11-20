package SOA;

import Api.Spotify.SpotifyHandler;
import Model.Playlist;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class SpotifyService {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String [] args) {
        SpotifyService client = new SpotifyService();
        try {
            client.startConnection(String.valueOf(InetAddress.getByName(null)), 6666);
            String response = client.sendMessage("hello server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
