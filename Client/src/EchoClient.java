import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.System.out;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port) {
        this.port = port;
        this.host = host;
    }
    public static EchoClient connectTo(int port){
        var localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }
    public void run(){
        out.printf("Type 'bye' to quit!%n%n");
        try(Socket socket = new Socket(host, port)){
            Scanner scanner = new Scanner(System.in, "UTF-8");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            InputStream input = socket.getInputStream();
            var isr = new InputStreamReader(input, "UTF-8");
            Scanner reader = new Scanner(isr);

            try(scanner; writer; reader){
                while (true){
                    String message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    String msg = reader.nextLine();
                    System.out.println("EchoServer: " + msg);

                    if("bye".equalsIgnoreCase(message)){
                        return;
                    }
                }
            }
        } catch (NoSuchElementException e){
            out.printf("Connection's dropped!%n");
        } catch (IOException e){
            out.printf("Can't connect to %s: %s!%n", host, port);
            e.printStackTrace();
        }
    }
}
