package com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.List;


// * https://developer.android.com/reference/androidx/lifecycle/ViewModelProvider.Factory
// * https://mahendranv.github.io/posts/viewmodel-store/ (em kotlin)
// * https://www.youtube.com/watch?v=_T8ln2ig5hE (em kotlin, minuto 4:35)

public class JogoViewModelFactory implements ViewModelProvider.Factory {
    private final DadosRepository repository;
    private final List<Result> results;

    public JogoViewModelFactory(DadosRepository repository, List<Result> results) {
        this.repository = repository;
        this.results = results;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(JogoViewModel.class)) {
            return (T) new JogoViewModel(repository, results);
        }
        throw new IllegalArgumentException("erro ao retornar a viewModel");
    }
}
