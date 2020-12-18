package com.github.boyarsky1997.socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) throws IOException {
        String myName = args[0];
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 2242);
            PrintStream ps = new PrintStream(socket.getOutputStream());
            Scanner scanner1 = new Scanner(socket.getInputStream());
            ps.println("iam:" + myName);
            System.out.println("Server > " + scanner1.nextLine());

        } catch (UnknownHostException e) {
            System.err.println("адрес недоступен" + e);
        } catch (IOException e) {
            System.err.println("ошибка I/О потока" + e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

