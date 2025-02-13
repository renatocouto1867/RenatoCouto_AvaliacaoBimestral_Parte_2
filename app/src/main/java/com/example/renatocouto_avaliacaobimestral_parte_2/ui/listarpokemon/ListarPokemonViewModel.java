package com.example.renatocouto_avaliacaobimestral_parte_2.ui.listarpokemon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.ArrayList;
import java.util.List;

public class ListarPokemonViewModel extends ViewModel {

    private final DadosRepository dadosRepository;
    private final MutableLiveData<List<Result>> liveDataRecebido;
    private final MutableLiveData<String> liveDataMensagem;

    public ListarPokemonViewModel(DadosRepository dadosRepository) {
        this.dadosRepository = dadosRepository;
        liveDataMensagem = new MutableLiveData<>();
        liveDataRecebido = new MutableLiveData<>();
    }

    public void listar50Pokemons() {

        dadosRepository.obter50Pokemons(new DadosRepository.OnBaixarListener() {
            @Override
            public void sucesso(List<Result> results) {
                List<Result> pokemonList = new ArrayList<>(results);
                salvaLista(results);
                liveDataRecebido.postValue(new ArrayList<>(pokemonList));
            }

            @Override
            public void erro(String erro) {
                liveDataRecebido.postValue(new ArrayList<>());
            }
        });
    }

    public LiveData<List<Result>> getPokemons() {
        return liveDataRecebido;
    }

    public LiveData<String> getMensagem() {
        return liveDataMensagem;
    }


    private void salvaLista(List<Result> pokemonList) {
        dadosRepository.bancoSalvarListaPokemons(pokemonList, new DadosRepository.OnSalvarListaListener() {
            @Override
            public void sucesso(long[] ids) {
                if (ids.length > 0) {
                    liveDataMensagem.postValue("sucesso");
                }
            }

            @Override
            public void erro(String erro) {
                liveDataMensagem.postValue("erro");
            }
        });
    }

}// listarPokemonViewModel




