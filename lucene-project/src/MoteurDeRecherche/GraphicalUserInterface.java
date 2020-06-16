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
  
	// Cr�ation d'une liste de crit�res en Fran�ais
    Object[] elements = new Object[]{"Auteur", "Date de publication", "Contenu", "Mot-cl�", "Date de saisie", "Titre", "R�sum� ", "R�f�rence"};
	
	@SuppressWarnings("unchecked")
	public  GraphicalUserInterface() {
		
		/*************************************** CREATION DE LA FENETRE *********************************************/
		//cr�ation de la fen�tre 
		fenetre = new JFrame("Fen�tre de recherche");
		
		
		/*************************************** CREATION DU PANEL GLOBAL *********************************************/
		// Cr�er le panel global
	    containerPane = new JPanel();
	    
	    // D�viser le panel en 2 Layout 
	    containerPane.setLayout(new GridLayout(1, 2));
	    
	    
	    /*************************************** CREATION DU PANEL MOTEUR *********************************************/
	    // Cr�er le panel � panMoteur � � gauche de la fen�tre qui doit contenir un champ pour de saisie de la recherche et un bouton
	    panMoteur = new JPanel();
	    panMoteur.setLayout(null);
	    // Cr�er des bordures bleue autour du � panMoteur � 
	    panMoteur.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Barre de recherche"));
	    // L'ajout du panMoteur au containerPane (le panel global)
        containerPane.add(panMoteur);
        
	    
        /*************************************** CREATION DU PANEL RESULTAT ********************************************/
	    // Cr�er le panel � panResultat � � droite de la fen�tre qui doit contenir un champ pour afficher les r�sultats
	    panResultat = new JPanel();
	    panResultat.setLayout(new GridLayout());
	    // Cr�er des bordures bleue autour du � panMoteur �
	    panResultat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "R�sultat(s)"));
        // L'ajout du panResultat au containerPane (le panel global)
        containerPane.add(panResultat);
        
        
        /*************************************** CREATION DE LA LISTE DES RESULTATS ***************************************/
	    // cr�er une liste vertical pour l'affichge des r�sultats 
	    dlm = new DefaultListModel();
	    listeresultat = new JList(dlm);
	    listeresultat.setLayoutOrientation(JList.VERTICAL);
	    
	    // Cr�er un Scroll pour pouvoir d�filer dans la liste 
	    scroll = new JScrollPane(listeresultat);
	    scroll.setViewportView(listeresultat);
	    //L'ajout du Scroll au PanResultat
	  	panResultat.add(scroll);
	    
	    //D�finir un police et un style 
	    Font font1 = new Font ("Garamond", Font.BOLD, 20); 
	    
	    
	    /*************************************** CREATION DE LABEL RECHERCHE *********************************************/
	    // Cr�er un laber " Recherche "
		lab1 = new JLabel ("Recherche");
		//Lui Associer des dimensions
		lab1.setBounds(10, 90, 300, 15);
		//Lui Associer le Font
		lab1.setFont(font1); 
		//Lui Associer une couleur
		lab1.setForeground(Color.black);
		// L'ajout du label "Recherche" au � panMoteur �
	    panMoteur.add(lab1);
		
	    
	    /*************************************** CREATION DE CHAMP DE SAISIE *********************************************/
	    // Cr�er un chmap de saisie "JTextField"
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
	    
	    
	    // L'ajout de "requete" le champ de saisie au � panMoteur �
		panMoteur.add(requete);
		
		
		/*************************************** CREATION DE LABEL CRITERE *********************************************/
		// Cr�er un label " Veuillez choisir un crit�re :  "
	    lab2= new JLabel (" Veuillez choisir un crit�re :  ");
	    //Lui Associer des dimmensions
		lab2.setBounds(165,150, 500, 20);
		//Lui Associer le Font
		lab2.setFont(font1);  
		//Lui Associer la couleur Font
		lab2.setForeground(Color.black);
		//Lui ajouter au � panMoteur �
		panMoteur.add(lab2);
		
		
		/*************************************** CREATION DE LA LISTE DEROULANTE ******************************************/
		// Cr�er une liste des choix des crit�re "JComboBox" � partir de "Object elements"
		listechoix = new JComboBox(elements);
		//Lui Associer des dimmensions
		listechoix.setBounds(187,200, 200, 35);
		//Lui Associer le Font
		listechoix.setFont(font1);
		//Lui Associer la couleur Font
	    listechoix.setForeground(Color.black);
	    //Lui ajouter au � panMoteur �
		panMoteur.add(listechoix);
		
		
		/*************************************** CREATION DE BOUTON RECHERCHER *********************************************/
		//Cr�er un boutton "JButton"(Rechercher)
		BoutonRecherche = new JButton("Rechercher");
		BoutonRecherche.setActionCommand("rechercher");
		//Lui Associer des dimmensions
		BoutonRecherche.setBounds(215, 280, 150, 40);
		//Lui Associer le Font
		BoutonRecherche.setFont(font1);
		BoutonRecherche.addActionListener(this);
		//Lui ajouter au � panMoteur �
		panMoteur.add(BoutonRecherche);
		
		
		 

		// R�cup�rer l'icone 
		ImageIcon iconSearchFrame = new ImageIcon("F:\\Informa\\L3-ISIL\\RI\\outils\\icons-chercher-frame.PNG");
		// L'ajout de l'icone � gauche de la barre de la fen�tre 
        fenetre.setIconImage(iconSearchFrame.getImage());
        
		// R�cup�rer l'icone
		ImageIcon iconSearch = new ImageIcon("F:\\Informa\\L3-ISIL\\RI\\outils\\icons-chercher-searchBar.PNG");
		//Ajout de l'icone � droite de la barre de recherche
		JLabel label = new JLabel(iconSearch);
		label.setBounds(355, 25, 300, 150);
		// L'ajout du label1 au panMoteur
        panMoteur.add(label);
       
        // L'ajout du panel global "containerPane" � la fen�tre  
        fenetre.add(containerPane);
        //D�finir la dimension de la fen�tre 
        fenetre.setSize(1200, 400);
        fenetre.setVisible(true);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // D�finir au Milieu de l'ecran
        fenetre.setLocationRelativeTo(null); 
		fenetre.setResizable(false);
		
	}
	
	
	 /*#####################################################################################################*/
	 /*                                                                                                     */
	 /* actionPerformed :                                                                                   */
	 /* Cette m�thode d�finit une action sur le bouton "Rechercher", pour affectuer une recherche.          */
     /*                                                                                                     */
	 /*  @inputs:                                                                                           */
	 /*          ActionEvent e : "e" est un �v�nement d'un click de bouton.                                 */
	 /* 		                                                                                            */
	 /*                                                                                                     */
	 /*#####################################################################################################*/
	
	public void actionPerformed(ActionEvent e) {   
		
		String mot="";
	    String champ="";
	   
	    // Cr�er une Liste des crit�res en "Anglais"
	 	Object[] EnglishList = new Object[]{"author", "date", "content", "keyword", "entrydate", "title", "abstract", "reference"};
	 	
	 	//R�cup�rer la requete saisit par l'utilisateur 
	    mot = requete.getText();
	    
	     int i = 0;
	     
	    // R�cup�rer l'�l�ment s�l�ctionn� dans la liste des crit�res
		champ= listechoix.getSelectedItem().toString();
		
	    boolean stop = false;
	    
	    while((i < elements.length) && (stop == false) ) {
	    	// V�rifier si le champ s�l�ctionn� est �gale � l'�l�ment de la liste "elements"
	    	if(champ.equals(elements[i].toString())) {
	    	
	    	// S'ils sont �gaux on arr�te la boucle	
	    	stop = true;
	    	
	    	 }else {
	    		 
	    		i++;
	      }
	    }
	    
	    // Affecter l'�l�ment en anglais de "EnglishList" au champ  
	    champ = EnglishList[i].toString();	
	    
		if("rechercher".equals(e.getActionCommand()))
		  { 
			if(!mot.equals(""))
			{
				// Cr�er l'objet search de la Classe "SerchFiles"
				SearchFiles search = new SearchFiles();
		    	
		    	try {
		    		
		    		//Appeler la m�thode "SearchFiles"
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
	
	System.out.println(".............D�but de l'indexation.............");
	
	// Initialisation
	StandardAnalyzer  analyzer = null ;
	
	// Appeler la m�thode Indexer()
	IndexFiles.Indexer(analyzer);
	
	System.out.println("..............fin de l'indexation...............");
	
	//Cr�er l'objet "projet" du constructeur "GraphicalUserInterface()"
	GraphicalUserInterface projet = new GraphicalUserInterface();
	
    }
  }

