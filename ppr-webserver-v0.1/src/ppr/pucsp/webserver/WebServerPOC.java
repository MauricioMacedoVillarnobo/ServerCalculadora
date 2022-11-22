package ppr.pucsp.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/*
 * Prova de Conceito de um servidor web simples
 */
public class WebServerPOC {
	private int port = 8080; // essa eh a porta utilizada normalmente para testes de webserver
	private static final String newLine = "\r\n";

	public WebServerPOC(int port) {
		this.port = port;
	}

	public void execute() 	{
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			System.out.println("Servidor Web iniciado na porta " + port);
			while (true)  {
				Socket connection = socket.accept();
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					OutputStream out = new BufferedOutputStream(connection.getOutputStream());
					PrintStream pout = new PrintStream(out);
					// lendo a primeira linha do request
					String request = in.readLine();
					if (request == null) {
						continue;
					}
					// ignoramos o resto
					while (true) {
						String ignore = in.readLine();
						if (ignore == null || ignore.length() == 0) { 
							break;
						}
					}
					if (!request.startsWith("GET ") || !(request.endsWith(" HTTP/1.0") || request.endsWith(" HTTP/1.1"))) {
						// bad request
						pout.print("HTTP/1.0 400 Bad Request" + newLine + newLine);
					} else 	{
						Date date = new Date();
						String response = "Oiee, eu estou vivo: " + date;
						pout.print(
								"HTTP/1.0 200 OK" + newLine + 
								"Content-Type: text/plain" + newLine + 
								"Date: "+ date + newLine + 
								"Content-length: " + response.length() + newLine + newLine + 
								response
								);
					}
					pout.close();
				} catch (Throwable tri) 	{
					System.err.println("Erro recebendo requisicao: "+tri);
				}
			}
		} catch (Throwable tr) {
			System.err.println("Nao conseguiu iniciar o servidor web: "+tr);
		} finally {
			close(socket);
		}
	}

	private void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			/* ... */
		}
	}

	public static void main(String[ ] args) 	{
		int port = 8080;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		WebServerPOC server = new WebServerPOC(port);
		server.execute();
	}
}
