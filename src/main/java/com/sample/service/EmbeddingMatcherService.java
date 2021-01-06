package com.sample.service;

import com.sample.controller.EmbedResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class EmbeddingMatcherService {

    private static final String EMBED_URL = "embedUrl";
    private static final String EMBED_TEXTMATCH_URL = "embedTextUrl";
    private static final String EMBED_ICDMATCH_URL = "embedIcdUrl";
    public static final String MATCH_DISEASE_V2_URL = "http://localhost:5200/api/v1/matchDiseaseV2";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    private Set<String> allItems = getAllItems();
    public List<String> pageItems = getPageInformation();

    public EmbeddingMatcherService() throws IOException {
    }

    public List<EmbedResponse> closestDiseaseMatch(String query,  String splitBy, float cutOff) {

        query = query.replaceAll("[ ]+", " ").replaceAll("~~", " ");


        String replacedContent = (" " + query + " ".toLowerCase())
                // .replaceAll("\\d", " ")
                .replaceAll("[ ]+", " ");

        String embedUrl = env.getProperty(EMBED_URL);

        if (embedUrl == null) {
            embedUrl = MATCH_DISEASE_V2_URL;
        }

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("content", replacedContent.trim());
        paramMap.add("match", String.join(",", allItems));
        paramMap.add("splitBy", splitBy);


        //System.out.println("Embed url : " + embedUrl);
        EmbedResponse[] embedResponses = restTemplate.postForObject(embedUrl, paramMap, EmbedResponse[].class);
        Set<EmbedResponse> highestEmbedMatch = new HashSet<>();

        for (EmbedResponse embedResponse : embedResponses) {
            if (embedResponse.getScore() > cutOff) {
                highestEmbedMatch.add(embedResponse);
            }
        }

        // pick the best match for the "content text", not all matches,
        // this also fix the big where "problem med ögon" is being matched to "breathing problems"
        List<EmbedResponse> repsonses = new ArrayList<>(highestEmbedMatch);
        Collections.sort(repsonses);

        //
        for (EmbedResponse embedResponse : embedResponses) {
            String word = embedResponse.getWord();
            for (String wordpage : pageItems){
                if(word.equalsIgnoreCase(wordpage)){
                    embedResponse.setPage(true);
                }
            }
        }
        return repsonses;
    }





    private Set<String> getAllItems() {
        try {
            List<String> allItems = FileUtils.readLines(new File("/home/namar/IdeaProjects/LAS/src/main/resources/items"), "UTF-8");

            List<String> filteredElem = new ArrayList<>();
            for (String elem : allItems) {
                String[] synonyms = elem.split(",");
                filteredElem.addAll(Arrays.asList(synonyms));
            }

            return new HashSet<>(filteredElem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
    public Set<String> getItems(){
        return this.allItems;
    }


    private List<String> getPageInformation() throws IOException {
        List<String> informationOfProduct = FileUtils.readLines(new File("/home/namar/IdeaProjects/LAS/src/main/resources/page"), "UTF-8");
        return informationOfProduct;
    }

    public List<String> pageItems() {
        return this.pageItems;

    }
}
