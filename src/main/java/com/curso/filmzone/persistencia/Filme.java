/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.curso.filmzone.persistencia;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author admin
 */
public class Filme {

    private Connection conexao;

    public Filme(Connection conexao) {
        if (conexao != null) {
            this.conexao = conexao;
        }
    }

    public com.curso.filmzone.dominio.Filme obterFilmeId(int id) throws SQLException {
        com.curso.filmzone.dominio.Filme filme = new com.curso.filmzone.dominio.Filme();
        String textSql = "select * from public.filmes where id = ?";
        PreparedStatement env = null;
        ResultSet resultado = null;

        try {
            env = conexao.prepareStatement(textSql);
            env.setInt(1, id);
            resultado = env.executeQuery();

            if (resultado.next()) {
                filme.setId(resultado.getInt("id"));
                filme.setTitulo(resultado.getString("titulo"));
                filme.setDataLancamento(resultado.getDate("data_lancamento"));
                filme.setRating(resultado.getDouble("rating"));
                filme.setSinopse(resultado.getString("sinopse"));
                filme.setDuracaoMin(resultado.getInt("duracao_min"));
                filme.setClassificacao(resultado.getInt("classificacao"));
                filme.setDiretor(resultado.getString("diretor"));
                filme.setIdiomaOriginal(resultado.getString("idioma"));
                filme.setPaisOrigem(resultado.getString("pais_origem"));
                filme.setUrlCapa(resultado.getString("url_capa"));
                filme.setUrlTrailer(resultado.getString("url_trailer"));
                filme.setUrlFilme(resultado.getString("url_filme"));
                filme.setVisualizacoes(resultado.getInt("visualisacoes"));
                //filme.setIsDisponivel(resultado.getBoolean("disponivel"));
            String[] generosArray = (String[]) resultado.getArray("ids_genero").getArray();
            filme.setIdsGenero(Arrays.asList(generosArray).stream().map(value -> Integer.valueOf(value)).collect(Collectors.toList()));
                return filme;
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao recuperar o filme com ID " + id, e);
        } finally {
            // Fecha o ResultSet e o PreparedStatement no bloco finally
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException e) {
                    throw new SQLException("Não foi possível fechar o ResultSet", e);
                }
            }
            if (env != null) {
                try {
                    env.close();
                } catch (SQLException e) {
                    throw new SQLException("Não foi possível fechar o PreparedStatement", e);
                }
            }
        }
        return null;
    }

    public List<com.curso.filmzone.dominio.Filme> obterLista() throws SQLException {
        List<com.curso.filmzone.dominio.Filme> listaFilmes = new ArrayList<>();
        String textSql = "select * from public.filmes";

        PreparedStatement getLista = conexao.prepareStatement(textSql);
        ResultSet resultado = getLista.executeQuery();

        while (resultado.next()) {
            com.curso.filmzone.dominio.Filme filme = new com.curso.filmzone.dominio.Filme();
            filme.setId(resultado.getInt("id"));
            filme.setTitulo(resultado.getString("titulo"));
            filme.setDataLancamento(resultado.getDate("data_lancamento"));
            filme.setRating(resultado.getDouble("rating"));
            filme.setSinopse(resultado.getString("sinopse"));
            filme.setDuracaoMin(resultado.getInt("duracao_min"));
            filme.setClassificacao(resultado.getInt("classificacao"));
            filme.setDiretor(resultado.getString("diretor"));
            filme.setIdiomaOriginal(resultado.getString("idioma"));
            filme.setPaisOrigem(resultado.getString("pais_origem"));
            filme.setUrlCapa(resultado.getString("url_capa"));
            filme.setUrlTrailer(resultado.getString("url_trailer"));
            filme.setUrlFilme(resultado.getString("url_filme"));
            filme.setVisualizacoes(resultado.getInt("visualisacoes"));
            //filme.setIsDisponivel(resultado.getBoolean("disponivel"));
            String[] generosArray = (String[]) resultado.getArray("ids_genero").getArray();
            filme.setIdsGenero(Arrays.asList(generosArray).stream().map(value -> Integer.valueOf(value)).collect(Collectors.toList()));
            listaFilmes.add(filme);
        }

        return listaFilmes;
    }

    public void inserir(List<com.curso.filmzone.dominio.Filme> listaFilmes) throws SQLException {
        String insert_sql = "insert into public.filmes"
                + " (id, titulo, adulto, ids_genero,"
                + " idioma, sinopse, data_lancamento,"
                + " contagem_avaliacoes, url_capa,"
                + " visualisacoes, disponivel, duracao_min,"
                + " rating)"
                + " values"
                + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement env = null;

        try {
            env = this.conexao.prepareStatement(insert_sql);

            for (com.curso.filmzone.dominio.Filme filme : listaFilmes) {
                env.setInt(1, filme.getId());
                env.setString(2, filme.getTitulo());
                env.setBoolean(3, filme.isAdulto());
                Array sqlArray = this.conexao.createArrayOf("TEXT", filme.getIdsGenero().toArray());
                env.setArray(4, sqlArray);
                env.setString(5, filme.getIdiomaOriginal());
                env.setString(6, filme.getSinopse());
                //O método setDate espera um objeto java.sql.Date
                env.setDate(7, new java.sql.Date(filme.getDataLancamento().getTime()));
                env.setInt(8, filme.getContagemAvaliacoes());
                env.setString(9, filme.getUrlCapa());
                env.setInt(10, filme.getVisualizacoes());
                env.setBoolean(11, filme.isIsDisponivel());
                env.setInt(12, filme.getDuracaoMin());
                env.setDouble(13, filme.getRating());

                env.executeUpdate();
            }
        } finally {
            if (env != null) {
                env.close();
            }
            if (this.conexao != null) {
                this.conexao.close();
            }
        }

    }

}
