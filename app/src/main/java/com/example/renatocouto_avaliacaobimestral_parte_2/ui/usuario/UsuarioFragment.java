package com.example.renatocouto_avaliacaobimestral_parte_2.ui.usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renatocouto_avaliacaobimestral_parte_2.R;
import com.example.renatocouto_avaliacaobimestral_parte_2.databinding.FragmentUsuarioBinding;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;
import com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo.JogoFragment;
import com.example.renatocouto_avaliacaobimestral_parte_2.ui.listarpokemon.ItemListarPokemonAdapter;
import com.example.renatocouto_avaliacaobimestral_parte_2.util.Mensagens;

import java.io.Serializable;
import java.util.List;


public class UsuarioFragment extends Fragment {
    private FragmentUsuarioBinding binding;
    private UsuarioViewModel usuarioViewModel;
    private List<Result> resultList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DadosRepository dadosRepository = new DadosRepository(requireActivity().getApplication());
        UsuarioViewModelFactory factory = new UsuarioViewModelFactory(dadosRepository);

        usuarioViewModel = new ViewModelProvider(this, factory)
                .get(UsuarioViewModel.class);

        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        observeLista();
        binding.buttonGabarito.setOnClickListener(v -> buscar());

        // ainda desenvolvendo
        //binding.buttonJogar.setOnClickListener(v-> iniciarJogo(resultList));

        return root;
    }

    private void buscar() {
        validarCampos();

        String numero = binding.editTextNumero.getText().toString();
        int qt = convertValidaNumero(numero);

        if (qt == -1) {
            binding.editTextNumero.requestFocus();
            binding.editTextNumero.setError(getString(R.string.numero_menor_que_2));
        } else if (qt == -2) {
            binding.editTextNumero.requestFocus();
            binding.editTextNumero.setError(getString(R.string.numero_maior_que_50));
        } else if (qt >= 2 && qt <= 50) {
            usuarioViewModel.listarAleatorioBanco(qt);
        }

    }

    private void validarCampos() {
        String numero = binding.editTextNumero.getText().toString();
        String nome = binding.editTextNome.getText().toString();
        if (nome.isEmpty()) {
            binding.editTextNome.requestFocus();
            binding.editTextNome.setError(getString(R.string.campo_vazio));

        } else if (numero.isEmpty()) {
            binding.editTextNumero.requestFocus();
            binding.editTextNumero.setError(getString(R.string.campo_vazio));
        }
    }

    private int convertValidaNumero(String numeroStr) {
        try {
            int numero = Integer.parseInt(numeroStr);
            return checarNumero(numero);
        } catch (NumberFormatException e) {
            return -7; //codigo para nota invalida
        }
    }

    private int checarNumero(int numero) {
        if (numero >= 2 && numero <= 50) {
            return numero;
        }
        return numero < 2 ? -1 : -2; // -1 para nota negativa, -2 para acima do máximo
    }

    private void observeLista() {
        usuarioViewModel.getPokemons().observe(getViewLifecycleOwner(), results -> {
            if (results != null) {
                resultList = results;
                if (resultList.isEmpty()) {
                    Mensagens.showErro(requireView(), getString(R.string.erro_baixar_pokemons));
                } else {
                    configurarRecyclerViews(resultList);
                }
            } else {
                Mensagens.showErro(requireView(), getString(R.string.erro_ao_baixar_dados));
            }
        });
    }

    public void iniciarJogo(List<Result> results){
        Bundle result = new Bundle();
        result.putSerializable("lista", (Serializable) results);

        JogoFragment jogoFragment= new JogoFragment();
        jogoFragment.setArguments(result);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, jogoFragment)
                .addToBackStack(null)
                .commit();
    }


    private void configurarRecyclerViews(List<Result> results) {
        RecyclerView recyclerViewPokemon = binding.recyclerViewLista;
        recyclerViewPokemon.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewPokemon.setAdapter(new ItemListarPokemonAdapter(results));
    }

}
