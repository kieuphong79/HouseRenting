package com.uet.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uet.threads.MultiThread;
import com.uet.view.BaseView;

import javafx.concurrent.Task;
import javafx.scene.Node;


public abstract class Request<T> extends Task<T> {
    private static String HOST = "127.0.0.1";
    private static int PORT = 10504;
    private Socket clientSocket;
    private JSONObject request;

    public Request() {
        try {
            clientSocket = new Socket(HOST, PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
        }
    }
    public void sendRequest() throws IOException {
        System.out.println("send request");
        PrintWriter p = new PrintWriter(clientSocket.getOutputStream());
        p.flush();
        p.println(request.toString());
        p.flush();
        // p.close();
    }
    public String receiveResponse() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String t = reader.readLine();
        while (t == null) {
            t = reader.readLine();
        }
        // clientSocket.close();
        return t;
    }
    public void createRequest(String type) {
        request = new JSONObject();
        request.put("type", type);
    }
    public void createQueryRequest(String sql, List<Object> p) {
        JSONArray parameters = new JSONArray();
        getRequest().put("queries", new JSONArray().put(new JSONObject().put("sql", sql).put("parameters", parameters)));
        for (Object o : p) {
            parameters.put(o);
        }
    }
    public JSONArray getDataFromResponse(int index, JSONObject response) {
        JSONArray data = response.getJSONArray("result").getJSONObject(index).getJSONArray("data");
        return data;
    }
    public void createQueryRequest(String sql) {
        getRequest().put("queries", new JSONArray().put(new JSONObject().put("sql", sql)));
    }
    public void createUpdateRequest(String sql) {
        getRequest().put("updateST", new JSONArray().put(new JSONObject().put("sql", sql)));
    }
    public void createUpdateRequest(String sql, List<Object> p) {
        JSONArray parameters = new JSONArray();
        getRequest().put("updateST", new JSONArray().put(new JSONObject().put("sql", sql).put("parameters", parameters)));
        for (Object o : p) {
            parameters.put(o);
        }
    }
    public JSONObject getRequest() {
        return request;
    }
    // public Statement createStatement() throws SQLException {
    //     return MysqlConnector.getInstance().getConnection().createStatement();
    // }
    // public PreparedStatement createPreparedStatement(String st) throws SQLException {
    //     return MysqlConnector.getInstance().getConnection().prepareStatement(st);
    // }


    public T startInMainThread() throws Exception{
        return this.call();
    }

    //ma 1
    public void startInThread() {
        // BaseView.getInstance().getProgressBar().progressProperty().bind(this.progressProperty());
        MultiThread.execute(this);
    }
    public void startInThread(Node node) {
        BaseView.getInstance().getProgressBar().setNode(node);
        BaseView.getInstance().getProgressBar().progressProperty().bind(this.progressProperty());
        MultiThread.execute(this);
    }
    
    // public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
    //     Socket clientSocket = new Socket(HOST, PORT);
    //     JSONObject jsonObject = new JSONObject();
    //     jsonObject.put("type", "query");
    //     jsonObject.put("sql", "select * from houses limit 10;");
    //     PrintWriter p = new PrintWriter(clientSocket.getOutputStream());
    //     p.println(jsonObject.toString());
    //     p.println("Honaf thanh");
    //     p.flush();
    //     System.out.println("send Success");

    //     BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    //     String t = rd.readLine();
    //     t = rd.readLine();
    //     t = rd.readLine();
    //     System.out.println(t);

    //     clientSocket.close();
    // }

}
