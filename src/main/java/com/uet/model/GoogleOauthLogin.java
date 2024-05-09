package com.uet.model;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uet.App;
import com.uet.exception.LoginErrorException;


public class GoogleOauthLogin {
    
    private static final String CLIENT_SECRET_FILE = "client_secret.json"; // Replace with your client secret file
    private static final String SCOPES = "email profile"; // Scopes you want to request access to
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new GsonFactory();
    private static int PORT = 10504;
    private static String HOST = "127.0.0.1";
    private static String REDIRECT_URL = "http://" + HOST + ":" + PORT;
    private static String CLIENT_ID;
    // private static int test = 0; // debug
    // private static final String DATA_STORE_DIR = System.getProperty("user.home") + File.separator + ".houserenting";

    private GoogleAuthorizationCodeFlow flow;
    private static ServerSocket serverSocket;
    public GoogleOauthLogin ()  {
        InputStreamReader isr = new InputStreamReader(App.class.getResourceAsStream(CLIENT_SECRET_FILE));
        JsonObject jsonObject = JsonParser.parseReader(isr).getAsJsonObject();
        CLIENT_ID = jsonObject.get("installed").getAsJsonObject().get("client_id").getAsString();
        GoogleClientSecrets clientSecrets;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(App.class.getResourceAsStream(CLIENT_SECRET_FILE)));
        } catch (IOException e) {
            throw new RuntimeException("lỗi load secret");
        }
        flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Arrays.asList(SCOPES))
                .setAccessType("offline") 
                .setApprovalPrompt("force")
                // .setDataStoreFactory(new FileDataStoreFactory(new File(DATA_STORE_DIR)))
                .build(); 
    }
    public User login() throws LoginErrorException, IOException {
        String authorizationUrl = flow.newAuthorizationUrl()
                // .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
                .setRedirectUri(REDIRECT_URL)
                .build();
        System.out.println("Authorization URL:");
        System.out.println(authorizationUrl);
        try {
            Desktop.getDesktop().browse(new URI(authorizationUrl));
        } catch (Exception e) {
            throw new LoginErrorException(e.getMessage());
        }
        String code = "";
        code = listentToResponse();
        
        // Task<String> task = new Task<String>() {

        //     @Override
        //     protected String call() throws Exception {
        //         // int t = test++;
        //         String res = listentToResponse();
        //         // System.out.println(test + " recieve");
        //         return res;
        //     }
            
        // };
        // MultiThread.execute(task);
        // try {
        //     System.out.println(task.get());
        // } catch (InterruptedException | ExecutionException e) {
        //     // TODO Auto-generated catch block
        //     System.out.println("here");
        // }
        // Thread t = new Thread(task);
        // t.start();
        // try {
        //     code = task.get(2, TimeUnit.SECONDS);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (ExecutionException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (TimeoutException e) {
        //     System.out.println("time fucking out");
        //     t.interrupt();
        //     while (true) {
        //         System.out.println("thread state : " + t.getState());
        //         System.out.println("thread interrupt: " + t.isInterrupted());
        //         if (t.getState().equals(Thread.State.TERMINATED)) break;
        //     }
        //     // e.printStackTrace();
        //     // throw new LoginErrorException("thread time out");
        // }
        // return null;
        System.out.println(code);

        AuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URL);
        TokenResponse tokenResponse;
        try {
            tokenResponse = tokenRequest.execute();
        } catch (IOException e) {
            throw new LoginErrorException("lỗi request token");
        }
        
        System.out.println(tokenResponse.get("id_token"));
        String idTokenString = (String) tokenResponse.get("id_token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                                            .setAudience(Collections.singletonList(CLIENT_ID))
                                            .build();
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            throw new LoginErrorException(e.getMessage());
        }

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
           
            // Get profile information from payload
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            // System.out.println("User ID: " + userId);
            // System.out.println(email);
            // System.out.println(name);
            // System.out.println(pictureUrl);
        System.out.println("done login googlea oauth");
            return new User(userId, email, name, pictureUrl);
        } else {
            throw new LoginErrorException("Lỗi id token");
        }
        // Extract tokens from response
        // String accessToken = tokenResponse.getAccesstoken();
        // string refreshtoken = tokenresponse.getrefreshtoken();

        // // print tokens
        // system.out.println("access token: " + accesstoken);
        // system.out.println("refresh token: " + refreshtoken);
    }
    public String listentToResponse() throws IOException, LoginErrorException  {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        } 
        serverSocket = new ServerSocket(PORT);
        serverSocket.setSoTimeout(10000);
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (SocketTimeoutException e) {
            System.out.println("socket time out");
            serverSocket.close();
            throw new LoginErrorException("time out socket error");
        }
        System.out.println("Server listening on port " + PORT);

        // while (true) {
            System.out.println("Client connected");

            // Handle client request in a new thread
            String temp = handleRequest(clientSocket);
            serverSocket.close();
            return temp;
        // }
    }
    private String handleRequest(Socket clientSocket) throws IOException, LoginErrorException  {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // Parse request line to get the URL and query parameters
            String[] parts = requestLine.split(" ");
            String url = parts[1];
            Map<String, String> queryParams = parseQueryParams(url);

            // Print the query parameters
            System.out.println("Query Parameters:");
            if (queryParams.containsKey("error")) {
                sendFailureHtmlResponse(out);
                throw new LoginErrorException("loi google");
            }
            //debug
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
             // Send response back to the client
            sendHtmlResponse(out);
            
            return queryParams.get("code");
        } finally {
           clientSocket.close();
        } 
        
    }
    private static Map<String, String> parseQueryParams(String url) {
        Map<String, String> queryParams = new HashMap<>();
        try {
            String[] parts = url.split("\\?");
            if (parts.length > 1) {
                String query = parts[1];
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    queryParams.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi decode GoogleOauthLogin");
        }
        return queryParams;
    }
    private static void sendFailureHtmlResponse(PrintWriter out) {
    out.println("HTTP/1.1 200 OK");
    out.println("Content-Type: text/html; charset=UTF-8");
    out.println();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<meta charset=\"UTF-8\">");
    out.println("<link href=\"https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap\" rel=\"stylesheet\">");
    out.println("</head>");
    out.println("<style>");
    out.println("body {");
    out.println("text-align: center;");
    out.println("padding: 40px 0;");
    out.println("background: #EBF0F5;");
    out.println("}");
    out.println("h1 {");
    out.println("color: #FF6347;"); // Red color for failure
    out.println("font-family: \"Nunito Sans\", \"Helvetica Neue\", sans-serif;");
    out.println("font-weight: 900;");
    out.println("font-size: 40px;");
    out.println("margin-bottom: 10px;");
    out.println("}");
    out.println("p {");
    out.println("color: #404F5E;");
    out.println("font-family: \"Nunito Sans\", \"Helvetica Neue\", sans-serif;");
    out.println("font-size:20px;");
    out.println("margin: 0;");
    out.println("}");
    out.println("i {");
    out.println("color: #FF6347;"); // Red color for failure
    out.println("font-size: 100px;");
    out.println("line-height: 200px;");
    out.println("margin-left:-15px;");
    out.println("}");
    out.println(".card {");
    out.println("background: white;");
    out.println("padding: 60px;");
    out.println("border-radius: 4px;");
    out.println("box-shadow: 0 2px 3px #C8D0D8;");
    out.println("display: inline-block;");
    out.println("margin: 0 auto;");
    out.println("}");
    out.println("</style>");
    out.println("<body>");
    out.println("<div class=\"card\">");
    out.println("<div style=\"border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;\">");
    out.println("<i class=\"crossmark\">✘</i>"); // Change checkmark to crossmark for failure
    out.println("</div>");
    out.println("<h1>Thất bại</h1>"); // Change "Success" to "Failure"
    out.println("<p>Đăng nhập thất bại</p>"); // Modify message for failure
    out.println("</div>");
    out.println("</body>");
    out.println("</html>");
}

    private void sendHtmlResponse(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">"); // Set character encoding in HTML head
        out.println("<link href=\"https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<style>");
        out.println("body {");
        out.println("text-align: center;");
        out.println("padding: 40px 0;");
        out.println("background: #EBF0F5;");
        out.println("}");
        out.println("h1 {");
        out.println("color: #88B04B;");
        out.println("font-family: \"Nunito Sans\", \"Helvetica Neue\", sans-serif;");
        out.println("font-weight: 900;");
        out.println("font-size: 40px;");
        out.println("margin-bottom: 10px;");
        out.println("}");
        out.println("p {");
        out.println("color: #404F5E;");
        out.println("font-family: \"Nunito Sans\", \"Helvetica Neue\", sans-serif;");
        out.println("font-size:20px;");
        out.println("margin: 0;");
        out.println("}");
        out.println("i {");
        out.println("color: #9ABC66;");
        out.println("font-size: 100px;");
        out.println("line-height: 200px;");
        out.println("margin-left:-15px;");
        out.println("}");
        out.println(".card {");
        out.println("background: white;");
        out.println("padding: 60px;");
        out.println("border-radius: 4px;");
        out.println("box-shadow: 0 2px 3px #C8D0D8;");
        out.println("display: inline-block;");
        out.println("margin: 0 auto;");
        out.println("}");
        out.println("</style>");
        out.println("<body>");
        out.println("<div class=\"card\">");
        out.println("<div style=\"border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;\">");
        out.println("<i class=\"checkmark\">✓</i>");
        out.println("</div>");
        out.println("<h1>Success</h1> ");
        out.println("<p>Đăng nhập thành công;<br/> hãy quay trở lại app!</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    // public boolean checkLogin() {
    //     Credential cre = flow.loadCredential("user");
    // }
    public static void main(String[] args) throws LoginErrorException, IOException {
        new GoogleOauthLogin().login();
        
    }
}
