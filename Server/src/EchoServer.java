import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class EchoServer {
    private final int port;
    private EchoServer(int port) {
        this.port = port;
    }
    static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }
    public void run() {
        try(ServerSocket server = new ServerSocket(port)){
                try (var clientSocket = server.accept()) {
                    handle(clientSocket);
                }
        } catch (IOException e){
            String fmtMsg = "Port %s is busy!%n";
            System.out.printf(fmtMsg, port);
            e.printStackTrace();
        }
    }
    public void handle(Socket socket) throws IOException {
        var input = socket.getInputStream();
        var isr = new InputStreamReader(input, "UTF-8");
        var scanner = new Scanner(isr);

        Scanner sc = new Scanner(System.in, "UTF-8");
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        try (scanner; writer; sc) {
            while (true) {
                var message = scanner.nextLine().strip();
                System.out.printf("Got: %s%n", message);
//                String msg = sc.nextLine();
                StringBuilder stringBuilder = new StringBuilder(message).reverse();
                writer.write(String.valueOf(stringBuilder));
                writer.write(System.lineSeparator());
                writer.flush();
                if("bye".equalsIgnoreCase(message)){
                    System.out.printf("Bye bye!%n");
                    return;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Client dropped the connection!%n");
        }
    }
}