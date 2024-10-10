/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.curso.filmzone;

import com.curso.filmzone.dominio.Filme;
import com.curso.filmzone.dominio.FilmeApiTmdb;
import com.curso.filmzone.dominio.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author admin
 */
public class FilmZone {

    public static void main(String[] args) throws SQLException {

        Connection conexao;
        try {
            conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432"
                            +"/film_zone?user=postgres&password=74115987");
                
         com.curso.filmzone.negocio.Filme filmeNeg = new com.curso.filmzone.negocio.Filme(conexao);
            System.err.println("Conexão realizada com sucesso");
         filmeNeg.inserir(buscarFilmesApi());
         //filmeNeg.obterFilmeId(1);        
         
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

    }
    
    public static List<Filme> buscarFilmesApi(){
        Scanner scanner = new Scanner(System.in);
        System.err.println("Digite a pagina: ");
        String param = scanner.nextLine();
        
        HttpClient cliente = HttpClient.newHttpClient();
            String baseUrl = "https://api.themoviedb.org/3/movie/popular?language=pt-BR&page=";
            String url = baseUrl + param;
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3NDEzNjI1MTYyM2JkMzFlNzA4NzQ4N"
                    + "zUwMWY1NWQzMiIsIm5iZiI6MTcyMzQ4MjI3NS4xNzAzMzUsInN1YiI6IjY2N2I2YmQwM"
                    + "2I1MDk1YzY3NzdkMjc0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ."
                    + "fFv1b-aXqcWjOUQ9opGgjj8sjRTSNMLJbCz0Kad36PU";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .build();
        
        try {
            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            
            Gson gson = new Gson();
            FilmeApiTmdb filmeApi = gson.fromJson(json, FilmeApiTmdb.class);
            List<Filme> listaFilmes = new ArrayList<>();
            
            for(Movie movieApi : filmeApi.getResults()){
                Filme filme = new Filme();
                filme.setAdulto(movieApi.isAdult());
                filme.setIdsGenero(movieApi.getGenre_ids());
                filme.setId(movieApi.getId());
                filme.setIdiomaOriginal(movieApi.getOriginal_language());
                filme.setSinopse(movieApi.getOverview());
                filme.setUrlCapa("https://image.tmdb.org/t/p/w300/" + movieApi.getPoster_path());
                filme.setDataLancamento(movieApi.getRelease_date());
                filme.setTitulo(movieApi.getTitle());
                filme.setRating(movieApi.getVote_average());
                
                listaFilmes.add(filme);
            }
                 
            return listaFilmes;
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return null;
        }        
    }
    
}
