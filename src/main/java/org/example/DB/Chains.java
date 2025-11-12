package org.example.DB;

import java.util.ArrayList;

public class Chains extends Base <org.example.Algorithm.Chains.Base> {
    public Chains(long date, ArrayList<org.example.Algorithm.Chains.Base> data) {
        super(date, data);
    }

    @Override
    public void out() throws InterruptedException {
        System.out.println("items count: " + data.size());
        for (org.example.Algorithm.Chains.Base element : data)
            if (element.data != null) element.out();
    }
}
