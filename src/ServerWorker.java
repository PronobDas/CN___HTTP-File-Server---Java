import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

public class ServerWorker extends Thread {
    Socket s;

    public ServerWorker(Socket s){
        this.s = s;
    }


    public void run(){
        try {

            File file = new File("index.html");
            FileInputStream fis = new FileInputStream(file);

            OutputStream os = s.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            File log = new File("log.txt");
            PrintWriter logpr = new PrintWriter(new FileWriter("log.txt"));

          /*  while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }*/

            String content; // = sb.toString();

            while(true)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                PrintWriter pr = new PrintWriter(os);
                String input = in.readLine();
                System.out.println(input);

                //String content = "<html>Hello</html>";
                if(input == null) continue;
                if(input.length() > 0) {
                    if(input.startsWith("GET"))
                    {
                        logpr.print("HTTP Request Type : GET \nHTTP Response:");


                        content = " ";
                        sb.setLength(0);
                        StringTokenizer sTokenizer = new StringTokenizer(input, " ");
                        sTokenizer.nextToken();
                        String path = sTokenizer.nextToken();
                        path = path.replace("%20"," ");
                        System.out.println(path);

                        File f = new File(path);
                        if(f.isDirectory())
                        {
                            PrintWriter printWriter=new PrintWriter(new FileWriter("index.html"));
                            printWriter.print("<html> \n <head> \n <meta http-equiv=\"content-type|default-style|refresh\"> \n </head> \n <body> \n" );
                            String[] pathNames = f.list();
                            for(String p : pathNames ){
                                if( p.charAt(0) == '.' )
                                    continue;
                                if( new File(path + "/" + p).isDirectory() )
                                    printWriter.print("<a href=\""+ path + "/" + p + "\"" + ">" + p + "</a><br> \n");
                                else
                                    printWriter.print("<a href=\""+ path + "/" + p + "\"" + ">" + p + "</a><br> \n");

                            }
                            printWriter.print("</body> \n </html>");
                            printWriter.close();

                            File file2=new File("index.html");
                            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file2),"UTF-8"));
                            while(( line = bufferedReader.readLine()) != null ) {
                                sb.append( line );
                                sb.append( '\n' );
                            }
                            content = sb.toString();

                            pr.write("HTTP/1.1 200 OK\r\n");
                            pr.write("Server: Java HTTP Server: 1.0\r\n");
                            pr.write("Date: " + new Date() + "\r\n");
                            pr.write("Content-Type: text/html\r\n");
                            pr.write("Content-Length: " + content.length() + "\r\n");
                            pr.write("\r\n");
                            pr.write(content);
                            pr.flush();

                            logpr.write("HTTP/1.1 200 OK\r\n");
                            logpr.write("Server: Java HTTP Server: 1.0\r\n");
                            logpr.write("Date: " + new Date() + "\r\n");
                            logpr.write("Content-Type: text/html\r\n");
                            logpr.write("Content-Length: " + content.length() + "\r\n");
                            logpr.write("\r\n");
                            logpr.write(content);
                            logpr.flush();

                        }
                        else if(f.isFile())
                        {
                          //  System.out.println(f);
                            pr.write("HTTP/1.1 200 OK\r\n");
                            pr.write("Server: Java HTTP Server: 1.0\r\n");
                            pr.write("Date: " + new Date() + "\r\n");
                            pr.write("Content-Type: application/x-forcedownload\r\n");
                           // System.out.println(f.length());
                            pr.write("Content-Length: " + f.length() + "\r\n");
                            pr.write("\r\n");
                            //pr.write(content);
                            pr.flush();

                            logpr.write("HTTP/1.1 200 OK\r\n");
                            logpr.write("Server: Java HTTP Server: 1.0\r\n");
                            logpr.write("Date: " + new Date() + "\r\n");
                            logpr.write("Content-Type: application/x-forcedownload\r\n");
                            // System.out.println(f.length());
                            logpr.write("Content-Length: " + f.length() + "\r\n");
                            logpr.write("\r\n");
                            logpr.write(content);
                            logpr.flush();

                            FileInputStream fis2 = new FileInputStream(f);
                            //BufferedReader br2 = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                            BufferedInputStream bis = new BufferedInputStream(fis2);
                            byte[] buffer = new byte[1024];
                            int n = 0;
                            while( (n = bis.read(buffer) ) > 0 )
                            {
                                os.write(buffer,0,n);
                            }

                        }
                        else
                        {
                            System.out.println("ERROR 404: PAGE NOT FOUND");
                            content = "ERROR 404: PAGE NOT FOUND.";

                            pr.write("HTTP/1.1 404 NOT FOUND\r\n");
                            pr.write("Server: Java HTTP Server: 1.0\r\n");
                            pr.write("Date: " + new Date() + "\r\n");
                            pr.write("Content-Type: text/html\r\n");
                            pr.write("Content-Length: " + content.length() + "\r\n");
                            pr.write("\r\n");
                            pr.write(content);
                            pr.flush();

                            logpr.write("HTTP/1.1 404 NOT FOUND\r\n");
                            logpr.write("Server: Java HTTP Server: 1.0\r\n");
                            logpr.write("Date: " + new Date() + "\r\n");
                            logpr.write("Content-Type: text/html\r\n");
                            logpr.write("Content-Length: " + content.length() + "\r\n");
                            logpr.write("\r\n");
                            logpr.write(content);
                            logpr.flush();
                        }

                    }

                    else
                    {
                        System.out.println("Sender Connected.");



                    }
                }
                logpr.close();

                pr.close();

                s.close();
            }

        } catch ( Exception e) {
           // e.printStackTrace();
        }
    }

}
