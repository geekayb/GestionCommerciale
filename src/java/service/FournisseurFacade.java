/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Fournisseur;
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
public class FournisseurFacade extends AbstractFacade<Fournisseur> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FournisseurFacade() {
        super(Fournisseur.class);
    }

    public List<Fournisseur> rechercheByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "SELECT f FROM Fournisseur f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        requete += " and f.nom like '%" + recherche + "%' "
                + "or f.adresse like '%" + recherche + "%' "
                + "or f.adresse like '%" + recherche + "%' "
                + "or f.ville like '%" + recherche + "%' "
                + "or f.adresse like '%" + recherche + "%' "
                + "or f.fax like '%" + recherche + "%' or f.email like '%" + recherche + "%' "
                + "or f.siteweb like '%" + recherche + "%'";
        //System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Fournisseur> listFournisseurSociete(Utilisateur utilisateur, String recherche) {
        String requete = "select f from Fournisseur f where f.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete += "  and ( f.nom like '%" + recherche + "%' "
                    + "or f.adresse like '%" + recherche + "%' "
                    + "or f.adresse like '%" + recherche + "%' "
                    + "or f.ville like '%" + recherche + "%' "
                    + "or f.adresse like '%" + recherche + "%' "
                    + "or f.fax like '%" + recherche + "%' or f.email like '%" + recherche + "%' "
                    + "or f.siteweb like '%" + recherche + "%')";

        }

        //System.out.println("haaaa requete dyal les fourni " + requete);
        //System.out.println(em.createQuery(requete).getResultList());
        if(em.createQuery(requete).getResultList()!=null) return em.createQuery(requete).getResultList();
        else return new ArrayList<>();
       

    }

}
