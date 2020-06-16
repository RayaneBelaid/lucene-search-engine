package MoteurDeRecherche;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import java.awt.*;
import javax.swing.*;


      /*#####################################################################################################*/
      /*                                                                                                     */
      /*                                      INTERFACE GRAPHIQUE                                            */
      /*                                                                                                     */
      /*#####################################################################################################*/

public class GraphicalUserInterface extends JFrame implements ActionListener {
	
    JFrame fenetre;
    JPanel containerPane;
    JPanel panMoteur;
    JPanel panResultat;
	
    JLabel lab1;
    JTextField requete;
    JLabel lab2;
    JComboBox listechoix;
    JButton BoutonRecherche;
    JComboBox EnglishListChoice;
    static DefaultListModel dlm ;
    JList listeresultat ;
    JScrollPane scroll ;
  
        // Création d'une liste de critères en Français
        Object[] elements = new Object[]{"Auteur", "Date de publication", "Contenu", "Mot-clé", "Date de saisie", "Titre", "Résumé ", "Référence"};
	
	@SuppressWarnings("unchecked")
	public  GraphicalUserInterface() {
		
	      /*************************************** CREATION DE LA FENETRE **********************************************/
	      //création de la fenêtre 
	      fenetre = new JFrame("Fenêtre de recherche");
		
		
	      /*************************************** CREATION DU PANEL GLOBAL *********************************************/
	      // Créer le panel global
	      containerPane = new JPanel();
	    
	      // Déviser le panel en 2 Layout 
	      containerPane.setLayout(new GridLayout(1, 2));
	    
	    
	      /*************************************** CREATION DU PANEL MOTEUR **********************************************/
	      // Créer le panel « panMoteur » à gauche de la fenêtre qui doit contenir un champ pour de saisie de la recherche et un bouton
	      panMoteur = new JPanel();
	      panMoteur.setLayout(null);
	      // Créer des bordures bleue autour du « panMoteur » 
	      panMoteur.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Barre de recherche"));
	      // L'ajout du panMoteur au containerPane (le panel global)
              containerPane.add(panMoteur);
        
	    
              /*************************************** CREATION DU PANEL RESULTAT *********************************************/
	      // Créer le panel « panResultat » à droite de la fenêtre qui doit contenir un champ pour afficher les résultats
	      panResultat = new JPanel();
	      panResultat.setLayout(new GridLayout());
	      // Créer des bordures bleue autour du « panMoteur »
	      panResultat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Résultat(s)"));
              // L'ajout du panResultat au containerPane (le panel global)
              containerPane.add(panResultat);
        
        
              /*************************************** CREATION DE LA LISTE DES RESULTATS *************************************/
	      // créer une liste vertical pour l'affichge des résultats 
	      dlm = new DefaultListModel();
	      listeresultat = new JList(dlm);
	      listeresultat.setLayoutOrientation(JList.VERTICAL);
	    
	      // Créer un Scroll pour pouvoir défiler dans la liste 
	      scroll = new JScrollPane(listeresultat);
	      scroll.setViewportView(listeresultat);
	      //L'ajout du Scroll au PanResultat
	      panResultat.add(scroll);
	    
	      //Définir un police et un style 
	      Font font1 = new Font ("Garamond", Font.BOLD, 20); 
	    
	    
	      /*************************************** CREATION DE LABEL RECHERCHE ********************************************/
	      // Créer un laber " Recherche "
	      lab1 = new JLabel ("Recherche");
	      //Lui Associer des dimensions
	      lab1.setBounds(10, 90, 300, 15);
	      //Lui Associer le Font
	      lab1.setFont(font1); 
	      //Lui Associer une couleur
	      lab1.setForeground(Color.black);
	      // L'ajout du label "Recherche" au « panMoteur »
	      panMoteur.add(lab1);
		
	    
	      /*************************************** CREATION DE CHAMP DE SAISIE ********************************************/
	      // Créer un chmap de saisie "JTextField"
	      requete= new JTextField("Saisir une requete");
	      //Lui Associer des dimensions
	      requete.setBounds(120, 80, 370, 35);
	      //requete.setFont(font1);
	    
	      //Ajout du place holder au champ de saisie "JTextField"
	      requete.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	            if (requete.getText().equals("Saisir une requete")){
	            	requete.setText("");
	            	requete.setForeground(Color.black);
	            	requete.setFont(font1);
	          }
	        }
	        
