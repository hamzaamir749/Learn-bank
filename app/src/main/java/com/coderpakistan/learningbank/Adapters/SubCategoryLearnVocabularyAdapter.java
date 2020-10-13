package com.coderpakistan.learningbank.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.LearnVocabulary.SessionVocabularyActivity;
import com.coderpakistan.learningbank.Models.SubCategoryLearnVocabularyModel;
import com.coderpakistan.learningbank.R;

import java.util.List;

public class SubCategoryLearnVocabularyAdapter extends RecyclerView.Adapter<SubCategoryLearnVocabularyAdapter.modelViewHolder> {

    List<SubCategoryLearnVocabularyModel> list;
    Context context;
    SharedHelper sharedHelper;

    public SubCategoryLearnVocabularyAdapter(List<SubCategoryLearnVocabularyModel> list, Context context) {
        this.list = list;
        this.context = context;
        sharedHelper = new SharedHelper(context);
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subcategorylearnvocabulary_layout, parent, false);
        return new modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, final int position) {
        final SubCategoryLearnVocabularyModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedHelper.putKey("categoryid", list.get(position).getId());
                sharedHelper.putKey("categoryname", list.get(position).getName());
                Intent intent = new Intent(context, SessionVocabularyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class modelViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        LinearLayout item;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            item = itemView.findViewById(R.id.item);
        }
    }
}
