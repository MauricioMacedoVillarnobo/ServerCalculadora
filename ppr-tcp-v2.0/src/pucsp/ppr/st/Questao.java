package pucsp.ppr.st;

class Questao {
	private int num;
	private int numAlt;
	private String repostas;
	
	public Questao(int num, int numAlt, String repostas) {
		super();
		this.num = num;
		this.numAlt = numAlt;
		this.repostas = repostas;
	}
	
	public int getNum() {
		return num;
	}
//	public void setNum(int num) {
//		this.num = num;
//	}
	public int getNumAlt() {
		return numAlt;
	}
//	public void setNumAlt(int numAlt) {
//		this.numAlt = numAlt;
//	}
	public String getRepostas() {
		return repostas;
	}
//	public void setRepostas(String repostas) {
//		this.repostas = repostas;
//	}
}