import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CatGallery {
    private CloseableHttpClient httpClient;
    private final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    private static List<Post> posts;
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CatGallery catGallery = new CatGallery();
        catGallery.createHttpClient();
        posts = catGallery.getPosts();
        catGallery.showPosts();
    }

    private List<Post> getSortedPosts() {
        return posts.stream()
                .filter(value -> !Integer.valueOf(value.getUpvotes()).equals(null) && value.getUpvotes() > 0)
                .collect(Collectors.toList());
    }

    public void showPosts() {
        getSortedPosts().stream().forEach(System.out::println);
    }

    private void createHttpClient() {
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

    private List<Post> getPosts() throws IOException {
        HttpGet request = new HttpGet(URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        return mapper.readValue(
                httpClient.execute(request).getEntity().getContent(),
                new TypeReference<>() {
                }
        );
    }

}