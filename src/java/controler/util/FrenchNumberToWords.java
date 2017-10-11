
package controler.util;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import java.text.*;

public class FrenchNumberToWords {

  private static final String[] dizaineNames = {
    "",
    "",
    "VINGT",
    "TRENTE",
    "QUARANTE",
    "CINQUANTE",
    "SOIXANTE",
    "SOIXANTE",
    "QUATRE-VINGT",
    "QUATRE-VINGT"
  };

  private static final String[] uniteNames1 = {
    "",
    "UN",
    "DEUX",
    "TROIS",
    "QUATRE",
    "CINQ",
    "SIX",
    "SEPT",
    "HUIT",
    "NEUF",
    "DIX",
    "ONZE",
    "DOUZE",
    "TREIZE",
    "QUATORZE",
    "QUINZE",
    "SEIZE",
    "DIX-SEPT",
    "DIX-HUIT",
    "DIX-NEUF"
  };

  private static final String[] uniteNames2 = {
    "",
    "",
    "DEUX",
    "TROIS",
    "QUATRE",
    "CINQ",
    "SIX",
    "SEPT",
    "HUIT",
    "NEUF",
    "DIX"
  };

  private static String convertZeroToHundred(int number) {

    int laDizaine = number / 10;
    int lUnite = number % 10;
    String resultat = "";

    switch (laDizaine) {
    case 1 :
    case 7 :
    case 9 :
      lUnite = lUnite + 10;
      break;
    default:
    }

    // séparateur "-" "ET"  ""
    String laLiaison = "";
    if (laDizaine > 1) {
      laLiaison = "-";
    }
    // cas particuliers
    switch (lUnite) {
    case 0:
      laLiaison = "";
      break;
    case 1 :
      if (laDizaine == 8) {
        laLiaison = "-";
      }
      else {
        laLiaison = " et ";
      }
      break;
    case 11 :
      if (laDizaine==7) {
        laLiaison = " et ";
      }
      break;
    default:
    }

    // dizaines en lettres
    switch (laDizaine) {
    case 0:
      resultat = uniteNames1[lUnite];
      break;
    case 8 :
      if (lUnite == 0) {
        resultat = dizaineNames[laDizaine];
      }
      else {
        resultat = dizaineNames[laDizaine] 
                                + laLiaison + uniteNames1[lUnite];
      }
      break;
    default :
      resultat = dizaineNames[laDizaine] 
                              + laLiaison + uniteNames1[lUnite];
    }
    return resultat;
  }

  private static String convertLessThanOneThousand(int number) {

    int lesCentaines = number / 100;
    int leReste = number % 100;
    String sReste = convertZeroToHundred(leReste);

    String resultat;
    switch (lesCentaines) {
    case 0:
      resultat = sReste;
      break;
    case 1 :
      if (leReste > 0) {
        resultat = "CENT " + sReste;
      }
      else {
        resultat = "CENT";
      }
      break;
    default :
      if (leReste > 0) {
        resultat = uniteNames2[lesCentaines] + " CENT " + sReste;
      }
      else {
        resultat = uniteNames2[lesCentaines] + " CENTS";
      }
    }
    return resultat;
  }

  public static String convert(long number) {
    // 0 à 999 999 999 999
    if (number == 0) { return "ZÉRO"; }

    String snumber = Long.toString(number);

    // pad des "0"
    String mask = "000000000000";
    DecimalFormat df = new DecimalFormat(mask);
    snumber = df.format(number);

    // XXXnnnnnnnnn 
    int lesMilliards = Integer.parseInt(snumber.substring(0,3));
    // nnnXXXnnnnnn
    int lesMillions  = Integer.parseInt(snumber.substring(3,6)); 
    // nnnnnnXXXnnn
    int lesCentMille = Integer.parseInt(snumber.substring(6,9)); 
    // nnnnnnnnnXXX
    int lesMille = Integer.parseInt(snumber.substring(9,12));    

    String tradMilliards;
    switch (lesMilliards) {
    case 0:
      tradMilliards = "";
      break;
    case 1 :
      tradMilliards = convertLessThanOneThousand(lesMilliards) 
      + " MILLIARD ";
      break;
    default :
      tradMilliards = convertLessThanOneThousand(lesMilliards) 
      + " MILLIARDS ";
    }
    String resultat =  tradMilliards;

    String tradMillions;
    switch (lesMillions) {
    case 0:
      tradMillions = "";
      break;
    case 1 :
      tradMillions = convertLessThanOneThousand(lesMillions) 
      + " MILLION ";
      break;
    default :
      tradMillions = convertLessThanOneThousand(lesMillions) 
      + " MILLIONS ";
    }
    resultat =  resultat + tradMillions;

    String tradCentMille;
    switch (lesCentMille) {
    case 0:
      tradCentMille = "";
      break;
    case 1 :
      tradCentMille = "MILLE ";
      break;
    default :
      tradCentMille = convertLessThanOneThousand(lesCentMille) 
      + " MILLE ";
    }
    resultat =  resultat + tradCentMille;

    String tradMille;
    tradMille = convertLessThanOneThousand(lesMille);
    resultat =  resultat + tradMille;

    return resultat;
  }

