package ply.exceptions;

public class FormatPlyException extends Exception {

	private static final long serialVersionUID = 6677487610288558193L;

	public FormatPlyException() {
		this("Votre fichier ne respecte pas la convention PLY");
	}

	public FormatPlyException(String message) {
		super(message);
	}

	public FormatPlyException(String message, Throwable cause) {
		super(message, cause);
	}

}