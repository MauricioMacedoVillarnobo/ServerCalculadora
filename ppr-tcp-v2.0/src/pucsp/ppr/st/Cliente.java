package pucsp.ppr.st;
/*
 * Cliente.java
 *
 *
 * Argumentos: <mensage> <HostIP>
 * Ex. java Cliente 127.0.0.1 6789 "mensagem teste"
 * O servidor devolve a msg (eco) invertida
 */
import java.util.Scanner;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Cliente {
	public static void main(String args[]) {
		DataInputStream dis =  null;
		DataOutputStream dos =  null;
		Socket clientSocket = null;

		String servidor="localhost";
		int porta = 6789;

		if (args.length > 0) {
			servidor = args[0];
		}
		if (args.length > 1)  {
			try {
				porta = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				System.out.println("Cliente: Server port inválido, assumindo 6789");
				porta = 6789;
			}
		}

		boolean continuar = true;
		while(continuar) {
			try {
				System.out.print("Digite o tipo de mensagem: ");
				String tipo = new Scanner(System.in).nextLine();
				System.out.print("Digite o tipo de mensagem: ");
				String conteudo = new Scanner(System.in).nextLine();
				InetAddress serv = InetAddress.getByName(servidor);
				clientSocket = new Socket(serv, porta); // cria um socket UDP
				try {
					dis =  new DataInputStream(clientSocket.getInputStream());
					dos =  new DataOutputStream(clientSocket.getOutputStream());
				} catch (IOException ex) {
					ex.printStackTrace();
					return;
				}
				System.out.println("Cliente: Socket criado na porta: " + clientSocket.getLocalPort());
				/*
				 * Request
				 * Protocolo: 
				 * 		CTRL
				 *     RESTART|START|PAUSE|STOP|LOG
				 * 		QUESTAO
				 * 		1;5;VVFFV
				 *      QUESTOES
				 *      2;4;VVVV:1;5;VVFFV
				 *    
				 */
				dos.writeUTF(tipo); // envia datagrama contendo a mensagem m
				dos.writeUTF(conteudo); // envia datagrama contendo a mensagem m
				System.out.println("Cliente: requeste enviado...: " + tipo + " --> " + conteudo);
				/*
				 * Espera Response
				 */
				clientSocket.setSoTimeout(1000*1000); // timeout em ms
				String resposta = dis.readUTF(); // aguarda resposta do servidor - bloqueante
				System.out.println("Cliente: Resposta do servidor: " + resposta);
			} catch (SocketTimeoutException stex) {
				// timeout, erro na criação
				System.out.println("Cliente: Oops Timeout " + stex.getMessage());
			} catch (SocketException sex) {
				System.out.println("Cliente: Oops Erro socket: " + sex.getMessage());
			} catch (IOException ioex) {
				System.out.println("Cliente: Erro envio/recepcao do pacote: " + ioex.getMessage());
			}  finally {
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
		} // end while
		System.out.println("Terminando Cliente");
	}

}