  public static void main(String[] args) {

    System.out.println("*** " + FrenchNumberToWords.convert(2000000));

    /*
     *** OUTPUT
     *** zéro
     *** neuf
     *** dix-neuf
     *** vingt et un
     *** vingt-huit
     *** soixante et onze
     *** soixante-douze
     *** quatre-vingt
     *** quatre-vingt-un
     *** quatre-vingt-neuf
     *** quatre-vingt-dix
     *** quatre-vingt-onze
     *** quatre-vingt-dix-sept
     *** cent
     *** cent un
     *** cent dix
     *** cent vingt
     *** deux cents
     *** deux cent un
     *** deux cent trente-deux
     *** neuf cent quatre-vingt-dix-neuf
     *** mille
     *** mille un
     *** dix mille
     *** dix mille un
     *** cent mille
     *** deux millions
     *** trois milliards
     *** deux milliards cent quarante-sept millions 
     **          quatre cent quatre-vingt-trois mille six cent quarante-sept
     */
  }
   public static BigDecimal  deuxChiffre(BigDecimal myNumber){
       BigDecimal partie1= new BigDecimal(myNumber.toBigInteger());
       BigDecimal partie2 =myNumber.subtract(new BigDecimal(myNumber.toBigInteger()));
       
       String partie2Str = partie2+"";
       partie2Str =partie2Str.substring(2);
       
        int par2 = new Integer(partie2Str);
        
        System.out.println("partie1 = "+partie1);
        System.out.println("partie2Str == "+partie2Str);
        System.out.println(" par2 =="+par2);
        
        
        if(par2==0){
            
            String par1 = partie1+".00";
           return new BigDecimal(par1);
        }
        
        
        System.out.println("hadiiii hyiaaaa "+partie2Str);
        if(partie2Str.startsWith("0")){
            System.out.println("taybeda b zeroooooooooooooo");
            return new BigDecimal(partie1+".0"+(partie2Str+"").charAt(1));
        }
         if(par2<10){
            par2*=10;
        }
         return  new BigDecimal(partie1+"."+(par2+"").charAt(0)+""+(par2+"").charAt(1));
   }
  public static String  convert(BigDecimal myNumber){
       BigDecimal partie1= new BigDecimal(myNumber.toBigInteger());
       BigDecimal partie2 =myNumber.subtract(new BigDecimal(myNumber.toBigInteger()));
       
       String partie2Str = partie2+"";
       partie2Str =partie2Str.substring(2);
       
        int par2 = new Integer(partie2Str);
        
        System.out.println("partie1 = "+partie1);
        System.out.println("partie2Str == "+partie2Str);
        System.out.println(" par2 =="+par2);
        
        
        if(par2==0){
            
            String par1 = partie1+"";
            return convert(new Integer(par1))+" DHs";
        }
        
        
        System.out.println("hadiiii hyiaaaa "+partie2Str);
        if(partie2Str.startsWith("0")){
            System.out.println("taybeda b zeroooooooooooooo");
            return convert(new Integer(partie1+""))+"  DHs ET "+convert(par2)+" CENTIMES";
        }
         if(par2<10){
            par2*=10;
        }
       return convert(new Integer(partie1+""))+"  DHs ET "+convert(new Integer(par2+""))+" CENTIMES";
        
    }
  
  public static String printingFormat(BigDecimal mynb){
      NumberFormat n = NumberFormat.getCurrencyInstance();
        String myString=n.format(mynb);
        myString=myString.substring(0, myString.length()-1);
        System.out.println("ana fost printing forma et mystrr ="+myString);
        return myString;
  }
}
