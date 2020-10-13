package com.coderpakistan.learningbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coderpakistan.learningbank.Models.ProModel;
import com.coderpakistan.learningbank.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProAdapter extends RecyclerView.Adapter<ProAdapter.modelViewHolder> {
    List<ProModel> list;
    Context context;
    public static TextSpeech speech;

    public ProAdapter(List<ProModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pronounciation_layout, parent, false);
        return new modelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, final int position) {
        ProModel model = list.get(position);
        holder.name.setText(model.getWord());
        holder.english.setText(model.getEnglish());
        holder.urdu.setText(model.getUrdu());
        holder.sentence.setText(model.getSentence());
        holder.name.setText(model.getWord());
        Picasso.get().load(model.getImage()).into(holder.image);
        holder.sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speech!=null){
                    speech.onItemClick(list.get(position).getEnglish());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        TextView name, english, urdu, sentence;
        ImageView image, sound;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pro_name);
            english = itemView.findViewById(R.id.pro_english);
            urdu = itemView.findViewById(R.id.pro_urdu);
            sentence = itemView.findViewById(R.id.pro_sentence);
            image = itemView.findViewById(R.id.pro_image);
            sound = itemView.findViewById(R.id.pro_sound);
        }
    }
    public interface TextSpeech{
        void onItemClick(String text);
    }
}
