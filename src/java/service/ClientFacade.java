/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Client;
import bean.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ayoub
 */
@Stateless
public class ClientFacade extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "Gestion_commercialePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }

    public List<Client> rechercheByCritere(String recherche, Utilisateur utilisateur) {

        String requete = "select c from Client c where c.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete += " and (c.raisonSociale like '%" + recherche + "%' "
                    + "or c.adresse like '%" + recherche + "%' "
                    + "or c.codePostale like '%" + recherche + "%' "
                    + "or c.Ville like '%" + recherche + "%' "
                    + "or c.telephone like '%" + recherche + "%' "
                    + "or c.Fax like '%" + recherche + "%' "
                    + "or c.email like '%" + recherche + "%')"
                    + "and u.societe.id = " + utilisateur.getSociete().getId();

        }
        System.out.println(requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Client> listClientSociete(Utilisateur utilisateur, String recherche) {
        String requete = "select c from Client c where c.utilisateur.societe.id = " + utilisateur.getSociete().getId();
        if (!recherche.equals("")) {
            requete += " and (c.raisonSociale like '%" + recherche + "%' "
                    + "or c.adresse like '%" + recherche + "%' "
                    + "or c.codePostale like '%" + recherche + "%' "
                    + "or c.Ville like '%" + recherche + "%' "
                    + "or c.telephone like '%" + recherche + "%' "
                    + "or c.Fax like '%" + recherche + "%' "
                    + "or c.email like '%" + recherche + "%')";
        }
        System.out.println("haaaa requete dyal lclient " + requete);
        System.out.println(em.createQuery(requete).getResultList());
        return em.createQuery(requete).getResultList();

    }

}
