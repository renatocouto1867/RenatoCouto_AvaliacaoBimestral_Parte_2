package com.example.renatocouto_avaliacaobimestral_parte_2.ui.listarpokemon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renatocouto_avaliacaobimestral_parte_2.R;
import com.example.renatocouto_avaliacaobimestral_parte_2.databinding.FragmentPokemonListarBinding;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;
import com.example.renatocouto_avaliacaobimestral_parte_2.util.Mensagens;

import java.util.List;

public class ListarPokemonFragment extends Fragment {

    private FragmentPokemonListarBinding binding;
    private ListarPokemonViewModel listarPokemonViewModel;
    private List<Result> resultList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //para passar o objeto repositorio para a viewModel
        DadosRepository dadosRepository = new DadosRepository(requireActivity().getApplication());
        ListarPokemonViewModelFactory factory = new ListarPokemonViewModelFactory(dadosRepository);

        // aqui passo viewModelFactory ja com o viewModel com o repositorio
        listarPokemonViewModel = new ViewModelProvider(this, factory)
                .get(ListarPokemonViewModel.class);

        binding = FragmentPokemonListarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        exibeProgresso(true);

        listarPokemonViewModel.listar50Pokemons();

        observeLista();
        observeMensagem();

        return root;
    }

    private void observeLista() {
        listarPokemonViewModel.getPokemons().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                resultList = results;
                if (resultList.isEmpty()) {
                    binding.recyclerViewPokemons.setVisibility(View.GONE);
                    binding.tvEmptyState.setVisibility(View.VISIBLE);
                } else {
                    binding.tvEmptyState.setVisibility(View.GONE);
                    binding.recyclerViewPokemons.setVisibility(View.VISIBLE);
                    configurarRecyclerViews(resultList);
                }
            } else {
                Mensagens.showErro(requireView(), getString(R.string.erro_ao_baixar_dados));
            }
        });
    }

    private void observeMensagem() {
        listarPokemonViewModel.getMensagem().observe(getViewLifecycleOwner(), mensagem -> {
            if (mensagem.equals("erro")) {
                Mensagens.showErro(requireView(), getString(R.string.erro_ao_salvar_lista));
            }
        });
    }

    private void configurarRecyclerViews(List<Result> results) {

        if (results.isEmpty()) {
            binding.tvEmptyState.setVisibility(View.VISIBLE);
        } else {

            RecyclerView recyclerViewPokemon = binding.recyclerViewPokemons;
            recyclerViewPokemon.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            recyclerViewPokemon.setAdapter(new ItemListarPokemonAdapter(results));
        }

        exibeProgresso(false);
    }

    private void exibeProgresso(boolean exibir) {

        if (exibir) {
            binding.loadingContainer.setVisibility(View.VISIBLE);
            binding.recyclerViewPokemons.setVisibility(View.GONE);
            binding.tvEmptyState.setVisibility(View.GONE);
        } else {
            binding.loadingContainer.setVisibility(View.GONE);
            binding.recyclerViewPokemons.setVisibility(View.VISIBLE);
            binding.tvEmptyState.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}