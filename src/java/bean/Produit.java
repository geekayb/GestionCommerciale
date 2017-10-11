/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Ayoub
 */
@Entity
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reference;
     private String libelle;
    private String description;
    private Double prixDirham;
    private Double prixDollar;
    private Double prixEuro;
    private Double quantiteStock;
    @ManyToOne
    private Fournisseur fournisseur;

    @ManyToOne
    public Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }
    
    

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrixDirham() {
        return prixDirham;
    }

    public void setPrixDirham(Double prixDirham) {
        this.prixDirham = prixDirham;
    }

    public Double getPrixDollar() {
        return prixDollar;
    }

    public void setPrixDollar(Double prixDollar) {
        this.prixDollar = prixDollar;
    }

    public Double getPrixEuro() {
        return prixEuro;
    }

    public void setPrixEuro(Double prixEuro) {
        this.prixEuro = prixEuro;
    }
    
    

    public Double getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Double quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
    
    
    

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reference != null ? reference.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produit)) {
            return false;
        }
        Produit other = (Produit) object;
        if ((this.reference == null && other.reference != null) || (this.reference != null && !this.reference.equals(other.reference))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return libelle;
    }
    
}
