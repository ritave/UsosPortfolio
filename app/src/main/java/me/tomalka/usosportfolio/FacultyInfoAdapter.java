package me.tomalka.usosportfolio;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.tomalka.usosdroid.jsonapis.FacultyInfo;

public class FacultyInfoAdapter extends RecyclerView.Adapter<FacultyInfoAdapter.FacultyInfoHolder> {
    List<FacultyInfo> faculties;

    FacultyInfoAdapter(List<FacultyInfo> faculties) {
        this.faculties = faculties;
    }


    @Override
    public FacultyInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_child, parent, false);
        return new FacultyInfoHolder(v);
    }

    @Override
    public void onBindViewHolder(FacultyInfoHolder holder, int position) {
        FacultyInfo info = faculties.get(position);
        Context context = holder.itemView.getContext();

        Picasso.with(context).cancelRequest(holder.cover);
        Picasso.with(context).load(info.getCoverUrls().get("screen"))
                .placeholder(R.drawable.usoslogo1_gradient_dark)
                .error(R.drawable.usoslogo1_gradient_dark)
                .into(holder.cover);

        holder.title.setText(info.getFacName().get("pl"));
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }


    public static class FacultyInfoHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        ImageView cover;

        FacultyInfoHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.children_card);
            title = (TextView)itemView.findViewById(R.id.faculty_child_name);
            cover = (ImageView)itemView.findViewById(R.id.children_faculty_cover);
        }
    }
}
