/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Ayoub
 */
@Entity
public class Societe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String raisonSociale;
    private String secteurActivite;
    private String activitePrincipale;
    private String statutJuridique;
    private String adresse;
    private String codePostale;
    private String pays;
    private String ville;
    private String email;
    private String telephone;
    private String mobile;
    private String fax;
    private String siteWeb;
    private String RC;
    private String numeroIF;
    private String TP;
    private String ICE;
    private String CNSS;
    private String capitaleSociale;
    private String numeroCompteBancaire;
    private String logo;
    private String dateCreation;
    private Boolean droitExploitation = false;

    @OneToMany(mappedBy = "societe")
    private List<Utilisateur> utilisateurs;

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

  

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        if (raisonSociale == null) {
            raisonSociale = "";
        }
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {

        this.raisonSociale = raisonSociale;
    }

    public String getSecteurActivite() {
        if (secteurActivite == null) {
            secteurActivite = "";
        }

        return secteurActivite;
    }

    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getActivitePrincipale() {
        if (activitePrincipale == null) {
            activitePrincipale = "";
        }
        return activitePrincipale;
    }

    public void setActivitePrincipale(String activitePrincipale) {
        this.activitePrincipale = activitePrincipale;
    }

    public String getStatutJuridique() {
        return statutJuridique;
    }

    public void setStatutJuridique(String statutJuridique) {
        this.statutJuridique = statutJuridique;
    }

    public String getAdresse() {
        if (adresse == null) {
            adresse = "";
        }

        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostale() {
         if (codePostale == null) {
            codePostale = "";
        }
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getPays() {
         if (pays == null) {
            pays = "";
        }
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        if (ville == null) {
            ville = "";
        }
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getEmail() {
         if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
         if (telephone == null) {
            telephone = "";
        }
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
         if (mobile == null) {
            mobile = "";
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        if (fax == null) {
            fax = "";
        }
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSiteWeb() {
         if (siteWeb == null) {
            siteWeb = "";
        }
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getRC() {
        if (RC == null) {
            RC = "";
        }
        return RC;
    }

    public void setRC(String RC) {
        this.RC = RC;
    }

    public String getNumeroIF() {
        if (numeroIF == null) {
            numeroIF = "";
        }
        return numeroIF;
    }

    public void setNumeroIF(String numeroIF) {
        this.numeroIF = numeroIF;
    }

    public String getTP() {
        if (TP == null) {
            TP = "";
        }
        return TP;
    }

    public void setTP(String TP) {
        this.TP = TP;
    }

    public String getICE() {
         if (ICE == null) {
            ICE = "";
        }
        return ICE;
    }

    public void setICE(String ICE) {
        this.ICE = ICE;
    }

    public String getCNSS() {
         if (CNSS == null) {
            CNSS = "";
        }
        return CNSS;
    }

    public void setCNSS(String CNSS) {
        this.CNSS = CNSS;
    }

    public String getCapitaleSociale() {
        if (capitaleSociale == null) {
            capitaleSociale = "";
        }
        return capitaleSociale;
    }

    public void setCapitaleSociale(String capitaleSociale) {
        this.capitaleSociale = capitaleSociale;
    }

    public String getNumeroCompteBancaire() {
         if (numeroCompteBancaire == null) {
            numeroCompteBancaire = "";
        }
        return numeroCompteBancaire;
    }

    public void setNumeroCompteBancaire(String numeroCompteBancaire) {
        this.numeroCompteBancaire = numeroCompteBancaire;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getDroitExploitation() {
        return droitExploitation;
    }

    public void setDroitExploitation(Boolean droitExploitation) {
        this.droitExploitation = droitExploitation;
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
        if (!(object instanceof Societe)) {
            return false;
        }
        Societe other = (Societe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return raisonSociale;
    }

}
