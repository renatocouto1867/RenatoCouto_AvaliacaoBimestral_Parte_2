package com.example.renatocouto_avaliacaobimestral_parte_2.ui.jogo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Pokemon;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.List;

public class JogoViewModel extends ViewModel {

    private final DadosRepository dadosRepository;
    private final List<Result> resultsRecebidos;
    private final MutableLiveData<String> liveDataMenagem;
    private final MutableLiveData<List<Pokemon>> liveDataRecebido;

    public JogoViewModel(DadosRepository dadosRepository, List<Result> results) {
        this.dadosRepository = dadosRepository;
        this.liveDataRecebido = new MutableLiveData<>();
        this.liveDataMenagem = new MutableLiveData<>();
        this.resultsRecebidos = results;
    }

    public void listaPokemons() {

        dadosRepository.obterDetalhesPokemon(resultsRecebidos, new DadosRepository.OnPokemonDetalhadoListener() {
            @Override
            public void sucesso(List<Pokemon> pokemons) {
                liveDataRecebido.postValue(pokemons);
            }

            @Override
            public void erro(String erro) {
                liveDataMenagem.postValue("erro");
            }
        });

    }

    public LiveData<List<Pokemon>> getPokemons() {
        return liveDataRecebido;
    }

    public LiveData<String> getMensagem() {
        return liveDataMenagem;
    }


}