package com.example.renatocouto_avaliacaobimestral_parte_2.ui.paineladmin;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;


// * https://developer.android.com/reference/androidx/lifecycle/ViewModelProvider.Factory
// * https://mahendranv.github.io/posts/viewmodel-store/ (em kotlin)
// * https://www.youtube.com/watch?v=_T8ln2ig5hE (em kotlin, minuto 4:35)

public class SettingsViewModelFactory implements ViewModelProvider.Factory {
    private final DadosRepository repository;

    public SettingsViewModelFactory(DadosRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)){
            return (T) new SettingsViewModel(repository);
        }
        throw new IllegalArgumentException("erro ao retornar a viewModel");
    }
}
