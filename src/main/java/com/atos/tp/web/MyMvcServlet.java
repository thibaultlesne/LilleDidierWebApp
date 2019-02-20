package com.atos.tp.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.tp.core.Categorie;
import com.atos.tp.core.Produit;
import com.atos.tp.core.ProduitService;
import com.atos.tp.core.ProduitServiceJdbc;
import com.atos.tp.core.ProduitServiceSimu;

/**
 * Servlet implementation class MyMvcServlet
 */
@WebServlet("/MyMvcServlet")
public class MyMvcServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyMvcServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//controleur principal ("front-controller") d�leguant vers sous m�thodes do...Xy()
		//selon valeur du champ cach� "task" de la page html/jsp appelante:
		String task = request.getParameter("task");
		switch(task) {
			case "tacheXy":	doTacheXy(request,response); break;
			case "ajouterCategorie":	doAjouterCategorie(request,response); break;
			case "rechercherProduits": doRechercherProduits(request,response);  break;
		}
	}
	
	protected void doAjouterCategorie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //code d'une tache ici ou bien dans classe annexe (appel�e sous-controleur) .
		ProduitService produitService = new ProduitServiceJdbc();
		String labelCategorie = request.getParameter("categorie");
		Categorie cat = new Categorie(null,labelCategorie);
		produitService.insertIntoCategory(cat);
		List<String> listeCat = produitService.getAllCategories();
		request.setAttribute("listeCat", listeCat);
		RequestDispatcher rd = 
				this.getServletContext().getRequestDispatcher("/allCategories.jsp");
		rd.forward(request,response);
	}
	
	protected void doTacheXy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //code d'une tache ici ou bien dans classe annexe (appel�e sous-controleur) .
	}
		
	protected void doRechercherProduits(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//r�cup�rer valeur saisie (ou s�lectionn�e):
		String categorie = request.getParameter("categorie");
		//on cr�er une instance de la classe ProduitServiceJdbc
		//pour pouvoir appeler dessus la m�thode produitsSelonCategorie()
		ProduitService produitService = null;
		List<Produit> listeProduits = null;
		produitService = new ProduitServiceJdbc();
		//on appelle la m�thode sur le service et on stocke le resultat dans une
		//variable/r�f�rence locale listeProduits.
		listeProduits = produitService.produitsSelonCategorie(categorie);
		String pageJsp=null;
		if(listeProduits==null || listeProduits.isEmpty() ) {
			//SECOND ESSAI en version Simu ICI EN TP (si base de donn�e inacessible) 
			produitService = new ProduitServiceSimu();
            listeProduits = produitService.produitsSelonCategorie(categorie);
		}
		if(listeProduits!=null) {
			pageJsp="/listeProduits.jsp";
			//le servlet stocke dans partie "attribute" de l'objet request
			//une association entre le nom logique "listeProd" et les donn�es pr�par�es
			request.setAttribute("listeProd", listeProduits);
		}else {
			pageJsp="/choixCategorie.jsp";
			request.setAttribute("message", "categorie invalide");
		}
		//on construit l'objet de redirection vers la page jsp
		RequestDispatcher rd = 
				this.getServletContext().getRequestDispatcher(pageJsp);
		//le .forward() demande � la page jsp de finir le travail:
		// afficher les valeurs en html
		rd.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
