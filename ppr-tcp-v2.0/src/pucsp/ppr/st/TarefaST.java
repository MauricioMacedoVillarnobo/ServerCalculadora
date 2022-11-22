package pucsp.ppr.st;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class TarefaST {
	ServerSocket tcpServerSocket;
	Socket clientRequest;
	GerenciadorGabarito gGab;

	TarefaST(ServerSocket tcpServerSocket, Socket clientRequest) {
		this.tcpServerSocket = tcpServerSocket;
		this.clientRequest = clientRequest;
		gGab = new GerenciadorGabarito();
	}

	/*
	 * Recebe os dados do DatagramPacket: cliReq
	 * Processa
	 * Produz resposta
	 * Envia resposta pelo DatagramSocket: udpSocket
	 */
	protected void executarTarefa() {
		DataInputStream dis =  null;
		DataOutputStream dos =  null;

		try {
			dis =  new DataInputStream(clientRequest.getInputStream());
			dos =  new DataOutputStream(clientRequest.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		System.out.println("\nThreadId: " + Thread.currentThread().getId());
		String request;
		try {
			request = dis.readUTF(); // 2a leitura sao as questoes
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String format  = "\nServidorST: Request recebido de: %s:%s";
		System.out.printf(format, clientRequest.getInetAddress().getHostName(), clientRequest.getPort());
		String response = null;
		try {
			String questoes[ ] = request.split(":");
			boolean primeiro = true;
			for (int i = 0; i < questoes.length; i++) {
				if (primeiro) {
					response = gGab.conferirQuestoes(questoes[i]);
					primeiro = false;
				} else {
					response +=  ":" + gGab.conferirQuestoes(questoes[i]);
				}
			}
		} catch (QuestoesExcetion e) {
			e.printStackTrace();
			response = "ERRO NO PROCESSAMENTO";
		}
		try {
			dos.writeUTF(response);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void executar() {
		executarTarefa();
	}
}
