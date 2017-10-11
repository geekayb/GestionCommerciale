/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Younes
 */
public class DateUtil {

    public static int[] findNext(int trimestre, int annee) {
        int nextTrimestre, nextAnnee;
        if (trimestre == 4) {
            nextTrimestre = 1;
            nextAnnee = annee + 1;
        } else {
            nextTrimestre = trimestre + 1;
            nextAnnee = annee;
        }

        return new int[]{nextTrimestre, nextAnnee};
    }

    public static Date getDateTauxTaxeAnuelle(int annee) {
        Calendar c1 = GregorianCalendar.getInstance();
        c1.set(annee + 1, Calendar.JANUARY, 01);
        return c1.getTime();
    }

    public static Date getDateTauxTaxe(int trimestre, int annee) {
        Calendar c1 = GregorianCalendar.getInstance();
        if (trimestre == 1) {
            c1.set(annee, Calendar.JANUARY, 01);  //January 30th 2000
        }
        if (trimestre == 2) {
            c1.set(annee, Calendar.APRIL, 01);  //January 30th 2000
        }
        if (trimestre == 3) {
            c1.set(annee, Calendar.JULY, 01);  //January 30th 2000
        }
        if (trimestre == 4) {
            c1.set(annee, Calendar.OCTOBER, 01);  //January 30th 2000
        }
        return c1.getTime();
    }

    public static int DateFinTrimestre(int trimestre, Date date, int annee) {
        Calendar c1 = GregorianCalendar.getInstance();
        if (trimestre == 1) {
            c1.set(annee, Calendar.MARCH, 31);  //January 30th 2000
        }
        if (trimestre == 2) {
            c1.set(annee, Calendar.JUNE, 30);  //January 30th 2000
        }
        if (trimestre == 3) {
            c1.set(annee, Calendar.SEPTEMBER, 30);  //January 30th 2000
        }
        if (trimestre == 4) {
            c1.set(annee, Calendar.DECEMBER, 31);  //January 30th 2000
        }
        if (date.after(c1.getTime())) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int differenceEnMois(Date date, int annee, int numeroTrimestre, int nbrPourCalculerRetard) {
        int nombreMois = 0, nombreAnnee = 0, dephasage = 0;
        nombreAnnee = (date.getYear() + 1900) - annee;
        if (numeroTrimestre == 1) {
            dephasage = 3;
        } else if (numeroTrimestre == 2) {
            dephasage = 6;
        } else if (numeroTrimestre == 3) {
            dephasage = 9;
        } else if (numeroTrimestre == 4) {
            dephasage = 12;
        }
        nombreMois = (date.getMonth() + 1) - (dephasage+ nbrPourCalculerRetard);

        return nombreAnnee * 12 + nombreMois;

    }

    public static int differenceEnMois(Date datePresentation, int annee) {
        int nombreMois = 0, nombreAnnee = 0;

        nombreAnnee = (datePresentation.getYear() + 1900) - annee;
        if (nombreAnnee > 0) {
            nombreAnnee--;
            nombreMois = (datePresentation.getMonth() + 1);
            return nombreAnnee * 12 + nombreMois;
        }

        return -1;

    }

    public static Date dateRetard(int nbrPourCalculerRetard, int annee) {
        Date date = new Date();
        date.setDate(1);
        if (nbrPourCalculerRetard == 12) {
            System.out.println("ha lmouchkiiiil pfff");
            nbrPourCalculerRetard = 0;
        }
        date.setMonth(nbrPourCalculerRetard + 1);
        date.setYear(annee + 1-1900);
        return date;
    }

    public static Date dateRetardTrimestrielle(int nbrPourCalculerRetard, int annee, int numeroTrimestre) {
        Date date = new Date();
        date.setDate(1);
        int dephasage = 0;
        if (numeroTrimestre == 1) {
            dephasage = 3;
        } else if (numeroTrimestre == 2) {
            dephasage = 6;
        } else if (numeroTrimestre == 3) {
            dephasage = 9;
        } else if (numeroTrimestre == 4) {
            dephasage = 0;
            annee += 1;
        }

        date.setMonth(nbrPourCalculerRetard + dephasage);
        date.setYear(annee-1900);
        return date;
    }

    public static java.sql.Date getSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Timestamp convertFromDateToTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static java.sql.Timestamp getSqlDateTime(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static String getYearOfCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(new Date());
    }

    public static String formateDate(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String formateDate(Date date) {
        return formateDate("dd/MM/yyyy", date);
    }
}
