import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Olá mundo!");

        String inputJson = "{ \"username\":\"fabricio\", \"password\":\"dificil\" }";
        //System.out.println(Post("https://igti-film.herokuapp.com/api/signup", inputJson));
        String token = Post("https://igti-film.herokuapp.com/api/signin", inputJson);
        System.out.println(token);
        System.out.println(Get("https://igti-film.herokuapp.com/api/films", token));
    }

    private static String Post(String url, String BodyRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(BodyRequest))            
            .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return response.body();
    }

    private static String Get(String url, String tokenBearer) throws Exception  {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")        
            .header("Authorization", "Bearer " + tokenBearer)  
            .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return response.body();
    }
}
