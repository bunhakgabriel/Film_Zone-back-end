/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.curso.filmzone.negocio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Filme {
    
    private Connection conexao;
    
    public Filme(Connection conexao){
        if(conexao != null){
            this.conexao = conexao;    
        }
    }
    
    public com.curso.filmzone.dominio.Filme obterFilmeId(int id) throws SQLException {
        try {
        com.curso.filmzone.persistencia.Filme filmePersist = new com.curso.filmzone.persistencia.Filme(this.conexao);
        return filmePersist.obterFilmeId(id);
    } catch (SQLException e) {
        // Aqui você pode registrar o erro, notificar o usuário, etc.
        System.err.println("Erro ao obter filme com ID " + id + ": " + e.getMessage());
        // Propagar a exceção ou lidar com ela conforme necessário
        // throw e; // Opcional: se você quiser que o erro seja propagado
    }
    return null;
    }
    
    public List<com.curso.filmzone.dominio.Filme> obterLista() throws SQLException{
        com.curso.filmzone.persistencia.Filme filmePersist = new com.curso.filmzone.persistencia.Filme(this.conexao);
        List<com.curso.filmzone.dominio.Filme> listaFilmes = filmePersist.obterLista();
        
        return listaFilmes;
    }
    
    public void inserir(List<com.curso.filmzone.dominio.Filme> listaFilme) throws SQLException{
        if(listaFilme.isEmpty() && listaFilme != null) return;
        
        com.curso.filmzone.persistencia.Filme filmePersist = new com.curso.filmzone.persistencia.Filme(this.conexao);
        filmePersist.inserir(listaFilme);
        
    }
    
}
