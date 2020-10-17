package cn.edu.hbut.hfcrt.pojo;

public class Convergence {
	
	int Aconvergence;
	int Fconvergence;
	public int getAconvergence() {
		return Aconvergence;
	}
	public void setAconvergence(int aconvergence) {
		Aconvergence = aconvergence;
	}
	public int getFconvergence() {
		return Fconvergence;
	}
	public void setFconvergence(int fconvergence) {
		Fconvergence = fconvergence;
	}
	@Override
	public String toString() {
		return "Convergence [Aconvergence=" + Aconvergence + ", Fconvergence=" + Fconvergence + "]";
	}

}
