package com.example.jake.university.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jake.university.R;
import com.example.jake.university.profile.Singleton;
import com.example.jake.university.ranked.RankedAdapter;

import org.json.JSONException;
import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FragmentPayment extends Fragment {
    Button payLocation;
    Singleton singleton = Singleton.getInstance("0");

    public FragmentPayment() throws InterruptedException, ExecutionException, JSONException{
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        payLocation = (Button) view.findViewById(R.id.butPayLocation);
        ArrayList<PaymentItem> listTrue=null;
        ArrayList<PaymentItem> listFalse=null;
        payLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, new FragmentPaymentInfo()).addToBackStack(null).commit();
            }
        });
        PaymentAdapter adapterTrue, adapterFalse;

        listTrue = singleton.getPaidPayments();
        listFalse = singleton.getNotPaidPayments();

        ListView lvPayTrue = (ListView) view.findViewById(R.id.lv_pay_true);
        ListView lvPayFalse = (ListView) view.findViewById(R.id.lv_pay_false);
        adapterTrue = new PaymentAdapter(view.getContext(), R.layout.payment_item, listTrue);
        adapterFalse = new PaymentAdapter(view.getContext(), R.layout.payment_item, listFalse);

        lvPayFalse.setAdapter(adapterFalse);
        lvPayTrue.setAdapter(adapterTrue);
        return view;
    }
}
