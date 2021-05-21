import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HTTPServerSkeleton {
    static final int PORT = 6799;

    public static String readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return String.valueOf(fileData);
    }
    
    public static void main(String[] args) throws IOException, BindException {
        
        ServerSocket serverConnect = new ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

        //open Thread
        while(true){
            Socket s = serverConnect.accept();
            Thread serverWorker = new ServerWorker(s);
            serverWorker.start();

        }

    }
    
}
