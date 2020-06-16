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
	 /* Cette m�thode utilise un parseur  utilisant DOM prend en  entr�e  un document XML                   */
	 /* et construit, � partir de cela, un arbre form� d�objets : chaque objet appartient                   */
	 /* � une sous-classe de  Node et des  op�rations sur ces objets  permettent de cr�er                   */
	 /* de nouveaux noeuds, ou de naviguer dans le document.                                                */
	 /* @inputs:                                                                                            */
	 /*          String filename : le fichier XML qui doit etre parser                                      */
	 /* @output:                                                                                            */
	 /*          ArrayList<Document> indexDocuments : Le document ind�x�                                    */
	 /*#####################################################################################################*/
	
			   
	public ArrayList<Document> parser(String filename) throws IOException {
	   	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	   	ArrayList<Document> indexDocuments = new ArrayList<Document>();

	   	try {
	   		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	   		org.w3c.dom.Document doc = dBuilder.parse(filename);
	   		doc.getDocumentElement().normalize();

	   		//r�cup�rer la liste des documents � indexer
	   		NodeList docList = doc.getElementsByTagName("document");
	   		
	     	// boucler sur tous les documents
	   		for (int i = 0; i < docList.getLength(); i++) { 
	   			
	   			// cr�er un document � indexer (un document au sens de l'indexation)
	   			Document indexDocument = new Document();  

				// r�cup�rer le n�ud de "document"
	   			Node docNode = docList.item(i);
	   			
				// v�rifier si c'est vraiment un n�ud
	   			if (docNode.getNodeType() == Node.ELEMENT_NODE) { 
	   				
	   			    // Nous avons maintenant un n�ud de document
					Element docElement = (Element)docNode;
					
					// r�cup�rer l' "id" du document
					String idStr = docElement.getAttribute("id");
					
					// l'ajout du l'�l�ment "id" au document � indexer
					Field id = new TextField("id", idStr, Field.Store.YES);
					indexDocument.add(id);
					
					// r�cup�rer la liste de tous les n�uds d'un document
					NodeList childrenList = docNode.getChildNodes(); 
					
					 // boucler sur tous les n�uds d'un document
					for (int j = 0; j < childrenList.getLength(); j++) {
						Node node = childrenList.item(j);
						
						//v�rifier si c'est vraiment un n�ud
						if (node.getNodeType() == Node.ELEMENT_NODE) { 
							Element nodeElement = (Element) node;
							
							switch (nodeElement.getTagName()) {
							case "authors":
								// r�cup�rer la liste de tous les n�uds de "authors"
								NodeList authorsList = nodeElement.getChildNodes();
								
								// boucler sur tous les n�uds de "authors"
								for (int k = 0; k < authorsList.getLength(); k++) { 
									
									// r�cup�rer un "author"
									Node authorNode = authorsList.item(k);
									
									// v�rifier si c'est vraiment un n�ud
									if (authorNode.getNodeType() == Node.ELEMENT_NODE) { 
										Element authorElement = (Element) authorNode;
										
										// l'ajout du �l�ment "author" au document � indexer
										String authorName = authorElement.getTextContent();
										Field author = new TextField("author", authorName, Field.Store.YES);
										indexDocument.add(author);
									}
								  }
								break;
								
							case "date":
								// l'ajout du l'�l�ment "date" au document � indexer
						   		String dateVal = nodeElement.getTextContent();
								Field date = new TextField("date", dateVal, Field.Store.YES);
								indexDocument.add(date);
								break;
								
							case "content":
								// l'ajout du l'�l�ment "content" au document � indexer
						   		String contentVal = nodeElement.getTextContent();
								Field content = new TextField("content", contentVal, Field.Store.YES);
								indexDocument.add(content);
								break;
								
							case "keywords":
								// r�cup�rer la liste de tous les n�uds de "keywords"
								NodeList keywordsList = nodeElement.getChildNodes(); 
								
								// boucler sur tous les n�uds de "keywords"
								for (int k = 0; k < keywordsList.getLength(); k++) { // loop over 'keywords' node
									
									// r�cup�rer un "keyword"
									Node keywordNode = keywordsList.item(k);
									
									// v�rifier si c'est vraiment un n�ud
									if (keywordNode.getNodeType() == Node.ELEMENT_NODE) { 
										Element keywordElement = (Element) keywordNode;
										
										// l'ajout du l'�l�ment "keyword" au document � indexer
								   		String keywordVal = nodeElement.getTextContent();
										Field keyword = new TextField("keyword", keywordVal, Field.Store.YES);
										indexDocument.add(keyword);
									}
								  }
								break;
								
							case "entrydate":
								// l'ajout du l'�l�ment "entrydate" au document � indexer
						   		String entrydateVal = nodeElement.getTextContent();
								Field entrydate = new TextField("entrydate", entrydateVal, Field.Store.YES);
								indexDocument.add(entrydate);
								break;
								
							case "title":
								// l'ajout du l'�l�ment "title" au document � indexer
						   		String titleVal = nodeElement.getTextContent();
								Field title = new TextField("title", titleVal, Field.Store.YES);
								indexDocument.add(title);
								break;
								
							case "abstract":
								// l'ajout du l'�l�ment "abstract" au document � indexer
						   		String abstractVal = nodeElement.getTextContent();
								Field abstractField = new TextField("abstract", abstractVal, Field.Store.YES);
								indexDocument.add(abstractField);
								break;
								
							case "references":
								// r�cup�rer la liste de tous les n�uds de "references"
								NodeList referencesList = nodeElement.getChildNodes(); 
								
								// boucler sur tous les n�uds de "references"
								for (int k = 0; k < referencesList.getLength(); k++) { 
									
									// r�cup�rer une "reference"
									Node referenceNode = referencesList.item(k);
									
									// v�rifier si c'est vraiment un n�ud
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
	   			
	   			//ind�xer le document
	   			indexDocuments.add(indexDocument);
			 }
	   	   } catch(Exception e) {}
	   	
         // retourner le document ind�x�
	   	 return indexDocuments;
	 }
 }