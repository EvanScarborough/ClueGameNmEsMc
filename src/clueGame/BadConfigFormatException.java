package clueGame;

public class BadConfigFormatException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2481807765877473765L;

	private String s;
	
	BadConfigFormatException(){
		super("Ya done messed up.");
	}
	BadConfigFormatException(String s){
		super(s);
		this.s = s;
	}
	
	public String toString(){
		return s;
	}
	
}
