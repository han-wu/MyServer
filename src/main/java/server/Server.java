package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description:
 * <br/>
 *
 * @author 吴晗
 * @date 2022/3/21 14:44
 */
public class Server{
    private static final boolean START_UP = false;

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket accept = serverSocket.accept();
                new HttpServer(accept).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean serverState () {
        return START_UP;
    }
}
