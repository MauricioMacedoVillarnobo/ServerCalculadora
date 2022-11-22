package pucsp.ppr.st;

public class GerenciadorGabarito {
	private Gabarito gabarito;

	public GerenciadorGabarito() {
		gabarito = new Gabarito();
		gabarito.iniciar();
		gabarito.carregar("gabarito.txt");
	}

	public String conferirQuestoes(String request) throws QuestoesExcetion {
		/* PRTOCOLO */
		/*
		 * 
		 Preciso ter um gabarito
		 <número da questão>;<número alternativas>;<respostas>
			Exemplo Request:
				1;5;VVFFV
				2;4;VVVV

		<número da questão>;<número acertos>;<número erros>
			Exemplo Response:
				Questão 1: acertos=5 erros=3
				Questão 2: acertos=4 erros=4

		 */

		
		String[] array = request.split(";");
		//1;5;VVFFV
		if (array.length != 3) {
			log("request invalido");
			new QuestoesExcetion("Request inválido");
		}
		int nq = 0; // numero da questao
		//1;5;VVFFV
		try {
			nq = Integer.parseInt(array[0]);
		} catch (NumberFormatException e1) {
			new QuestoesExcetion("Numero de questao inválido");
		}
		/*
		 * Python, C
		 * LED: Java
		 * LP - C
		 * LIC : intro a comp: Python
		 * LPS: proj soft 
		 * CGPI, AS, Comp(iladores)
		 * 
		 * Tratamento de execoes: LED
		 * 
		 */
		int na = 0; // numero de alternativas
		//1;5;VVFFV
		try {
			na = Integer.parseInt(array[1]);
		} catch (NumberFormatException e2) {
			new QuestoesExcetion("Numero de alternativas inválido");
		} 

		String alts = array[2]; // as alternativas
		if (alts.trim().length() != na) {
			new QuestoesExcetion("Alternativas inválidas");
		}
		
		Questao que = new Questao(nq, na, alts);
		String resp = conferirQuestao(que);
		return resp;
	}
	
	private String conferirQuestao(Questao que) throws QuestoesExcetion{
		int nq = que.getNum();
		int na = que.getNumAlt();
		Questao queGab = gabarito.getQuestao(nq);
		if (na != queGab.getNumAlt()) {
			new QuestoesExcetion("Numeros de alternativa diferente do gabarito");
		}
		String alts = que.getRepostas().toUpperCase();
		String gabAlts = queGab.getRepostas().toUpperCase();
		int numAcertos = 0;
		int numErros = 0;
		for(int i = 0; i < na; i++) {
			if (alts.charAt(i) == gabAlts.charAt(i)) {
				numAcertos++;
			} else {
				numErros++;
			}
		}
		
		String format = "%d;%d;%d";
		String resp = String.format(format, nq, numAcertos, numErros);
		return resp;
	}
	
	public void log(String msg) {
		System.out.println("GerenciadorGabarito: " + msg);
	}
}
