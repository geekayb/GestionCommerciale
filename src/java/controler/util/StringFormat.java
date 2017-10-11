/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

/**
 *
 * @author Ayoub
 */
public class StringFormat {

    public static String format(String nombre) {

        String resReplace = nombre.replace('.', 'v');
        String[] res = resReplace.split("v");

        if (res.length == 1) {
            nombre = nombre + ".00";
            return nombre;
        } else {
            if (res[1].length() == 1) {
                return nombre + "0";
            } else if (res[1].length() == 2) {
                return nombre;
            } else {
                Integer i = new Integer(res[1].charAt(2) + "");
                if (i > 5) {
                    return res[0] + "." + res[1].charAt(0) + (new Integer(res[1].charAt(1) + "") + 1);
                } else {
                    return res[0] + "." + res[1].charAt(0) + res[1].charAt(1);
                }
            }
        }

    }

    public static String formatSansVergule(String nombre) {

        String resReplace = nombre.replace('.', 'v');
        String[] res = resReplace.split("v");

        if (res.length == 2) {
            if (res[1].charAt(1) == '0') {
                if (res[1].charAt(0) == '0') {
                    return res[0];
                } else {
                    return res[0] + "." + res[1].charAt(0);
                }
            } 

        } 
            return res[0] + "." + res[1].charAt(0)+""+res[1].charAt(1);
        

    }

    public static void main(String[] args) {

        System.out.println(StringFormat.format("3.16677"));
        System.out.println(StringFormat.format("3.16"));
        System.out.println(formatSansVergule("3.00"));
    }
}
