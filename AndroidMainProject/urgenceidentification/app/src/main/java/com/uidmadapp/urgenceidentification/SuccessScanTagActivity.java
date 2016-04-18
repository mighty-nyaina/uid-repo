package com.uidmadapp.urgenceidentification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uidmadapp.urgenceidentification.adapter.MaladieAdapter;
import com.uidmadapp.urgenceidentification.model.InformationComplet;
import com.uidmadapp.urgenceidentification.model.Maladie;
import com.uidmadapp.urgenceidentification.model.MapsActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class SuccessScanTagActivity extends AppCompatActivity {

    InformationComplet info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_scan_tag);

        info = (InformationComplet) getIntent().getSerializableExtra("InfoComplet");
        RecyclerView recList = (RecyclerView) findViewById(R.id.liste_maladie);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        final TextView value_hopital_signaler = (TextView)findViewById(R.id.value_hopital_signaler);
        value_hopital_signaler.setText(info.getHopital_proche()[0].getNom());
        List<Maladie> liste = Arrays.asList(info.getMaladie());
        MaladieAdapter ca = new MaladieAdapter(liste, this);
        recList.setAdapter(ca);

        final Button btn_show_hospital = (Button) findViewById(R.id.btn_show_hospital);
        btn_show_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessScanTagActivity.this, MapsActivity.class);
                intent.putExtra("InfoComplet", info);
                startActivity(intent);
            }
        });
    }
}
