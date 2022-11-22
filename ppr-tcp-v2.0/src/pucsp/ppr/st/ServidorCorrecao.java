package pucsp.ppr.st;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
/*
 * Servidor.java
 *
 */

public class ServidorCorrecao {
	private int port;
	private String nome;
	private boolean paused;
	private boolean forever;
	private boolean isMultiThread;

	private ServerSocket tcpServerSocket;

	public ServidorCorrecao(int port) {
		this(port, "Servidor TCP");
	}

	public ServidorCorrecao(int port, String nome) {
		this.port = port;
		if (nome != null && !nome.trim().isEmpty()) {
			this.nome = nome;
		} else {
			this.nome = "Servidor TCP";
		}
		tcpServerSocket = null;
		paused = true;
		forever = false;
		isMultiThread = false;
	}

	public boolean isMultiThread() {
		return isMultiThread;
	}

	public void setMultiThread(boolean multiThread) {
		this.isMultiThread = multiThread;
	}

	// "QUESTAO" + "1;5;VVFFV"
	void iniciar() {
		try {
			paused = false;
			forever = true;
			Socket clientRequest = null;
			while (forever) {
				log("aguardando request");
				try {
					if (tcpServerSocket == null || tcpServerSocket.isClosed()) {
						tcpServerSocket = new ServerSocket(port); // cria um socket TCP
						tcpServerSocket.setSoTimeout(3*1000); // timeout de 3 seg
					}
					clientRequest  = tcpServerSocket.accept();
				} catch (SocketTimeoutException stex) {
					log("cansou de esperar...\"reiniciando\"");
					continue;
				}
				DataInputStream dis =  new DataInputStream(clientRequest.getInputStream());
				DataOutputStream dos =  new DataOutputStream(clientRequest.getOutputStream());
				String cmd =dis.readUTF(); // 1a leitura é o comando [CTRL|QUESTAO|QUESTOES]
				log("REQUEST:" + cmd);
				/*
				 * Agora vamos processar o protocolo
				 * 	 Protocolo: 
				 * 		CTRL:[RESTART,START,PAUSE, STOP,LOG]
				 * 		QUESTAO:1;5;VVFFV
				 *      QUESTOES:2;4;VVVV:1;5;VVFFV
				 */
				if ("CTRL".equalsIgnoreCase(cmd)) {
					String action =dis.readUTF().toUpperCase(); // 2a leitura é a acao  de controle
					switch(action) {
					case "STOP":
						log("Parando o servidor...");
						dos.writeUTF("Parando o servidor...");
						terminar(); // fecha em definitivo o ServerSocket
						continue;
					case "RESTART":
						log("Reiniciando o servidor...");
						dos.writeUTF("Reiniciando o servidor...");
						closeServerSocket();
						paused = false;
						continue;
					case "START":
						log("Iniciando o servidor...");
						dos.writeUTF("Iniciando o servidor...");
						paused = false;
						continue;
					case "PAUSE":
						if (paused) {
							dos.writeUTF("Servidor já esta pausado..");
							continue;
						}
						log("Pausando o servidor...");
						dos.writeUTF("Pausando o servidor...");
						paused = true;
						continue;
					case "LOG":
						String pauseStatus =  paused? "-->pausado" :  "-->iniciado no port: " + port;
						String msg =  nome + ": " + pauseStatus;
						log("Retornando o log do servidor...");
						dos.writeUTF("LOG-" + msg);
						continue;
					} // switch
				} else if ("QUESTAO".equalsIgnoreCase(cmd) ||  "QUESTOES".equalsIgnoreCase(cmd)) {
					if (paused) {
						dos.writeUTF("Servidor esta pausado..");
						continue;
					}
				}  else {
					if (paused) {
						dos.writeUTF("Servidor esta pausado..");
						continue;
					}
					dos.writeUTF("ERRO NA REQUISICAO");
					continue;
				}
				/*
				 * Executa em modo MultiThread ou SingleThread
				 * de acordo com flag isMultiThread
				 * Veja o Main.java
				 */
				if (this.isMultiThread()) {
					TarefaMT tarefaMT = new TarefaMT(tcpServerSocket, clientRequest);
					tarefaMT.executar();
				} else {
					TarefaST tarefaST = new TarefaST(tcpServerSocket, clientRequest);
					tarefaST.executar();
				}
			} // end while
			log("parou");

		} catch (SocketException sex) {
			log("Erro de socket: " + sex.getMessage());
			sex.printStackTrace();
		} catch (IOException ioex) {
			log("Erro envio/recepcao pacote: " + ioex.getMessage());
		} catch (Exception ex) {
			log("Erro inesperado: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			closeServerSocket();
		} 
	}


	public void log(String msg) {
		System.out.println("\n" + nome + ":  " + msg);
	}

	public void terminar() {
		closeServerSocket();
		forever = false;
	}

	private void closeServerSocket() {
		try {
			if (tcpServerSocket != null) {
				tcpServerSocket.close();
				tcpServerSocket = null;
			}
		} catch (IOException e) { /* ... */ }
	}
}