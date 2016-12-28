package com.example.mustafa.toplusms;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mustafa on 26.09.2016.
 */
public class Package {
    String packageDate,packageOriginator,packageTotal, successful;

    ArrayList<String> packageList;

    public Package(String packageId, String packageDate, String packageState, String packageOriginator,
                   String packageCredit, String packageTotal, String successful, String unsuccessful,
                   String timeOut, String standBy, String other, String extradite) {

        this.packageDate = packageDate;
        this.packageOriginator = packageOriginator;
        this.packageTotal = packageTotal;
        this.successful = successful;

        packageList = new ArrayList<String>(Arrays.asList(packageId,packageDate,packageOriginator,
                packageState,packageCredit,packageTotal,successful,unsuccessful,timeOut,standBy,other,extradite));
    }

    public String getPackageDate() {
        return packageDate;
    }

    public String getPackageOriginator() {
        return packageOriginator;
    }

    public String getPackageTotal() {
        return packageTotal;
    }

    public String getSuccessful() {
        return successful;
    }

    public ArrayList<String> getPackageList() {
        return packageList;
    }
}
