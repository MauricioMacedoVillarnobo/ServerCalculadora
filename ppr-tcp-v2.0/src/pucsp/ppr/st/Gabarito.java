package pucsp.ppr.st;

import java.util.HashMap;
import java.util.Map;

class Gabarito {
	// <número da questão>;<número alternativas>;<respostas>
	private Map<Integer, Questao> questoes;
	public Gabarito() {
		questoes = new HashMap<Integer, Questao>();
	}
	
	public void iniciar() {
		//1;5;VVFFV
		//2;4;VVVV
		Questao q1 = new Questao(1,5,"VVFFV");
		Questao q2 = new Questao(2,3,"VVVV");
		addQuestao(q1);
		addQuestao(q2);
		
		addQuestao(new Questao(3,7,"FFVVFFF"));
		addQuestao(new Questao(4,5,"VVFFV"));
		addQuestao(new Questao(5,4,"VVVV"));
	}
	
	/*
	 * Carraga de um arquivo
	 */
	public void carregar(String nomeArquivo) {
		
	}

	public void addQuestao(Questao q) {
		questoes.put(q.getNum(), q);
	}
	
	public int getSize() {
		return questoes.size();
	}
	
	public Questao getQuestao(int i) {
		return questoes.get(i);
	}
}