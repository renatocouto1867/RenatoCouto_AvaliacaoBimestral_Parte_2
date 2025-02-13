package com.example.renatocouto_avaliacaobimestral_parte_2.ui.usuario;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.ArrayList;
import java.util.List;

public class UsuarioViewModel extends ViewModel {

    private final DadosRepository dadosRepository;
    private final MutableLiveData<List<Result>> liveDataRecebido;

    public UsuarioViewModel(DadosRepository dadosRepository) {
        this.dadosRepository = dadosRepository;
        liveDataRecebido = new MutableLiveData<>();
    }

    public LiveData<List<Result>> getPokemons() {
        return liveDataRecebido;
    }

    public void listarAleatorioBanco(int qt) {

        dadosRepository.bancoGetAleatroioResults(qt, new DadosRepository.OnBaixarListener() {
            @Override
            public void sucesso(List<Result> results) {
                List<Result> pokemonList = new ArrayList<>(results);
                Log.e("tamanho lista Banco", String.valueOf(pokemonList.size()));
                if (pokemonList.isEmpty()) {
                    Log.e("tamanho lista Banco", "lista vazia");
                    liveDataRecebido.postValue(new ArrayList<>());
                }

                liveDataRecebido.postValue(new ArrayList<>(pokemonList));
            }

            @Override
            public void erro(String erro) {
                liveDataRecebido.postValue(new ArrayList<>());
            }
        });
    }

}// listarPokemonViewModel




