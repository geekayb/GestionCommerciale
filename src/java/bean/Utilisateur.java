/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Ayoub
 */
@Entity
public class Utilisateur implements Serializable {

    @OneToMany(mappedBy = "utilisateur")
    private List<Produit> produits;

    @OneToMany(mappedBy = "utilisateur")
    private List<Livraison> livraisons;

    @OneToMany(mappedBy = "utilisateur")
    private List<Fournisseur> fournisseurs;

    @OneToMany(mappedBy = "utilisateur")
    private List<Facture> factures;

    @OneToMany(mappedBy = "utilisateur")
    private List<Devis> deviss;

    @OneToMany(mappedBy = "utilisateur")
    private List<Commande> commandes;

    @OneToMany(mappedBy = "utilisateur")
    private List<Client> clients;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String telephone;
    private String fonction;
    private String profil;
    private Boolean gererClients = false;
    private Boolean gererCatalogue = false;
    private Boolean gererFournisseur = false;
    private Boolean gererDonnees = false;
    private Boolean gererUsers = false;
    private Boolean impression = false;
    private Boolean gererParametre = false;
    private Boolean payerFacture = false;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDernierCnx;

    @ManyToOne
    public Societe societe;

    public Boolean getGererParametre() {
        return gererParametre;
    }

    public void setGererParametre(Boolean gererParametre) {
        this.gererParametre = gererParametre;
    }

    public Boolean getPayerFacture() {
        return payerFacture;
    }

    public void setPayerFacture(Boolean payerFacture) {
        this.payerFacture = payerFacture;
    }

    
    
    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public List<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(List<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public List<Fournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public Boolean getGererClients() {
        return gererClients;
    }

    public void setGererClients(Boolean gererClients) {
        this.gererClients = gererClients;
    }

    public Boolean getGererCatalogue() {
        return gererCatalogue;
    }

    public void setGererCatalogue(Boolean gererCatalogue) {
        this.gererCatalogue = gererCatalogue;
    }

    public Boolean getGererFournisseur() {
        return gererFournisseur;
    }

    public void setGererFournisseur(Boolean gererFournisseur) {
        this.gererFournisseur = gererFournisseur;
    }

    public Boolean getGererDonnees() {
        return gererDonnees;
    }

    public void setGererDonnees(Boolean gererDonnees) {
        this.gererDonnees = gererDonnees;
    }

    public Boolean getGererUsers() {
        return gererUsers;
    }

    public void setGererUsers(Boolean gererUsers) {
        this.gererUsers = gererUsers;
    }

    public Boolean getImpression() {
        return impression;
    }

    public void setImpression(Boolean impression) {
        this.impression = impression;
    }

    public List<Facture> getFactures() {
        return factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }

    public List<Devis> getDeviss() {
        return deviss;
    }

    public void setDeviss(List<Devis> deviss) {
        this.deviss = deviss;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getProfil() {
      
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateDernierCnx() {
        return dateDernierCnx;
    }

    public void setDateDernierCnx(Date dateDernierCnx) {
        this.dateDernierCnx = dateDernierCnx;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Utilisateur[ id=" + id + " ]";
    }

}
