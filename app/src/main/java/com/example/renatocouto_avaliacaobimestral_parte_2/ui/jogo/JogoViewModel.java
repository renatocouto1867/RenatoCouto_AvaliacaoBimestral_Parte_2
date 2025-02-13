package com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.List;

public class JogoViewModel extends ViewModel {

    private final DadosRepository dadosRepository;
    private final MutableLiveData<List<Result>> liveDataRecebido;

    public JogoViewModel(DadosRepository dadosRepository) {
        this.dadosRepository = dadosRepository;
        liveDataRecebido = new MutableLiveData<>();
    }

    public LiveData<List<Result>> getPokemons() {
        return liveDataRecebido;
    }


}






