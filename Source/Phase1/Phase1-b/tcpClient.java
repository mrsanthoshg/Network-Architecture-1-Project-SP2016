import java.io.*; 
import java.net.*; 
import java.util.Scanner;
class Client { 

	public static void main(String argv[]) throws Exception 
	{ 
		String sentence = ""; 
		String serverSentence=""; 

		BufferedReader inFromUser = 
				new BufferedReader(new InputStreamReader(System.in)); 
		System.out.println("Client is running");
		System.out.println("*****************************************");
		System.out.println("Choose below options");
		System.out.println("1. Send Messages\t2.Send File");
		Scanner scanner = new Scanner(System.in);
		int option = scanner.nextInt();

		if(option==1){
			//opening socket connection
			Socket clientSocket=null; 

			while(!serverSentence.startsWith("Bye from Server")){
				System.out.println("ENTER MESSAGE : ");
				clientSocket= new Socket("152.54.14.22", 4567);
				DataOutputStream outToServer = 
						new DataOutputStream(clientSocket.getOutputStream()); 
				BufferedReader inFromServer = 
						new BufferedReader(new
								InputStreamReader(clientSocket.getInputStream())); 

				sentence = inFromUser.readLine(); 

				outToServer.writeBytes(sentence + '\n'); 

				serverSentence = inFromServer.readLine(); 

				System.out.println("FROM SERVER: " + serverSentence); 

			}           
			clientSocket.close(); 
		}
		else if(option==2){
			System.out.println("Choose file");
		}
		else{
			System.out.println("Choose correct option");
		}

	} 
} 