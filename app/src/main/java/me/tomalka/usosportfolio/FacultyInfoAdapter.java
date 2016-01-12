package me.tomalka.usosportfolio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
        FacultyCard card = (FacultyCard) LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_card_row, parent, false);
        return new FacultyInfoHolder(card);
    }

    @Override
    public void onBindViewHolder(FacultyInfoHolder holder, int position) {
        FacultyInfo info = faculties.get(position);
        holder.card.fromFacultyInfo(info);
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }


    public static class FacultyInfoHolder extends RecyclerView.ViewHolder {
        public FacultyCard card;

        FacultyInfoHolder(FacultyCard card) {
            super(card);
            this.card = card;
        }
    }
}
