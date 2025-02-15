package com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renatocouto_avaliacaobimestral_parte_2.R;
import com.example.renatocouto_avaliacaobimestral_parte_2.databinding.FragmentJogoBinding;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Pokemon;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;
import com.example.renatocouto_avaliacaobimestral_parte_2.util.Mensagens;

import java.util.ArrayList;
import java.util.List;

public class JogoFragment extends Fragment {
    private FragmentJogoBinding binding;
    private JogoViewModel jogoViewModel;
    private List<Result> resultList;
    private List<Pokemon> pokemonList;
    private String nome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            resultList = (List<Result>) bundle.getSerializable("lista");
            nome = bundle.getString("nome");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DadosRepository dadosRepository = new DadosRepository(requireActivity().getApplication());
        JogoViewModelFactory factory = new JogoViewModelFactory(dadosRepository, resultList);

        jogoViewModel = new ViewModelProvider(this, factory).get(JogoViewModel.class);

        binding = FragmentJogoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        exibeProgresso(true);

        jogoViewModel.listaPokemons();

        observeViewModel();
        binding.textNome.setText(nome);


        return root;
    }

    private void observeViewModel() {
        jogoViewModel.getPokemons().observe(getViewLifecycleOwner(), pokemons -> {
            exibeProgresso(false);

            if (pokemons != null && !pokemons.isEmpty()) {
                pokemonList = new ArrayList<>(pokemons);
                binding.tvEmptyState.setVisibility(View.GONE);
                binding.recyclerViewPokemons.setVisibility(View.VISIBLE);
                configurarRecyclerView(pokemonList);
            } else {
                binding.recyclerViewPokemons.setVisibility(View.GONE);
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                Mensagens.showErro(requireView(), getString(R.string.erro_ao_baixar_dados));
            }
        });
    }

    private void configurarRecyclerView(List<Pokemon> pokemons) {
        RecyclerView recyclerViewJogo = binding.recyclerViewPokemons;
        recyclerViewJogo.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewJogo.setAdapter(new ItemListarJogoAdapter(pokemons));
    }

    private void exibeProgresso(boolean exibir) {
        binding.loadingContainer.setVisibility(exibir ? View.VISIBLE : View.GONE);
        binding.recyclerViewPokemons.setVisibility(exibir ? View.GONE : View.VISIBLE);
        binding.tvEmptyState.setVisibility(View.GONE);
    }
}
