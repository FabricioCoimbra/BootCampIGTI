package com.Fabricio;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

class PostUser {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;

    public Integer getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

@Getter
@Setter
class TokenRetorno{
    private String token;
    public String getToken(){ return token;}
    public void setToken(final String token){ this.token = token; }
}

public class App {
    private static Gson gson;
    public static void main( final String[] args) {
        gson = new GsonBuilder().create();
        System.out.println();
        System.out.println("Carregando Lista de filmes do herokuapp");
        System.out.println();
        System.out.println(get("https://igti-film.herokuapp.com/api/films", autenticar()));

        System.out.println();        
        System.out.println("Carregando Posts do arquivo");           
        System.out.println();       
        ArrayList<PostUser> posts = carregarPostsDoArquivo();
        System.out.println(gson.toJson(posts));

        System.out.println();        
        System.out.println("Filtrando Posts com titulo dolorem");           
        System.out.println();  
        posts = filtrarTitle("dolorem", posts);
        for(PostUser post: posts){
            System.out.println(gson.toJson(post));
            System.out.println();  
        }
        
        
    }

    private static String autenticar() {
        final Type tokenType = new TypeToken<TokenRetorno>() {}.getType();
        final String inputJson = "{ \"username\":\"fabricio\", \"password\":\"dificil\" }";
        post("https://igti-film.herokuapp.com/api/signup", inputJson);
        final String token = post("https://igti-film.herokuapp.com/api/signin", inputJson);
        final TokenRetorno retorno = gson.fromJson(token, tokenType);
        return retorno.getToken();
    }

    private static String post(final String url, final String BodyRequest) {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(BodyRequest))
            .build();
        final HttpResponse<String> response;
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (final Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
        return response.body();
    }

    private static String get(final String url, final String tokenBearer) {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + tokenBearer)
            .build();
        final HttpResponse<String> response;
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (final Exception e) {
            System.out.println(e);
            return e.getMessage();
        }        
        return response.body();
    }

    private static ArrayList<PostUser> carregarPostsDoArquivo(){
        final Type postUserType = new TypeToken<ArrayList<PostUser>>() {}.getType(); 
        try {            
            //final String PostsJson = FileUtils.readFileToString(new File("D:/Curso Mobile IGTI/PostsUsers.json"));                       
            final String PostsJson = FileUtils.readFileToString(new File("PostsUsers.json"), Charset.defaultCharset());                       
            return gson.fromJson(PostsJson, postUserType);              
        } catch(final IOException e){
            System.out.println(e); 
            return new ArrayList<>();
        }        
    }

    private static ArrayList<PostUser> filtrarTitle(String filtro, ArrayList<PostUser> posts){
        // essa função aparentemente inocente duplica 2x o array, e faz um for para filtrar, além é clarro da dupla conversão de tipos...
        return (ArrayList<PostUser>) posts.stream()
            .filter(post -> post.getTitle().contains(filtro))
            .collect(Collectors.toList());
    }
}
