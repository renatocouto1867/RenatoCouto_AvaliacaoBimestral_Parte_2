package com.example.renatocouto_avaliacaobimestral_parte_2.ui.paineladmin;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.repository.DadosRepository;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewModel extends ViewModel {

    private final DadosRepository dadosRepository;
    private final MutableLiveData<List<Result>> liveDataRecebido;
    private final MutableLiveData<String> liveDataMensagem;

    public SettingsViewModel(DadosRepository dadosRepository) {
        this.dadosRepository = dadosRepository;
        liveDataMensagem = new MutableLiveData<>();
        liveDataRecebido = new MutableLiveData<>();
    }


    public void listar50Pokemons() {

        dadosRepository.obter50Pokemons(new DadosRepository.OnBaixarListener() {
            @Override
            public void sucesso(List<Result> results) {
                liveDataRecebido.postValue(new ArrayList<>(results));
            }

            @Override
            public void erro(String erro) {
                liveDataRecebido.postValue(new ArrayList<>());
                liveDataMensagem.postValue(erro);
            }
        });
    }

    public LiveData<List<Result>> getPokemons() {
        return liveDataRecebido;
    }

    public LiveData<String> getMensagem() {
        return liveDataMensagem;
    }

    public void listarBanco() {

        dadosRepository.bancoGetAllResults(new DadosRepository.OnBaixarListener() {
            @Override
            public void sucesso(List<Result> results) {
                if (results.isEmpty()) {
                    Log.e("Banco de Dados", "Lista vazia");
                    liveDataRecebido.postValue(new ArrayList<>());
                }
                liveDataRecebido.postValue(new ArrayList<>(results));
            }

            @Override
            public void erro(String erro) {
                liveDataRecebido.postValue(new ArrayList<>());
            }
        });
    }

    public void salvarListaPokemons(List<Result> pokemons) {
        dadosRepository.bancoGetAllResults(new DadosRepository.OnBaixarListener() {
            @Override
            public void sucesso(List<Result> results) {
                List<Result> pokemonList = new ArrayList<>(results);
                if (pokemonList.size() > 0) {
                    liveDataMensagem.postValue("erro_existente");
                } else if (pokemonList.isEmpty()) {
                    salvaLista(pokemons);
                }
            }

            @Override
            public void erro(String erro) {
                liveDataMensagem.postValue("erro_salvar");
            }
        });

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
                liveDataMensagem.postValue(erro);
            }
        });
    }

    public void limparBancoDados() {
        dadosRepository.bancoDeleteAll(new DadosRepository.OnDeleteAllListener() {
            @Override
            public void onSuccess(int registrosAfetados) {
                if (registrosAfetados == 0) {
                    liveDataMensagem.postValue("vazio");
                }
                if (registrosAfetados > 0) {
                    liveDataMensagem.postValue("sucesso");
                }
            }

            @Override
            public void onError(String error) {
                liveDataMensagem.postValue(error);
            }
        });

    }

}// listarPokemonViewModel




