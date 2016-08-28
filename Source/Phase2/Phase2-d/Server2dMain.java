import java.net.*;
import java.io.*;

public class Server2dMain implements Runnable
{  
	private ServerThread C[] = new ServerThread[20];
	private ServerSocket Ssocket = null;
	private Thread       thread = null;
	private int countClients = 0;

	public Server2dMain(int port)
	{ 
		try
		{  
			Ssocket = new ServerSocket(port);  
			System.out.println("**********Server running************ " );
			start(); 
		}
		catch(IOException ioe)
		{ 
			ioe.printStackTrace();
			System.out.println("**********Exception created************ ");
		}
	}
   public void run()
   {  while (thread != null)
      {  try
         {  
            createThread(Ssocket.accept()); }
         catch(IOException ioe)
         {  
        	stop(); 
         }
      }
   }
   public void start()  
   {
	   if (thread == null)
	      {  thread = new Thread(this); 
	         thread.start();
	      }
   }
   public void stop()   { 
	  
	   if (thread != null)
	      {  thread.interrupt(); 
	         thread = null;
	      }
	   }

   public synchronized void handle(int ID, String input)
   { 
         for (int i = 0; i < countClients; i++)
            C[i].send( input);   
   }
  
   private void createThread(Socket socket)
   {  
        
         C[countClients] = new ServerThread(this, socket);
         try
         {  C[countClients].open(); 
         	C[countClients].start();  
         	countClients++; 
         }
         catch(IOException e)
         { 
        	e.printStackTrace();
         } 
      
   }
   public static void main(String args[]) { 

	   Server2dMain server = null;

	   server = new Server2dMain(14567);
   }
   public class ServerThread extends Thread
   {  private Server2dMain server    = null;
      private Socket           socket    = null;
      private int              ID        = -1;
      private DataInputStream  streamIn  =  null;
      private DataOutputStream streamOut = null;

      public ServerThread(Server2dMain _server, Socket _socket)
      {  super();
         server = _server;
         socket = _socket;
         ID     = socket.getPort();
      }
      public void send(String msg)
      {   try
          {  streamOut.writeUTF(msg);
             streamOut.flush();
          }
          catch(IOException ioe)
          { 
       	   ioe.printStackTrace();
             stop();
          }
      }
      public int getID()
      {  return ID;
      }
      public void run()
      { 
    	  System.out.println("Client Connected \n running this thread for each client");
         while (true)
         {  try
            {  server.handle(ID, streamIn.readUTF());
            }
            catch(IOException ioe)
            { 
           	 ioe.printStackTrace();
               stop();
            }
         }
      }
      public void open() throws IOException
      {  streamIn = new DataInputStream(new 
                           BufferedInputStream(socket.getInputStream()));
         streamOut = new DataOutputStream(new
                           BufferedOutputStream(socket.getOutputStream()));
      }
      public void close() throws IOException
      {  if (socket != null)    socket.close();
         if (streamIn != null)  streamIn.close();
         if (streamOut != null) streamOut.close();
      }
   }
}