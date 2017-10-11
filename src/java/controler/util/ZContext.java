/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;
 
//import bean.Commune;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;

/**
 *
 * @author moulaYounes
 */
public class ZContext {

    private static List<Item> myMap = new ArrayList<>();
//    private static List<Commune> communes = new ArrayList<>();
//
//    public static int findCommunePosition(Commune commune) {
//        int i = 0;
//        for (Commune myCommune : communes) {
//            if (Objects.equals(commune, myCommune)) {
//                return i;
//            }
//            i++;
//        }
//        return -1;
//    }

//    private static int registreCommune(Commune commune) {
//        int indexCommune = findCommunePosition(commune);
//        if (indexCommune == -1) {
//            communes.add(commune);
//            return indexCommune;
//        }
//        return -1;
//    }

  

    public static Object getAttribut(String name) {
        for (int i = 0; i < myMap.size(); i++) {
            Item sessionItem = myMap.get(i);
            if (sessionItem.getKey().equals(name)) {
                return sessionItem.getObject();
            }
        }
        return null;
    }

    public static int updateAttribute(Object obj, String name) {
        int index = indexOfAttribut(name);
        if (index == -1) {
            createAtrribute(obj, name);
            return 1;
        } else {
            myMap.get(index).setObject(obj);
            return 2;
        }
    }

    public static int delete(String name) {
        int index = indexOfAttribut(name);
        if (index != -1) {
            myMap.remove(index);
            return 1;
        }
        return -1;

    }

    public static void clear() {
        myMap.clear();
    }

    public static void createAtrribute(Object obj, String name) {
        Item sessionItem = new Item();
        sessionItem.setKey(name);
        sessionItem.setObject(obj);
        myMap.add(sessionItem);
    }

    private static int indexOfAttribut(String name) {
        for (int i = 0; i < myMap.size(); i++) {
            Item sessionItem = myMap.get(i);
            if (sessionItem.getKey().equals(name)) {
                return i;
            }
        }
        return -1;

    }
}
