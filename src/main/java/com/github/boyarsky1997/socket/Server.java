package com.github.boyarsky1997.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Server {
    private static final List<String> strings = Arrays.asList("a", "b", "c");

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket server = new ServerSocket(2242);
            System.out.println("initialized");

            while (true) {
                Socket socket = server.accept();
                System.out.println();

                ServerThread thread = new ServerThread(socket);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static List<String> readFile(String path) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path)))
                .lines()
                .collect(Collectors.toList());
    }

    static class ServerThread extends Thread {
        private final PrintStream os;
        private final BufferedReader is;
        private final InetAddress addr;
        private final Socket socket;
        private String name;

        public ServerThread(Socket s) throws IOException {
            os = new PrintStream(s.getOutputStream());
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            addr = s.getInetAddress();
            socket = s;
        }

        public void run() {
            String str;
            try {
                while ((str = is.readLine()) != null) {
                    StringBuilder result = new StringBuilder();
                    if (str.startsWith("iam")) {
                        name = str.split(":")[1];
                        System.out.println("client " + name + " connected");
                        String s = strings.get((int) (Math.random() * strings.size()));
                        List<String> strings = readFile("src\\main\\resources\\" + s + ".txt");
                        for (String string : strings) {
                            result.append(string);
                        }
                    }
                    os.println(result);
                    os.flush();

                }
            } catch (IOException e) {
// если клиент не отвечает, соединение с ним разрывается
                e.printStackTrace();
                System.err.println("Disconnect");
            } finally {
                disconnect(); // уничтожение потока
            }
        }

        public void disconnect() {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                System.out.println(addr.getHostName() + " disconnecting");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.interrupt();
            }
        }
    }
}