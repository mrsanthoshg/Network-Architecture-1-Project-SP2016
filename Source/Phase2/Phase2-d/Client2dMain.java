import java.net.*;
import java.io.*;

public class Client2dMain implements Runnable
{  private Socket socket              = null;
   private Thread thread              = null;
   private BufferedReader  console   = null;
   private DataOutputStream streamOut = null;
   private ThreadClient client    = null;
  
   public static void main(String args[])
   { 
	   Client2dMain client = null;

	   client = new Client2dMain("server.RakeshPhase1.ch-geni-net.instageni.umkc.edu",14567);
   }
   public Client2dMain(String serverName, int serverPort)
   { 

      try
      {  socket = new Socket(serverName, serverPort);
        
         start();
      }
      catch(Exception e)
      {  
    	  e.printStackTrace();
      }
   }
   public void run()
   {  while (thread != null)
      {  try
         {  streamOut.writeUTF(console.readLine());
            streamOut.flush();
         }
         catch(IOException e)
         { 
        	 e.printStackTrace();
        	 stop();
         }
      }
   }
   public void handle(String msg)
   {  
         System.out.println("From Server which is from all clients : "+msg);
   }
   public void start() throws IOException
   {  console   = new BufferedReader(new InputStreamReader(System.in)); 
      streamOut = new DataOutputStream(socket.getOutputStream());
      if (thread == null)
      {  
    	 client = new ThreadClient(this, socket);
         thread = new Thread(this);                   
         thread.start();
      }
   }
   public void stop()
   {  if (thread != null)
      {  thread.interrupt();  
         thread = null;
      }
      try
      {  if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException e)
      {  
    	  e.printStackTrace();
      }
      client.close();  
      client.interrupt();
   }
   public class ThreadClient extends Thread
   {  private Socket           socket   = null;
      private Client2dMain       client   = null;
      private DataInputStream  streamIn = null;

      public ThreadClient(Client2dMain _client, Socket _socket)
      {  client   = _client;
         socket   = _socket;
         open();  
         start();
      }
      public void open()
      {  try
         {  streamIn  = new DataInputStream(socket.getInputStream());
         }
         catch(IOException e)
         {  
       	  e.printStackTrace();
            client.stop();
         }
      }
      public void close()
      {  try
         {  if (streamIn != null) streamIn.close();
         }
         catch(IOException e)
         {  
       	  e.printStackTrace();
         }
      }
      public void run()
      {  while (true)
         {  try
            {  client.handle(streamIn.readUTF());
            }
            catch(IOException e)
            {  
           	 e.printStackTrace();
               client.stop();
            }
         }
      }
   }
}