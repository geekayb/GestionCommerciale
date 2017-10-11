/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Produit;
import bean.Utilisateur;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ayoub
 */
@Stateless
public class ProduitFacade extends AbstractFacade<Produit> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProduitFacade() {
        super(Produit.class);
    }

    public int getNombreProduit() {
        return (int) em.createQuery("select count(*) from Produit").getSingleResult();
    }

    public List<Produit> rechercheByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT p FROM Produit p where p.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        requete
                += "  and (p.libelle like '%" + recherche + "%' "
                + "or p.description like '%" + recherche + "%' "
                + "or p.fournisseur.nom like '%" + recherche + "%')";

        // System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }

    public Double getQteByProduit(Produit produit) {

        if (produit != null) {
            String requete = "select p.quantiteStock from Produit p where p.reference = " + produit.getReference();
            //System.out.println(requete);
            //System.out.println("ha res = " + em.createQuery(requete).getSingleResult().toString());
            return (Double) em.createQuery(requete).getSingleResult();
        } else {
            return 0.0;
        }

    }

    public double getPrixByProduit(Produit produit,String devise) {
        String requete="";
        if (produit != null && !devise.equals("")) {
            if(devise.equals("Dhs"))requete= "select p.prixDirham from Produit p where p.reference = " + produit.getReference();
            else if(devise.equals("$"))requete= "select p.prixDollar from Produit p where p.reference = " + produit.getReference();
            else if(devise.equals("€"))requete= "select p.prixEuro from Produit p where p.reference = " + produit.getReference(); //euro
             
            return (double) em.createQuery(requete).getSingleResult();
        } else {
            return 0;
        }

    }

    public List<Produit> listProduitSociete(Utilisateur utilisateur, String recherche) {
        String requete = "select p from Produit p where p.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete += " and p.libelle like '%" + recherche + "%' "
                    + "or p.description like '%" + recherche + "%' "
                    + "or p.reference like '%" + recherche + "%' "
                    + "or p.fournisseur.nom like '%" + recherche + "%'";
        }
        System.out.println("haaaa requete dyal les produits " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        if(em.createQuery(requete).getResultList()!=null) return em.createQuery(requete).getResultList();
        else return new ArrayList<>();

    }

    public List<Produit> produitExist(Integer reference) {
        List<Produit> p = null;
        String requete = "select p from Produit p where p.reference = " + reference;
        System.out.println("ha requeta " + requete);
          //if(em.createQuery(requete).getSingleResult()!=null)System.out.println("raaah kaaayn l prod");
        //  else if(em.createQuery(requete).getSingleResult()==null)System.out.println("raaah makinch l prod");
      //  p = (Produit) em.createQuery(requete).getSingleResult();
        System.out.println("haa res dyal resuta "+ em.createQuery(requete).getResultList());
        p = em.createQuery(requete).getResultList();
        return p;

    }

   

    
}
