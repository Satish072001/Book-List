package com.example.android.booklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Input> {
    public ListAdapter(Activity context, ArrayList<Input> inputs){
        super(context,0,inputs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.result, parent, false);
        }
        Input current=getItem(position);
        ImageView image = (ImageView)listItemView.findViewById(R.id.book_image);
        Picasso.get().load(current.getMimage()).into(image);
        TextView title=(TextView)listItemView.findViewById(R.id.title);
        title.setText(current.getMtitle());
        TextView author=(TextView)listItemView.findViewById(R.id.author);
        author.setText(current.getMauthor());




        return listItemView;

    }
}
