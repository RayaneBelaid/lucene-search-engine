package MoteurDeRecherche;

import java.io.File;
import java.io.FileFilter;

    /*#####################################################################################################*/
    /*                                                                                                     */
    /*                                      FILTRER LES FICHIERS                                           */
    /*                                                                                                     */
    /*#####################################################################################################*/

public class XmlFileFilter  implements FileFilter{
	// Constructeur
	public  XmlFileFilter() {}

	@Override
	public boolean accept(File pathname){
			return pathname.getName().toLowerCase().endsWith(".xml");
	}
   }

    /*#####################################################################################################*/
    /* accept:                                                                                             */
    /* cette méthode permet de filtrer les document en acceptant seulement ceux du type xml                */
    /* @inputs:                                                                                            */
    /*          File pathname : le fichier qui doit etre filtrer                                           */
    /* @output:                                                                                            */
    /*          un document de type XML                                                                    */
    /*#####################################################################################################*/
