package modele;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


public class RecuperationPly {
	
	public static void main(String[] args) {
		String fichier = "vache.ply";
		List<Point> res =recuperationCoordonnee(fichier);
		List<Trace> ensembleDePoint = recuperationTracerDesPoint(fichier, res);
		System.out.println("DEBUT");
		
		for(Trace p : ensembleDePoint) {
			System.out.println(p);
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
				
				String ligne=" ";

				RandomAccessFile raf = new RandomAccessFile(FichierPly, "r");
				placerApresLaTeteDuFichier(raf);
				String tab[]= new String[0];
				boolean finPoint=true;
				while(finPoint) {
					
					ligne = raf.readLine();
					
					if(nombreOccurence(ligne,' ')==3) {
						tab = ligne.split(" ");
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
	
	public static List<Trace> recuperationTracerDesPoint(String fichier, List<Point> points) {
		List<Trace> res = new ArrayList<Trace>();
		
		String myPath = System.getProperty("user.dir")
				+ File.separator + "src"
				+ File.separator + "test"
				+ File.separator;
		 
			File FichierPly = null ;
			
			try{
				
				FichierPly = new File(myPath+fichier);
				
				String ligne=" ";

				RandomAccessFile raf = new RandomAccessFile(FichierPly, "r");
				placerApresLaTeteDuFichier(raf);
				String tab[]= new String[0];

				
				while(ligne!=null) {
					
					ligne = raf.readLine();
					
					if(ligne!=null && nombreOccurence(ligne,' ')==4) {
						tab = ligne.split(" ");
						Trace ensembleDePoint = new Trace();
						for(int i=0; i< tab.length; i++) {
							ensembleDePoint.add(points.get(Integer.valueOf(tab[i])));
						}
						res.add(ensembleDePoint);
					}
					
					
					
				}
				
				raf.close();
			}
			catch(IOException o) {
				
				o.printStackTrace();
			}
	
			return res;
	}
	
	
	public static int nombreOccurence(String mes, char c) {
		int cpt=0;
		for(int i =0; i<mes.length();i++) {
			if(mes.charAt(i)==c) {
				cpt++;
			}
		}
		return cpt;
	}
	
	public static void placerApresLaTeteDuFichier(RandomAccessFile raf) {
		try
		{
			
			raf.seek(0);
			String ligne;
			ligne = raf.readLine();
			
			while(!ligne.contains("end_header")) {
				ligne = raf.readLine();
				
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// on se replace tout au début du fichier
		

	}
}
