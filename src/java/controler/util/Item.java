/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

/**
 *
 * @author moulaYounes
 */
public class Item {

    private String key;
    private Object object;

    public Item() {
    }

    public Item(String key, Object object) {
        this.key = key;
        this.object = object;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "SessionItem{" + "key=" + key + ", object=" + object + '}';
    }

}
