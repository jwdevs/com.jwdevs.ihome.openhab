package com.jwdevs.ihome.openhab.communication.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaTcpServer {

    private static class Connection implements Runnable {
        private final Socket clientSocket;

        Connection(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String clientSentence;
                    clientSentence = inFromClient.readLine();
                    if (clientSentence != null) {
                        System.out.println("Received: " + clientSentence);
                        // outToClient.writeBytes("0123456789");
                        outToClient.writeByte('X');
                        outToClient.writeByte('Y');

                        System.out.println("Written bytes: " + outToClient.size());
                        outToClient.flush();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(2222);
        System.out.println("Waiting for clients...");
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected: " + connectionSocket.getInetAddress().toString());

            Thread thread = new Thread(new Connection(connectionSocket));
            thread.start();
        }

    }
}
