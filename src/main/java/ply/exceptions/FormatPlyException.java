package ply.exceptions;
/**
 * 
 * Cette classe sert à définir une erreur PLY
 *
 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
 * IUT-A Informatique, Universite de Lille.
 * @date 16 dec. 2022
 */
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