package com.example.mustafa.toplusms;

/**
 * Created by mustafa on 17.08.2016.
 */
public class Contact {

    //KİŞİLERİN BİLGİLERİNİ TUTAN REHBER SINIFI
    private String cName, cNumber;
    boolean checkbox;
    TextDrawable drawable;

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public Contact(String cName, String cNumber, TextDrawable drawable) {
        this.cName = cName;
        this.cNumber = cNumber;
        this.drawable = drawable;
    }

    public TextDrawable getDrawable() {
        return drawable;
    }

    public String getcName() {
        return cName;
    }
    public String getcNumber() {
        return cNumber;
    }

}
