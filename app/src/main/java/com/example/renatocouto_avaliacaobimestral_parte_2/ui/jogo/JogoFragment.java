package com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.renatocouto_avaliacaobimestral_parte_2.databinding.FragmentJogoBinding;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.List;


public class JogoFragment extends Fragment {
    private FragmentJogoBinding binding;
    private JogoViewModel jogoViewModel;
    private List<Result> resultList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DadosRepository dadosRepository = new DadosRepository(requireActivity().getApplication());
        JogoViewModelFactory factory = new JogoViewModelFactory(dadosRepository);

        jogoViewModel = new ViewModelProvider(this, factory)
                .get(JogoViewModel.class);

        binding = FragmentJogoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

}
