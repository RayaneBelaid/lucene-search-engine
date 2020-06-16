package MoteurDeRecherche;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;


    /*#####################################################################################################*/
    /*                                                                                                     */
    /*                                      INDEXATION                                                     */
    /*                                                                                                     */
    /*#####################################################################################################*/


public class IndexFiles {
	
	 /*#####################################################################################################*/
	 /*                                                                                                     */
	 /* Indexer :                                                                                           */
	 /* cette m�thode indexe les documents de la collection CACM d"une mani�re automatique                  */
	 /*                                                                                                     */
	 /* @inputs:                                                                                            */
	 /* 		StandardAnalyzer analyzer: le document qui etre ind�x�                                      */
	 /*                                                                                                     */
	 /*#####################################################################################################*/
	  

	static Directory dir;
	
    // Constructeur
	public IndexFiles() {}

	public static void Indexer(StandardAnalyzer analyzer) throws IOException{
		
		// Chemin de la collection CACM 
		String path = "F:\\Informa\\L3-ISIL\\RI\\outils\\projet_Ri\\CACM";
		
		// Chemin de l'index CACM
		String indexDirectoryPath = "F:\\Informa\\L3-ISIL\\RI\\outils\\projet_Ri\\IndexCACM"; 
			
		// Ce r�pertoire contiendra les indexes
		dir = FSDirectory.open(Paths.get(indexDirectoryPath));
        
		// Cr�er une liste de fichiers en lui associant le chemin de CACM
		File[] files = new File(path).listFiles(); 
		
		analyzer = new StandardAnalyzer();
		
		// Analyser le text
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		// D�finir le mode � CREATE � afin de supprimer tous les fichiers index�s existants dans le r�pertoire � index �
		config.setOpenMode(OpenMode.CREATE);

		IndexWriter iwriter = new IndexWriter(dir, config);
         
		// Cr�er l'objet xmlFileFilter de la classe XmlFileFilter
		XmlFileFilter xmlFileFilter = new XmlFileFilter();
		
		// Cr�er l'objet xmlFileParser de la classe xmlFileParser
		XmlFileParser xmlFileParser = new XmlFileParser();
        
		
		for (File file : files) {
			File fXmlFile = new File(path+"\\"+file.getName());
			try {
				// V�rifier si le fichier est de type XML en Applant la m�thode � accept() � de la classe � XmlFileFilter �  
				if(xmlFileFilter.accept(fXmlFile) == true) {
					
					// Cr�er un document 
					ArrayList<Document> documents = new ArrayList<Document>();
                    
					// Appeler la m�thode � parser(String filename ) � de la classe � XmlFileParser �
					documents = xmlFileParser.parser(file.getAbsolutePath());
				
					for (Document doc : documents) {
						//System.out.println("document : " + doc);
						// Indexer le document
						iwriter.addDocument(doc);
					}
			      } 
			    } catch(Exception e) {} 
		      }
	  iwriter.close();
    }
  }
