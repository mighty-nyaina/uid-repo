package com.uidmadapp.urgenceidentification.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uidmadapp.urgenceidentification.DownloadImageTask;
import com.uidmadapp.urgenceidentification.R;
import com.uidmadapp.urgenceidentification.model.Maladie;

import java.util.List;

/**
 * Created by nyaina on 17/04/2016.
 */
public class MaladieAdapter extends RecyclerView.Adapter<MaladieAdapter.MaladieViewHolder> {

    private List<Maladie> listeMaladie;
    private Context context;
    public MaladieAdapter(List<Maladie> listeMaladie, Context context) {
        this.context = context;
        this.listeMaladie = listeMaladie;
    }


    @Override
    public int getItemCount() {
        return listeMaladie.size();
    }

    @Override
    public void onBindViewHolder(MaladieViewHolder MaladieViewHolder, int i) {
        Maladie m = listeMaladie.get(i);
        MaladieViewHolder.titre.setText(m.getTitre());
        new DownloadImageTask(MaladieViewHolder.img, true).execute(context.getResources().getString(R.string.images_url) + m.getImg());
        //MaladieViewHolder.conseil.setText(m.getConseil_description());
        MaladieViewHolder.conseil.setText(Html.fromHtml(m.getConseil_description()).toString());
    }

    @Override
    public MaladieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.maladie_list_layout, viewGroup, false);

        return new MaladieViewHolder(itemView);
    }

    public static class MaladieViewHolder extends RecyclerView.ViewHolder {

        protected TextView titre;
        protected ImageView img;
        protected TextView conseil;

        public MaladieViewHolder(View v) {
            super(v);
            titre =  (TextView) v.findViewById(R.id.secour_titre_maladie);
            img = (ImageView)  v.findViewById(R.id.secour_img);
            conseil = (TextView)  v.findViewById(R.id.secour_description);
        }
    }
}
