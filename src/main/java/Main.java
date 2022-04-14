import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static final String targetUrl = "https://api.nasa.gov/planetary/apod?api_key="; // first part - url for request

    public static final String key = "u57uk4obaCv5B2Fz5wDByVUqq9ur7tyTvpCRYaqT"; // second part - key

    public static final String targetUrlWithKey = targetUrl + key; // full url = url for request + key

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // maximum server connection timeout
                        .setSocketTimeout(30000)    // maximum waiting time for receiving data
                        .setRedirectsEnabled(false) // the ability to follow a redirect in the response
                        .build())
                .build();

        HttpGet request = new HttpGet(targetUrlWithKey); // run request
        CloseableHttpResponse response = httpClient.execute(request); // response from server

        NasaResponse nasaResponse = mapper.readValue(response.getEntity().getContent(),
                new TypeReference<>() {
                }); // make object 'nasaResponse' of class 'NasaResponse' from body of server response

        String imageUrl = nasaResponse.getUrl(); // get full image url from 'nasaResponse' object

        String[] imageUrlParts = imageUrl.split("/"); // split full image url for get fileName

        String fileName = imageUrlParts[imageUrlParts.length - 1]; // last part of imageUrlParts array

        File file = new File(fileName); // create File object
        URL url = new URL(imageUrl); // create URL object

        FileUtils.copyURLToFile(url, file); // copy from URL object to File object
    }
}
