package modele;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RecuperationPly {
	
	public static void main(String[] args) {
		List<Point> res =recuperationCoordonnee("vache.ply");
		
		System.out.println("DEBUT");
		for(Point p : res) {
			System.out.println(p.getX() +" "+ p.getY() +" "+ p.getZ());
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static List<Point> recuperationCoordonnee(String fichier){
		List<Point> res = new ArrayList<Point>();
		
		String myPath = System.getProperty("user.dir")
				+ File.separator + "src"
				+ File.separator + "test"
				+ File.separator;
		 
			File FichierPly = null ;
			
			try{
				
				FichierPly = new File(myPath+fichier);
				// on récupère un fichier qui possède le plateau correspondant
				String ligne=" ";

				RandomAccessFile raf = new RandomAccessFile(FichierPly, "r");
				raf.seek(0);
				// on se replace tout au début du fichier

				ligne = raf.readLine();
				
				while(!ligne.contains("end_header")) {
					ligne = raf.readLine();
					
				}
				String tab[]= new String[0];
				boolean finPoint=true;
				while(finPoint) {
					
					ligne = raf.readLine();
					tab = ligne.split(" ");
					if(tab.length==3) {
						
						res.add(new Point(Double.valueOf(tab[0]), Double.valueOf(tab[1]), Double.valueOf(tab[2])) );
					}
					else {
						finPoint=false;
					}
					
					
				}
				
				raf.close();
			}
			catch(IOException o) {
				
				o.printStackTrace();
			}
	
			
			
		return res;
	}
}
