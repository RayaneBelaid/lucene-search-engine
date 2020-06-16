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
	 /* Cette méthode analyse une requête d'entrée de l'utilisateur                                         */
	 /*                                                                                                     */
	 /* @inputs:                                                                                            */
	 /* 		String req: la requete à analysé, e.g., "a+b"                                               */
	 /* 		String choice: le critère lequel sera effectué la recherche, e.g., "author"                 */
	 /*                                                                                                     */
	 /*#####################################################################################################*/
	  
	@SuppressWarnings("unchecked")
	public void searchFiles(String req, String choice) throws ParseException, IOException {
		
		// cet argument indique combien de meilleurs résultats de recherche il doit retourner.
        int hitsPerPage = 3204;  
        
        // ouvrir l'index lucene en mode lecture et gère la recherche
        IndexReader reader = DirectoryReader.open(IndexFiles.dir);  
        
        // permet de l'execution d'une recherche dans l'index et la recherche des documents pertinents
        IndexSearcher searcher = new IndexSearcher(reader); 

        TopDocs docs = null;
        ScoreDoc[] hits = null;
        int h;
        
        // initialiser notre boolean query
        BooleanQuery booleanQuery = null;
        
        // Appeler la méthode parseQuery 
        booleanQuery = parseQuery(req, choice);
        
        /*#####################################################################################################*/
        /*                                                                                                     */
        /*                                      COMMENCER LA RECHERCHE                                         */
        /*                                                                                                     */
        /*#####################################################################################################*/
        
        //Permet de selectionner des documents pertinents (avec le score associé) pour une requête
        docs = searcher.search(booleanQuery, hitsPerPage); 

        hits = docs.scoreDocs;

        h = hits.length;
        
        // vider la liste des résultats pour une nouvelle recherche
        GraphicalUserInterface.dlm.clear();
        
        // afficher le nombre des résultats trouvés dans l'interface graphique
        if (hits.length == 0) {
             GraphicalUserInterface.dlm.addElement("Aucun article n'a été trouvé \n");
             }else if(hits.length == 1) { 
                	GraphicalUserInterface.dlm.addElement(+h+" Article a été trouvé \n");
                    }else {
        	            GraphicalUserInterface.dlm.addElement(+h+" Articles ont été trouvés \n");
                        }
        
        for(int j = 0; j < hits.length; ++j) {
            int docId = hits[j].doc;
            Document document = null;
			document = searcher.doc(docId);
			String id = document.get("id") ;
			String title = document.get("title") ;

            //afficher la liste des résultats (les articles trouvés) dans l'interface graphique 
			GraphicalUserInterface.dlm.addElement(j+1+" /  ID : "+ id+" -- Titre : " +title);
        }
		reader.close();
	}
	
	
	/*#####################################################################################################*/
	/*  parseQuery :                                                                                       */
	/*  Cette méthode analyse une requête d'entrée et la divise en sous-requêtes.                          */
	/*  Ensuite, elle crée une requête booléenne                                                           */
	/*                                                                                                     */
	/*  @inputs:                                                                                           */
	/*  		String inputQuery: la requête à analysé, e.g., "a+b"                                       */
	/*  		String searchCriteria: le critère lequel sera effectué la recherche, e.g., "author"        */
	/*  @output:                                                                                           */
    /*		BooleanQuery booleanQuery: un BooleanQuery qui sera utilisé pour lancer la recherche           */
	/*                                                                                                     */
	/*#####################################################################################################*/
	
	private BooleanQuery parseQuery(String inputQuery, String searchCriteria){
		BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
		
		//Initialisation 
		BooleanQuery booleanQuery = null;
		char currentOperator;
        String currentQuery = "";
        int i;
        
		//vérification et affectation (le premier élément de la requete doit être précédé par un opérateur "+" ou "-")
		if (inputQuery.startsWith("-") == false) {
			inputQuery = "+" + inputQuery;
		}
		
		i = 0;
		
		//initialiser le premier opérateur
		//la méthode "charAt()" récupère le caractère de l'indice associé
		currentOperator = inputQuery.charAt(i); 
		++i;
		
		//la boucle avance à chaque itération par 1 caractère 
		while (i < inputQuery.length()) {
			if (inputQuery.charAt(i) == '+' || inputQuery.charAt(i) == '|' || inputQuery.charAt(i) == '-') { 
			    // une nouvelle clause est rencontrée
				
				// création de la requete query et l'ajouté à BooleanQuery
				// créationn de l'objet query
				Query query;
				
				try {
					query = new QueryParser(searchCriteria, new StandardAnalyzer()).parse(currentQuery);
					if (currentOperator == '+') {
						//signifie que la clause est obligatoire.
						booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
						
					} else if (currentOperator == '-') {
						//signifie que la clause intèrdite
						booleanQueryBuilder.add(query, BooleanClause.Occur.MUST_NOT);
						
					} else if (currentOperator == '|') {
						//signifie que la clause est facultative
						booleanQueryBuilder.add(query, BooleanClause.Occur.SHOULD);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// vider la la requête courante (currentQuery)
				currentQuery = "";
				
				//mettre à jour l'opérateur courant (currentOperator) pour la prochaine requête
				currentOperator = inputQuery.charAt(i);
				
			} else {
				
				//initialiser currentQuery par le caractère suivant
				currentQuery = currentQuery + inputQuery.charAt(i); 
			}
			++i;
		}
		
		// récupérer la dernière requête (le dernier caractère de l'élément)
		// création de la requête "query" et l'ajouté à BooleanQuery
		// créationn de l'objet query
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
