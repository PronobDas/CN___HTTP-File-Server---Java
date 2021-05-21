import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        Socket s = new Socket("localhost",6799);


        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();

        int count;
        File file = new File("str");
        byte[] buffer = new byte[1024];

        OutputStream out = s.getOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

        //out.write(100);
        while( (count = in.read(buffer)) > 0 )
        {
            out.write(buffer,0,count);
        }



    }
}
