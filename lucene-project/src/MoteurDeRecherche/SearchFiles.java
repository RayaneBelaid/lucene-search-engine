package MoteurDeRecherche;

import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


public class SearchFiles {

	public SearchFiles(){}
	
	 /*#####################################################################################################*/
	 /* searchFiles :                                                                                       */
	 /* Cette m�thode analyse une requ�te d'entr�e de l'utilisateur                                         */
	 /*                                                                                                     */
	 /* @inputs:                                                                                            */
	 /* 		String req: la requete � analys�, e.g., "a+b"                                               */
	 /* 		String choice: le crit�re lequel sera effectu� la recherche, e.g., "author"                 */
	 /*                                                                                                     */
	 /*#####################################################################################################*/
	  
	@SuppressWarnings("unchecked")
	public void searchFiles(String req, String choice) throws ParseException, IOException {
		
		// cet argument indique combien de meilleurs r�sultats de recherche il doit retourner.
        int hitsPerPage = 3204;  
        
        // ouvrir l'index lucene en mode lecture et g�re la recherche
        IndexReader reader = DirectoryReader.open(IndexFiles.dir);  
        
        // permet de l'execution d'une recherche dans l'index et la recherche des documents pertinents
        IndexSearcher searcher = new IndexSearcher(reader); 

        TopDocs docs = null;
        ScoreDoc[] hits = null;
        int h;
        
        // initialiser notre boolean query
        BooleanQuery booleanQuery = null;
        
        // Appeler la m�thode parseQuery 
        booleanQuery = parseQuery(req, choice);
        
        /*#####################################################################################################*/
        /*                                                                                                     */
        /*                                      COMMENCER LA RECHERCHE                                         */
        /*                                                                                                     */
        /*#####################################################################################################*/
        
        //Permet de selectionner des documents pertinents (avec le score associ�) pour une requ�te
        docs = searcher.search(booleanQuery, hitsPerPage); 

        hits = docs.scoreDocs;

        h = hits.length;
        
        // vider la liste des r�sultats pour une nouvelle recherche
        GraphicalUserInterface.dlm.clear();
        
        // afficher le nombre des r�sultats trouv�s dans l'interface graphique
        if (hits.length == 0) {
             GraphicalUserInterface.dlm.addElement("Aucun article n'a �t� trouv� \n");
             }else if(hits.length == 1) { 
                	GraphicalUserInterface.dlm.addElement(+h+" Article a �t� trouv� \n");
                    }else {
        	            GraphicalUserInterface.dlm.addElement(+h+" Articles ont �t� trouv�s \n");
                        }
        
        for(int j = 0; j < hits.length; ++j) {
            int docId = hits[j].doc;
            Document document = null;
			document = searcher.doc(docId);
			String id = document.get("id") ;
			String title = document.get("title") ;

            //afficher la liste des r�sultats (les articles trouv�s) dans l'interface graphique 
			GraphicalUserInterface.dlm.addElement(j+1+" /  ID : "+ id+" -- Titre : " +title);
        }
		reader.close();
	}
	
	
	/*#####################################################################################################*/
	/*  parseQuery :                                                                                       */
	/*  Cette m�thode analyse une requ�te d'entr�e et la divise en sous-requ�tes.                          */
	/*  Ensuite, elle cr�e une requ�te bool�enne                                                           */
	/*                                                                                                     */
	/*  @inputs:                                                                                           */
	/*  		String inputQuery: la requ�te � analys�, e.g., "a+b"                                       */
	/*  		String searchCriteria: le crit�re lequel sera effectu� la recherche, e.g., "author"        */
	/*  @output:                                                                                           */
    /*		BooleanQuery booleanQuery: un BooleanQuery qui sera utilis� pour lancer la recherche           */
	/*                                                                                                     */
	/*#####################################################################################################*/
	
	private BooleanQuery parseQuery(String inputQuery, String searchCriteria){
		BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
		
		//Initialisation 
		BooleanQuery booleanQuery = null;
		char currentOperator;
        String currentQuery = "";
        int i;
        
		//v�rification et affectation (le premier �l�ment de la requete doit �tre pr�c�d� par un op�rateur "+" ou "-")
		if (inputQuery.startsWith("-") == false) {
			inputQuery = "+" + inputQuery;
		}
		
		i = 0;
		
		//initialiser le premier op�rateur
		//la m�thode "charAt()" r�cup�re le caract�re de l'indice associ�
		currentOperator = inputQuery.charAt(i); 
		++i;
		
		//la boucle avance � chaque it�ration par 1 caract�re 
		while (i < inputQuery.length()) {
			if (inputQuery.charAt(i) == '+' || inputQuery.charAt(i) == '|' || inputQuery.charAt(i) == '-') { 
			    // une nouvelle clause est rencontr�e
				
				// cr�ation de la requete query et l'ajout� � BooleanQuery
				// cr�ationn de l'objet query
				Query query;
				
				try {
					query = new QueryParser(searchCriteria, new StandardAnalyzer()).parse(currentQuery);
					if (currentOperator == '+') {
						//signifie que la clause est obligatoire.
						booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
						
					} else if (currentOperator == '-') {
						//signifie que la clause int�rdite
						booleanQueryBuilder.add(query, BooleanClause.Occur.MUST_NOT);
						
					} else if (currentOperator == '|') {
						//signifie que la clause est facultative
						booleanQueryBuilder.add(query, BooleanClause.Occur.SHOULD);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// vider la la requ�te courante (currentQuery)
				currentQuery = "";
				
				//mettre � jour l'op�rateur courant (currentOperator) pour la prochaine requ�te
				currentOperator = inputQuery.charAt(i);
				
			} else {
				
				//initialiser currentQuery par le caract�re suivant
				currentQuery = currentQuery + inputQuery.charAt(i); 
			}
			++i;
		}
		
		// r�cup�rer la derni�re requ�te (le dernier caract�re de l'�l�ment)
		// cr�ation de la requ�te "query" et l'ajout� � BooleanQuery
		// cr�ationn de l'objet query
		Query query;
		try {
			query = new QueryParser(searchCriteria, new StandardAnalyzer()).parse(currentQuery);
			if (currentOperator == '+') {
				
				//signifie que la clause est obligatoire.
				booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
				
			} else if (currentOperator == '-') {
				//signifie que la clause est interdite 
				booleanQueryBuilder.add(query, BooleanClause.Occur.MUST_NOT);
				
			} else if (currentOperator == '|') {
				//signifie que la clause est facultative 
				booleanQueryBuilder.add(query, BooleanClause.Occur.SHOULD);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		booleanQuery = booleanQueryBuilder.build();

		return booleanQuery;
	}
}
