package com.example.mustafa.toplusms;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mustafa on 8.08.2016.
 */
public class XmlGet {

    //MUTLUSOFT SUNUCULARINA POST EDİLECEK XML DÜZENİ VE URL ADRESLERİ.
    public final String getCreditUrl = "https://smsgw.mutlucell.com/smsgw-ws/gtcrdtex";
    public final String sendSmsUrl = "https://smsgw.mutlucell.com/smsgw-ws/sndblkex";
    public final String getOriginatorUrl = "https://smsgw.mutlucell.com/smsgw-ws/gtorgex";
    public final String getInboxUrl = "https://smsgw.mutlucell.com/smsgw-ws/gtincmngapi";
    public final String reportNumberUrl = "https://smsgw.mutlucell.com/smsgw-ws/srchblkrprtapi";
    public final String packageReportUrl = "https://smsgw.mutlucell.com/smsgw-ws/gtsummaryex";
    public final String addBlackListUrl = "https://smsgw.mutlucell.com/smsgw-ws/addblklst";
    public final String getBlackListUrl = "https://smsgw.mutlucell.com/smsgw-ws/gtblklst";
    public final String deleteBlackListUrl = "https://smsgw.mutlucell.com/smsgw-ws/dltblklst";


    public String getCredit(String user, String pwd)
    {
        return "<?xml version='1.0' encoding='utf-8'?><smskredi ka='" + user + "' pwd='" + pwd + "'/>";
    }

    public String getOriginators(String user, String pwd)
    {
        return "<?xml version='1.0' encoding='utf-8'?><smsorig ka='" + user + "' pwd='" + pwd + "'/>";
    }

    public String getInbox(String subscriberNo, String pwd, String startDate, String endDate) {
        return "<?xml version='1.0' encoding='utf-8'?><increport aboneno= '" + subscriberNo
                + "' pwd= '" + pwd + "' startdate= '" +  startDate + "' enddate= '" + endDate + "'/>";
    }

    public String reportNumber(String user, String pwd, String gsmNo, String startDate, String endDate )
    {
       return "<?xml version='1.0' encoding='UTF-8'?><gonrapor ka='" + user + "' pwd='" + pwd + "' gsmno='" +
               gsmNo + "' tarih='" + startDate + "' bitis='" + endDate +"'/>";
    }

    public String packageReport(String user, String pwd, String startDate, String endDate )
    {
        return "<?xml version='1.0' encoding='UTF-8'?><gonrapor ka='" + user + "' pwd='" + pwd +
                "' tarih='" + startDate + "' bitis='" + endDate +"'/>";
    }

    public String addBlackList(String user, String pwd, String numbers)
    {
        return "<?xml version='1.0' encoding='UTF-8'?><addblacklist ka='" + user + "' pwd='" +
                pwd + "'><nums>'" + numbers + "'</nums></addblacklist>";
    }

    public String getBlackList(String user, String pwd)
    {
        return "<?xml version='1.0' encoding='UTF-8'?><blacklist ka='" + user + "' pwd='" + pwd + "'/>";
    }

    public String deleteBlackList(String user, String pwd, String numbers)
    {
        return "<?xml version='1.0' encoding='UTF-8'?><dltblacklist ka='" + user + "' pwd='" +
                pwd + "'><nums>'" + numbers + "'</nums></dltblacklist>";
    }

    public String sendSmsWithName(String user, String pwd, String orig, String msg, String sendNextDate,
                                  ArrayList<Contact> choosingContacts, String etChoosingContacts)
    {
        String nameTag = "\\{isim\\}";

        String xml =  "<?xml version='1.0' encoding='utf-8'?>" +
                "<smspack  ka='" + user + "' pwd='" + pwd + "' org='" + orig + "'";

        if(!sendNextDate.isEmpty())
        {
          xml+= " tarih='" + sendNextDate + "'";
        }

        xml += ">";

        for(int i = 0; i < choosingContacts.size(); i++) {
            String newMessage = msg.replaceAll(nameTag, choosingContacts.get(i).getcName());
            xml += "<mesaj><metin>" + xmlEncode(newMessage) + "</metin><nums>" +
            choosingContacts.get(i).getcNumber() + "</nums></mesaj>";
        }

        if(!etChoosingContacts.isEmpty()) {
            String newMessage = msg.replaceAll(nameTag, "");
            xml += "<mesaj><metin>" + xmlEncode(newMessage) + "</metin><nums>" +
                    etChoosingContacts + "</nums></mesaj>";
        }

        xml += "</smspack>";

        return xml;
    }

    private static String xmlEncode(String s) {
        s = s.replace("&", "&amp;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        s = s.replace("'", "&apos;");
        s = s.replace("\"",  "&quot");
        return s;
    }

        /*public String sendSms(String user,String pwd, String orig, String msg, String numbers)
    {
        return "<?xml version='1.0' encoding='utf-8'?><smspack  ka='" + user + "' pwd='" +
                pwd + "' org='" + orig + "'><mesaj><metin>" + xmlEncode(msg) + "</metin><nums>" +
                numbers + "</nums></mesaj></smspack>";
    }*/

}
