package MoteurDeRecherche;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


    /*#####################################################################################################*/
    /*                                                                                                     */
    /*                                      PARSEUR DOM XML                                                */
    /*                                                                                                     */
    /*#####################################################################################################*/


public class XmlFileParser {
	
	// Constructeur   
	public  XmlFileParser() {}
	
	 /*#####################################################################################################*/
	 /* parser:                                                                                             */
	 /* Cette méthode utilise un parseur  utilisant DOM prend en  entrée  un document XML                   */
	 /* et construit, à partir de cela, un arbre formé d’objets : chaque objet appartient                   */
	 /* à une sous-classe de  Node et des  opérations sur ces objets  permettent de créer                   */
	 /* de nouveaux noeuds, ou de naviguer dans le document.                                                */
	 /* @inputs:                                                                                            */
	 /*          String filename : le fichier XML qui doit etre parser                                      */
	 /* @output:                                                                                            */
	 /*          ArrayList<Document> indexDocuments : Le document indéxé                                    */
	 /*#####################################################################################################*/
	
			   
	public ArrayList<Document> parser(String filename) throws IOException {
	   	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	   	ArrayList<Document> indexDocuments = new ArrayList<Document>();

	   	try {
	   		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	   		org.w3c.dom.Document doc = dBuilder.parse(filename);
	   		doc.getDocumentElement().normalize();

	   		//récupérer la liste des documents à indexer
	   		NodeList docList = doc.getElementsByTagName("document");
	   		
	     	// boucler sur tous les documents
	   		for (int i = 0; i < docList.getLength(); i++) { 
	   			
	   			// créer un document à indexer (un document au sens de l'indexation)
	   			Document indexDocument = new Document();  

				// récupérer le nœud de "document"
	   			Node docNode = docList.item(i);
	   			
				// vérifier si c'est vraiment un nœud
	   			if (docNode.getNodeType() == Node.ELEMENT_NODE) { 
	   				
	   			    // Nous avons maintenant un nœud de document
					Element docElement = (Element)docNode;
					
					// récupérer l' "id" du document
					String idStr = docElement.getAttribute("id");
					
					// l'ajout du l'élément "id" au document à indexer
					Field id = new TextField("id", idStr, Field.Store.YES);
					indexDocument.add(id);
					
					// récupérer la liste de tous les nœuds d'un document
					NodeList childrenList = docNode.getChildNodes(); 
					
					 // boucler sur tous les nœuds d'un document
					for (int j = 0; j < childrenList.getLength(); j++) {
						Node node = childrenList.item(j);
						
						//vérifier si c'est vraiment un nœud
						if (node.getNodeType() == Node.ELEMENT_NODE) { 
							Element nodeElement = (Element) node;
							
							switch (nodeElement.getTagName()) {
							case "authors":
								// récupérer la liste de tous les nœuds de "authors"
								NodeList authorsList = nodeElement.getChildNodes();
								
								// boucler sur tous les nœuds de "authors"
								for (int k = 0; k < authorsList.getLength(); k++) { 
									
									// récupérer un "author"
									Node authorNode = authorsList.item(k);
									
									// vérifier si c'est vraiment un nœud
									if (authorNode.getNodeType() == Node.ELEMENT_NODE) { 
										Element authorElement = (Element) authorNode;
										
										// l'ajout du élément "author" au document à indexer
										String authorName = authorElement.getTextContent();
										Field author = new TextField("author", authorName, Field.Store.YES);
										indexDocument.add(author);
									}
								  }
								break;
								
							case "date":
								// l'ajout du l'élément "date" au document à indexer
						   		String dateVal = nodeElement.getTextContent();
								Field date = new TextField("date", dateVal, Field.Store.YES);
								indexDocument.add(date);
								break;
								
							case "content":
								// l'ajout du l'élément "content" au document à indexer
						   		String contentVal = nodeElement.getTextContent();
								Field content = new TextField("content", contentVal, Field.Store.YES);
								indexDocument.add(content);
								break;
								
							case "keywords":
								// récupérer la liste de tous les nœuds de "keywords"
								NodeList keywordsList = nodeElement.getChildNodes(); 
								
								// boucler sur tous les nœuds de "keywords"
								for (int k = 0; k < keywordsList.getLength(); k++) { // loop over 'keywords' node
									
									// récupérer un "keyword"
									Node keywordNode = keywordsList.item(k);
									
									// vérifier si c'est vraiment un nœud
									if (keywordNode.getNodeType() == Node.ELEMENT_NODE) { 
										Element keywordElement = (Element) keywordNode;
										
										// l'ajout du l'élément "keyword" au document à indexer
								   		String keywordVal = nodeElement.getTextContent();
										Field keyword = new TextField("keyword", keywordVal, Field.Store.YES);
										indexDocument.add(keyword);
									}
								  }
								break;
								
							case "entrydate":
								// l'ajout du l'élément "entrydate" au document à indexer
						   		String entrydateVal = nodeElement.getTextContent();
								Field entrydate = new TextField("entrydate", entrydateVal, Field.Store.YES);
								indexDocument.add(entrydate);
								break;
								
							case "title":
								// l'ajout du l'élément "title" au document à indexer
						   		String titleVal = nodeElement.getTextContent();
								Field title = new TextField("title", titleVal, Field.Store.YES);
								indexDocument.add(title);
								break;
								
							case "abstract":
								// l'ajout du l'élément "abstract" au document à indexer
						   		String abstractVal = nodeElement.getTextContent();
								Field abstractField = new TextField("abstract", abstractVal, Field.Store.YES);
								indexDocument.add(abstractField);
								break;
								
							case "references":
								// récupérer la liste de tous les nœuds de "references"
								NodeList referencesList = nodeElement.getChildNodes(); 
								
								// boucler sur tous les nœuds de "references"
								for (int k = 0; k < referencesList.getLength(); k++) { 
									
									// récupérer une "reference"
									Node referenceNode = referencesList.item(k);
									
									// vérifier si c'est vraiment un nœud
									if (referenceNode.getNodeType() == Node.ELEMENT_NODE) { 
										Element referenceElement = (Element) referenceNode;
									}
								  }
								break;

							default:
								
								break;
							}
						 }
					  }
				   }
	   			
	   			//indéxer le document
	   			indexDocuments.add(indexDocument);
			 }
	   	   } catch(Exception e) {}
	   	
         // retourner le document indéxé
	   	 return indexDocuments;
	 }
 }