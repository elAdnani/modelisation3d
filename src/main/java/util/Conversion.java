package util;

public class Conversion {
	
	private Conversion() {
	}
	public static String getSize(long size) {
		String res = "";

		long kilo = 1024;
		long mega = kilo * kilo;
		long giga = mega * kilo;
		long tera = giga * kilo;

		double formatKb = (double) size / kilo;
		double formatMb = formatKb / kilo;
		double formatGb = formatMb / kilo;
		double formatTb = formatGb / kilo;

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
}