	        @Override
	        public void focusLost(FocusEvent e) {
	            if (requete.getText().isEmpty()) {
	            	
	            	requete.setForeground(Color.gray);
	            	requete.setFont(font1);
	            	requete.setText("Saisir une requete");
	            }
	          }
	       });
	    
	    
	       // L'ajout de "requete" le champ de saisie au « panMoteur »
	       panMoteur.add(requete);
		
		
		/*************************************** CREATION DE LABEL CRITERE ********************************************/
		// Créer un label " Veuillez choisir un critère :  "
	        lab2= new JLabel (" Veuillez choisir un critère :  ");
	        //Lui Associer des dimmensions
		lab2.setBounds(165,150, 500, 20);
		//Lui Associer le Font
		lab2.setFont(font1);  
		//Lui Associer la couleur Font
		lab2.setForeground(Color.black);
		//Lui ajouter au « panMoteur »
		panMoteur.add(lab2);
		
		
		/*************************************** CREATION DE LA LISTE DEROULANTE **************************************/
		// Créer une liste des choix des critère "JComboBox" à partir de "Object elements"
		listechoix = new JComboBox(elements);
		//Lui Associer des dimmensions
		listechoix.setBounds(187,200, 200, 35);
		//Lui Associer le Font
		listechoix.setFont(font1);
		//Lui Associer la couleur Font
	        listechoix.setForeground(Color.black);
	        //Lui ajouter au « panMoteur »
		panMoteur.add(listechoix);
		
		
		/*************************************** CREATION DE BOUTON RECHERCHER ****************************************/
		//Créer un boutton "JButton"(Rechercher)
		BoutonRecherche = new JButton("Rechercher");
		BoutonRecherche.setActionCommand("rechercher");
		//Lui Associer des dimmensions
		BoutonRecherche.setBounds(215, 280, 150, 40);
		//Lui Associer le Font
		BoutonRecherche.setFont(font1);
		BoutonRecherche.addActionListener(this);
		//Lui ajouter au « panMoteur »
		panMoteur.add(BoutonRecherche);
		
		
		 

		// Récupérer l'icone 
		ImageIcon iconSearchFrame = new ImageIcon("F:\\Informa\\L3-ISIL\\RI\\outils\\icons-chercher-frame.PNG");
		// L'ajout de l'icone à gauche de la barre de la fenêtre 
                fenetre.setIconImage(iconSearchFrame.getImage());
        
		// Récupérer l'icone
		ImageIcon iconSearch = new ImageIcon("F:\\Informa\\L3-ISIL\\RI\\outils\\icons-chercher-searchBar.PNG");
		//Ajout de l'icone à droite de la barre de recherche
		JLabel label = new JLabel(iconSearch);
		label.setBounds(355, 25, 300, 150);
		// L'ajout du label1 au panMoteur
                panMoteur.add(label);
       
        // L'ajout du panel global "containerPane" à la fenêtre  
        fenetre.add(containerPane);
        //Définir la dimension de la fenêtre 
        fenetre.setSize(1200, 400);
        fenetre.setVisible(true);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Définir au Milieu de l'ecran
        fenetre.setLocationRelativeTo(null); 
	fenetre.setResizable(false);
		
	}
	
	
	 /*#####################################################################################################*/
	 /*                                                                                                     */
	 /* actionPerformed :                                                                                   */
	 /* Cette méthode définit une action sur le bouton "Rechercher", pour affectuer une recherche.          */
         /*                                                                                                     */
	 /*  @inputs:                                                                                           */
	 /*          ActionEvent e : "e" est un évènement d'un click de bouton.                                 */
	 /* 		                                                                                        */
	 /*                                                                                                     */
	 /*#####################################################################################################*/
	
	public void actionPerformed(ActionEvent e) {   
		
	    String mot="";
	    String champ="";
	   
	    // Créer une Liste des critères en "Anglais"
	    Object[] EnglishList = new Object[]{"author", "date", "content", "keyword", "entrydate", "title", "abstract", "reference"};
	 	
	    //Récupérer la requete saisit par l'utilisateur 
	    mot = requete.getText();
	    
	    int i = 0;
	     
	    // Récupérer l'élément séléctionné dans la liste des critères
	    champ= listechoix.getSelectedItem().toString();
		
	    boolean stop = false;
	    
	    while((i < elements.length) && (stop == false) ) {
	    	// Vérifier si le champ séléctionné est égale à l'élément de la liste "elements"
	    	if(champ.equals(elements[i].toString())) {
	    	
	    	// S'ils sont égaux on arrête la boucle	
	    	stop = true;
	    	
	    	 }else { 
	    		i++;
	      }
	    }
	    
	    // Affecter l'élément en anglais de "EnglishList" au champ  
	    champ = EnglishList[i].toString();	
	    
		if("rechercher".equals(e.getActionCommand()))
		  { 
			if(!mot.equals(""))
			{
				// Créer l'objet search de la Classe "SerchFiles"
				SearchFiles search = new SearchFiles();
		    	
		    	try {
		    		
		    		//Appeler la méthode "SearchFiles"
					search.searchFiles(mot,champ);
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			  }   
			} 	
	      } 
	    }
	
@SuppressWarnings({ "resource", "unchecked" })
public static void main (String[] args) throws IOException, ParseException{    
	
	System.out.println(".............Début de l'indexation.............");
	
	// Initialisation
	StandardAnalyzer  analyzer = null ;
	
	// Appeler la méthode Indexer()
	IndexFiles.Indexer(analyzer);
	
	System.out.println("..............fin de l'indexation...............");
	
	//Créer l'objet "projet" du constructeur "GraphicalUserInterface()"
	GraphicalUserInterface projet = new GraphicalUserInterface();
	
    }
  }

