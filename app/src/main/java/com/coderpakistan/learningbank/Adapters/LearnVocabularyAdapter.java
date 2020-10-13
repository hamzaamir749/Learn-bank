package com.coderpakistan.learningbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.LearnVocabulary.SessionVocabularyActivity;
import com.coderpakistan.learningbank.LearnVocabulary.SubCategoryLearnVocabularyActivity;
import com.coderpakistan.learningbank.Models.LearnVocabularyModel;
import com.coderpakistan.learningbank.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LearnVocabularyAdapter extends RecyclerView.Adapter<LearnVocabularyAdapter.modelViewHolder> {
    private List<LearnVocabularyModel> list;
    private Context context;
    SharedHelper sharedHelper;

    public LearnVocabularyAdapter(List<LearnVocabularyModel> list, Context context) {
        this.list = list;
        this.context = context;
        sharedHelper = new SharedHelper(context);
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_learnvocabulary_layout, parent, false);
        return new modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedHelper.putKey("categoryid", list.get(position).getId());
                sharedHelper.putKey("categoryname", list.get(position).getName());
                if (list.get(position).isCategory()) {
                    Intent intent = new Intent(context, SubCategoryLearnVocabularyActivity.class);
                    context.startActivity(intent);
                } else {

                    Intent intent = new Intent(context, SessionVocabularyActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        LinearLayout item;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            item = itemView.findViewById(R.id.item);
        }
    }
}
