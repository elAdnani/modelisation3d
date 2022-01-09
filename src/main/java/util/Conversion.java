package util;
/**
 * 
 * Cette classe permet d'obtenir des conversions utiles à nos différents calcules ou affichages
 *
 */
public class Conversion {
	
	private Conversion() {
	}
	/**
	 * à partir d'un nombre en octet permet d'obtenir la conversion textuel de ce nombre.</br>
	 * Il peut être sous forme de: les octets, les kilos octets, les mega octets, les giga octets et les tera octets
	 * @param size taille en octet
	 * @return conversion textuel abrégé du nombre
	 */
	public static String getSize(long size) {

		long kilo = 1024;
		long mega = kilo * kilo;
		long giga = mega * kilo;
		long tera = giga * kilo;

		double formatKb = (double) size / kilo;
		double formatMb = formatKb / kilo;
		double formatGb = formatMb / kilo;
		double formatTb = formatGb / kilo;

		String res = "";

		if (size < kilo) {
			res = size + " Bytes";
		} else if (size < mega) {
			res = String.format("%.2f", formatKb) + " KB";
		} else if (size < giga) {
			res = String.format("%.2f", formatMb) + " MB";
		} else if (size < tera) {
			res = String.format("%.2f", formatGb) + " GB";
		} else {
			res = String.format("%.2f", formatTb) + " TB";
		}

		return res;
	}
	
	/**
	 * Réalise la transformation de degré en radian
	 * @param degree nombre en dégré
	 * @return nombre équivalent en radian
	 */
	public static double toRadian(double degree) {
		return degree * Math.PI / 180;
	}
	
	/**
	 * Réalise la moyenne des nanosecondes et réalise sa conversion en milliseconde.<br>
	 * 
	 * @param repetition moyenne
	 * @param nanoTime (temps en nanoTime)
	 * @return temps en milliseconds
	 */
	public double nanotimeToMiliseconds(int repetition, double nanoTime) {
		 return nanoTime / repetition / 1000 / 1000;
	}
	/**
	 * Réalise la transformation de nanoseconde en milliseconde.
	 * 
	 * @param nanoTime (temps en nanoTime)
	 * @return temps en milliseconds
	 */
	public double nanotimeToMiliseconds(double nanoTime) {
		 return nanotimeToMiliseconds(1,nanoTime);
	}
}
