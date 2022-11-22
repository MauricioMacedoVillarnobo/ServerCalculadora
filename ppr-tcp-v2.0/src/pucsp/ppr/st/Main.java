package pucsp.ppr.st;

/*
 * arg[0]: porta do servidor
 * arg[1]: tipo de servidor. false: single-thread, true: multi-thread
 */
public class Main {
	public static void main(String args[]) {
		int port = 6789;
		if (args.length > 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch(Exception ex) {
				System.out.println("Port inválido, assumindo 6789");	
				port = 6789;
			}
		}
		
		String multiThread = "false";
		if (args.length > 1) { // true ou false ou nada
			multiThread = args[1].toLowerCase().trim();
		}
		boolean isMultiThread = "true".equals(multiThread) ? true : false; 
		
		String nome = "";
		if (isMultiThread) {
			nome = "Servidor Multi-thread";
		} else {
			nome = "Servidor Single-thread";
		}
		
		System.out.println(nome  + ": " + "iniciado no port: " + port);
		ServidorCorrecao server = new ServidorCorrecao(port, nome);
		server.setMultiThread(isMultiThread); // define se é multi-thread(true) ou single-thread(false)
		server.iniciar();
		server.terminar();
	}
}
