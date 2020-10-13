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

import com.coderpakistan.learningbank.Game.GameLevelActivity;
import com.coderpakistan.learningbank.HelperClasses.SharedHelper;
import com.coderpakistan.learningbank.LearnVocabulary.DirectLevelVocabularyActivity;
import com.coderpakistan.learningbank.Models.SessionVocabularyModel;
import com.coderpakistan.learningbank.R;

import java.util.List;

public class SessionVocabularyAdapter extends RecyclerView.Adapter<SessionVocabularyAdapter.modelViewHolder> {
    List<SessionVocabularyModel> list;
    Context context;
    SharedHelper sharedHelper;
    public SessionVocabularyAdapter(List<SessionVocabularyModel> list, Context context) {
        this.list = list;
        this.context = context;
        sharedHelper=new SharedHelper(context);
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_session_layout, parent, false);
        return new modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int position) {
        final SessionVocabularyModel model=list.get(position);
        holder.name.setText(model.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isUnlock()){
                if (model.getType().equals("game"))
                {
                    sharedHelper.putKey("gamesessionid", model.getId());
                    sharedHelper.putKey("gamesessionname", model.getName());
                    Intent intent=new Intent(context, GameLevelActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else {
                    sharedHelper.putKey("directsessionid", model.getId());
                    sharedHelper.putKey("directsessionname", model.getName());
                    Intent intent=new Intent(context, DirectLevelVocabularyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                }
                else {
                    Toast.makeText(context, "Locked", Toast.LENGTH_SHORT).show();
                }
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
