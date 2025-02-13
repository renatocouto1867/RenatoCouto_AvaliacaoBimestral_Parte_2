package com.example.renatocouto_avaliacaobimestral_parte_2.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.renatocouto_avaliacaobimestral_parte_2.ClienteHttp.Conexao;
import com.example.renatocouto_avaliacaobimestral_parte_2.R;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Pokemons;
import com.example.renatocouto_avaliacaobimestral_parte_2.entity.Result;
import com.example.renatocouto_avaliacaobimestral_parte_2.util.Auxilia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DadosRepository {

    private static final String TAG = "DadosRepository";
    private static final String URL = "https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0";
    private final ResultDao resultDao;
    private final ExecutorService executorService;
    private final Context context;
    private Pokemons dadosBaixados;

    public DadosRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        resultDao = database.resultDao();
        executorService = Executors.newSingleThreadExecutor();
        context = application.getApplicationContext();
    }

    /**
     * Para Executar a requisição da API, deve ser executado em uma thread separada
     */
    private Pokemons apiObterDados() {

        if (dadosBaixados != null) {
            return dadosBaixados;
        }
        Conexao conexao = new Conexao();
        InputStream inputStream = conexao.obterRespostaHTTP(URL);
        Auxilia auxilia = new Auxilia();
        String textoJSON = auxilia.converter(inputStream);
        Log.i(TAG, "JSON recebido: " + textoJSON);

        Gson gson = new Gson();

        if (textoJSON != null) {
            Type type = new TypeToken<Pokemons>() {
            }.getType();
            dadosBaixados = gson.fromJson(textoJSON, type);
            atualizaId();
            return dadosBaixados;

        } else {
            return null;
        }//if else
    }

//    /**
//     * Método público para obter todos os pokémons da API
//     */
//    private Pokemons obterPokemons() {
//        Pokemons dados = apiObterDados();
//        return dados;
//    }

    /**
     * Método público para obter apenas os 50 primeiros Pokémons
     */
    public void obter50Pokemons(OnBaixarListener listener) {
        executorService.execute(() -> {
            try {
                Pokemons dados = apiObterDados();

                if (dados != null && dados.getResults() != null) {
                    List<Result> todosResultados = dados.getResults();
                    // função Math.min retorna o menor valor passado entre os dois parametros,
                    // nesse caso, 50 e o tamanho da lista, isso ajuda a evitar erro.
                    List<Result> lista50 = todosResultados.subList(0, Math.min(50, todosResultados.size()));
                    Log.i(TAG, "Lista de Pokémons obtida: " + lista50.size());

                    listener.sucesso(new ArrayList<>(lista50));

                } else {
                    listener.erro(context.getString(R.string.n_o_foi_poss_vel_obter_dados_dos_pok_mons));
                }
            } catch (Exception e) {
                listener.erro(context.getString(R.string.erro_ao_baixar_pok_mons) + e.getMessage());
            }
        });
    }

    /**
     * Atualiza os IDs dos pokémons
     */
    private void atualizaId() {
        if (dadosBaixados == null || dadosBaixados.getResults() == null) {
            Log.e(TAG, "Não há dados para atualizar IDs.");
            return;
        }

        for (Result result : dadosBaixados.getResults()) {
            result.setId(getIdPokemon(result.getUrl()));
        }
    }

    /**
     * Extrai o ID do Pokémon a partir da URL
     */
    private int getIdPokemon(String url) {
        if (url == null || url.isEmpty()) {
            return -1;
        }
        String[] parts = url.split("/");
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Insere um Pokémon no banco de dados
     */
    public void bancoInsert(Result result, OnInsertListener listener) {
        executorService.execute(() -> {
            long registrosAfetados = resultDao.insert(result);
            if (registrosAfetados >= 0) {
                listener.onSuccess(registrosAfetados);
            } else {
                listener.onError("erro");
            }
        });
    }

    /**
     * Obtém todos os pokémons do banco de dados
     */
    public void bancoGetAllResults(OnBaixarListener listener) {
        executorService.execute(() -> {
            try {
                List<Result> results = resultDao.getAllResults();

                if (results != null) {
                    listener.sucesso(new ArrayList<>(results));
                } else {
                    listener.erro(context.getString(R.string.n_o_foi_poss_vel_obter_dados_dos_pok_mons));
                }
            } catch (Exception e) {
                listener.erro(context.getString(R.string.erro_ao_baixar_pok_mons) + e.getMessage());
            }
        });

    }
    public void bancoGetAleatroioResults(int qt, OnBaixarListener listener) {
        executorService.execute(() -> {
            try {
                List<Result> results = resultDao.getAleatorioResults(qt);

                if (results != null) {
                    listener.sucesso(new ArrayList<>(results));
                } else {
                    listener.erro(context.getString(R.string.n_o_foi_poss_vel_obter_dados_dos_pok_mons));
                }
            } catch (Exception e) {
                listener.erro(context.getString(R.string.erro_ao_baixar_pok_mons) + e.getMessage());
            }
        });

    }

    /**
     * Deleta todos os registros do banco de dados
     */
    public void bancoDeleteAll(OnDeleteAllListener listener) {
        executorService.execute(() -> {
            int registrosAfetados = resultDao.deleteAll();
            if (registrosAfetados >= 0) {
                listener.onSuccess(registrosAfetados);
            } else {
                listener.onError("erro");
            }
        });
    }


    /**
     * Salva uma lista de pokémons no banco de dados
     */
    public void bancoSalvarListaPokemons(List<Result> pokemons, OnSalvarListaListener listener) {

        if (pokemons == null || pokemons.isEmpty()) {
            listener.erro("lista vazia");
        } else {
            executorService.execute(() -> {
                try {
                    long[] ids = resultDao.insertAll(pokemons);
                    listener.sucesso(ids);

                } catch (Exception e) {
                    listener.erro(context.getString(R.string.erro_ao_baixar_pok_mons) + e.getMessage());
                }
            });
        }
    }

    // Interfaces para fazer callback.
    // Achei melhor fazer com callback, mas vi que da para usar o LiviData
    // fiquei um pouco perdido com tanto LiviData, mas depois vi que era mais facil.

    public interface OnBaixarListener {
        void sucesso(List<Result> results);

        void erro(String erro);
    }

    public interface OnDeleteAllListener {
        void onSuccess(int registrosAfetados);

        void onError(String error);
    }

    public interface OnInsertListener {
        void onSuccess(long registrosAfetados);

        void onError(String error);
    }

    public interface OnSalvarListaListener {
        void sucesso(long[] ids);

        void erro(String erro);
    }

}
