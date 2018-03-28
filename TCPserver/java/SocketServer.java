import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mauropiva on 27/03/18.
 */
public class SocketServer {

    public static void main(String[] args){
        ServerSocket server;
        Integer portNumber = 40000;
        String line;
        DataInputStream is;
        PrintStream os;
        Socket clientSocket = null;
        try {
            server = new ServerSocket(9999);
            clientSocket = server.accept();
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());

                line = is.readLine();
                os.println(line);

            clientSocket.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
