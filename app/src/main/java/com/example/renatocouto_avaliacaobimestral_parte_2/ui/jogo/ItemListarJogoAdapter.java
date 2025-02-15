package com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.renatocouto_avaliacaobimestral_parte_2.R;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Pokemon;

import java.util.Collections;
import java.util.List;

//<a href="https://www.flaticon.com/br/icones-gratis/erro" title="erro ícones">Erro ícones criados por Idealogo Studio - Flaticon</a>
//<a href="https://www.flaticon.com/br/icones-gratis/upload-de-imagem" title="upload de imagem ícones">Upload de imagem ícones criados por JessHG - Flaticon</a>
public class ItemListarJogoAdapter extends RecyclerView.Adapter<ItemListarJogoAdapter.ViewHolder> {

    private final List<Pokemon> pokemons;

    public ItemListarJogoAdapter(List<Pokemon> pokemons) {
        this.pokemons = (pokemons != null) ? pokemons : Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jogo_pokemon_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.textViewNome.setText(pokemons.get(position).getName());
        holder.textHabilidade.setText(pokemons.get(position).getHabilidade());
        holder.textPontos.setText(String.valueOf(pokemons.get(position).getPontos()));

        if (!TextUtils.isEmpty(pokemons.get(position).getImagemUrl())) {
            Glide.with(holder.imageView.getContext())
                    .load(pokemons.get(position).getImagemUrl())
                    .placeholder(R.drawable.carregamento_de_imagem)
                    .error(R.drawable.erro)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.carregamento_de_imagem);
        }
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewNome;
        final TextView textHabilidade;
        final ImageView imageView;
        final TextView textPontos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.text_nome);
            textHabilidade = itemView.findViewById(R.id.text_habilidade);
            imageView = itemView.findViewById(R.id.image_pokemon);
            textPontos = itemView.findViewById(R.id.text_pontos);
        }
    }
}
