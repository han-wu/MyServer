package server;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * <br/>
 *
 * @author 吴晗
 * @date 2022/3/21 15:01
 */
public class HttpServer extends Thread{
    private static final String RESOURCE_DIR = "work";

    private Socket socket;

    public HttpServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.printf("当前时间 %s, 收到浏览器请求，线程name = %s, 线程Id = %s\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                Thread.currentThread().getName(), Thread.currentThread().getId());
        BufferedReader reader = null;
        BufferedReader sourceReader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //封装响应消息行
            writer.write("HTTP/1.1 200 Hello MyServer");
            writer.newLine();
            //封装响应消息头
            writer.write("Content-Type:text/html");
            writer.newLine();
            writer.newLine();

            //请求行
            String requestLine = reader.readLine();
            System.out.printf("当前时间 %s, 请求uri = %s，线程name = %s, 线程Id = %s\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), requestLine, Thread.currentThread().getName(), Thread.currentThread().getId());
            String uri = requestLine.split(" ")[1].replaceFirst("/", "");
            uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
            if (StringUtils.isBlank(uri)) {
                //加载index.html
                uri = "index.html";
            }
            String fileSeparator = System.getProperty("file.separator");
            String path = System.getProperty("user.dir").concat(fileSeparator).concat(RESOURCE_DIR).concat(fileSeparator).concat(uri);
            sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            //封装响应消息体
            String content;
            while ((content = sourceReader.readLine()) != null) {
                writer.write(content);
            }
            writer.flush();
            System.out.printf("当前时间 %s, 请求处理完毕，线程name = %s, 线程Id = %s\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), Thread.currentThread().getName(), Thread.currentThread().getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (sourceReader != null) {
                try {
                    sourceReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
