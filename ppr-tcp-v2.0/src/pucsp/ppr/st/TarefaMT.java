package pucsp.ppr.st;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class TarefaMT extends TarefaST implements Runnable {
	Thread threadWorker;
	
	public TarefaMT(ServerSocket udpServerSocket, Socket cliReq) {
		super(udpServerSocket, cliReq);
	}

	@Override
	public void run() {
		// super nao é necessario, esta ai somente para evidenciar!!
		super.executarTarefa();
	}
	
	@Override
	public void executar() {
		threadWorker = new Thread(this);
		threadWorker.start();
	}
}

