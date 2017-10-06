package com.example.khutsomatlala.hackaton_user11;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class imageListAdapter extends ArrayAdapter<ImageUpload> {

    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;


    public imageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.listImage = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(resource, null);

        //referencing item_list
        final ImageView img = view.findViewById(R.id.imgPlacePic);
        TextView txtPlaceName = view.findViewById(R.id.tvPlaceName);
        TextView txt = view.findViewById(R.id.tvLike);
        View ViewName = view.findViewById(R.id.ViewName);
        TextView price = view.findViewById(R.id.tvPrice);

        //Assigning data
        txtPlaceName.setText(listImage.get(position).getPlaceName());
        txt.setText(listImage.get(position).getPlaceWebsite());
        price.setText(listImage.get(position).getPlaceAddress());


        Glide.with(context).load(listImage.get(position).getUrI()).into(img);


        ViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailActivity.class);

                String lat = listImage.get(position).getPlaceLatitude();
                String lon = listImage.get(position).getPlaceLongitude();
                String name = listImage.get(position).getPlaceName();
                String call = listImage.get(position).getPlaceCell();
                String infor = listImage.get(position).getPlaceInfo();
                String address = listImage.get(position).getPlaceAddress();
                String hours = listImage.get(position).getPlaceHours();
                String pic = listImage.get(position).getUrI();
                String webside = listImage.get(position).getPlaceAddress();


                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("name", name);
                intent.putExtra("call", call);
                intent.putExtra("infor", infor);
                intent.putExtra("address", address);
                intent.putExtra("hours", hours);
                intent.putExtra("pic", pic);

                context.startActivity(intent);

            }
        });

//tabbing on the image
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final LayoutInflater inflater = context.getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                ImageView dia_pic = dialogView.findViewById(R.id.dia_pic);
                TextView dia_name = dialogView.findViewById(R.id.dia_name);
                //changed
                ImageView dia_call = dialogView.findViewById(R.id.dia_call);
                final ImageView dia_direction = dialogView.findViewById(R.id.dia_direction);

                Glide.with(context).load(listImage.get(position).getUrI()).centerCrop().into(dia_pic);

                dia_name.setText(listImage.get(position).getPlaceName());

                builder.setView(dialogView);


                dialogView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Intent intent = new Intent(getContext(), max_pic.class);

                        //  String pic = listImage.get(position).getUrI();

                        //    intent.putExtra("pic",pic);

                        //    context.startActivity(intent);

                    }
                });
                final AlertDialog alertDialog = builder.create();


                dia_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String call = listImage.get(position).getPlaceCell();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + call));
                        context.startActivity(intent);

                        alertDialog.dismiss();
                    }
                });

                dia_direction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getContext(), MapsActivity.class);

                        String lat = listImage.get(position).getPlaceLatitude();
                        String lon = listImage.get(position).getPlaceLongitude();
                        String name = listImage.get(position).getPlaceName();

                        intent.putExtra("lat", lat);
                        intent.putExtra("lon", lon);
                        intent.putExtra("name", name);
                        context.startActivity(intent);

                        alertDialog.dismiss();

                    }
                });

                alertDialog.show();

            }

        });

        return view;
    }
}
