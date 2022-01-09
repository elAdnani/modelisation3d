package modele;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import math.*;
import modele.geometrique.*;
import ply.RecuperationPly;
import ply.exceptions.FormatPlyException;
import modele.*;
	/**
	 * 
	 * Cette classe sert à mesurer les capacités de récupération des points.<br>
	 * Il nous permettra également de passer de la liste des points aux matrices. <br>
	 * Il nous permet également de tester plusieurs fichiers ply à notre guise. ( Et voir la conformité d'un fichier, s'il récupère bien les points et les faces) <br>
	 * 
	 *
	 * @author <a href="mailto:adnan.kouakoua.etu@univ-lille.fr">Adnân KOUAKOUA</a>
	 * IUT-A Informatique, Universite de Lille.
	 * @date 11 nov. 2021
	 */
	public class RecuperationPlyTest {

		/**
		 * Chemin pour arriver jusqu'au dossier pour les .ply
		 */
		private final static String myPathRessource= System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
				+ File.separator + "resources" + File.separator;
		
		/**
		 *  Attribut de calcul de temps
		 */
		private long startTime;
		private long endTime;
		private int repetition;
		/**
		 *  Exemple de récupération d'un fichier PLY
		 */
		private final String fichier="chopper.ply";
		private final int VERTEX = 1066;
		private final int FACE = 2094;

		
		
		// TESTS
		
		@BeforeAll
		public static void beforeAllTests() {
			System.out.println("Début des tests pour la classe RecuperationPlyTest\n");

		}
		
		@AfterAll
		public static void afterAllTests() {
			System.out.println("Fin des tests de RecuperationPlyTest");

		}
		
		@BeforeEach
		public void setUp() {
			this.repetition=100;
				
		}
		
		@Test
		public void testLectureHeader() throws Exception {
			final int points =133009;
			final int faces = 248999;
			
			assertEquals(RecuperationPly.getNbVertices(myPathRessource+"header.ply"), points);
			assertEquals(RecuperationPly.getNbFaces(myPathRessource+"header.ply"), faces);
			
			assertEquals(VERTEX,RecuperationPly.getNbVertices(myPathRessource+fichier));
			assertEquals(FACE,RecuperationPly.getNbFaces(myPathRessource+fichier));
			
		
		}
		
		@Test
		public void testErreurLectureHeader() {
			assertThrows(FormatPlyException.class, () -> RecuperationPly.readFile(myPathRessource+"headerWithoutFormatPly.ply"));
			assertThrows(FormatPlyException.class, () -> RecuperationPly.readFile(myPathRessource+"nullPointeur"));
			assertThrows(FormatPlyException.class, () -> RecuperationPly.readFile(myPathRessource+"nullPointeurAvecFormatPly.ply"));
			assertThrows(FormatPlyException.class, () -> RecuperationPly.readFile(myPathRessource+"headerWithoutPlyLine.ply"));
			
			assertThrows(FormatPlyException.class, () -> RecuperationPly.checkFormat(myPathRessource+"headerWithoutFormatPly"));
			assertThrows(FormatPlyException.class, () -> RecuperationPly.checkFormat(myPathRessource+"nullPointeur"));
			assertThrows(FormatPlyException.class, () -> RecuperationPly.checkFormat(myPathRessource+"nullPointeurAvecFormatPly.ply"));
			
		}
		
		

	
}