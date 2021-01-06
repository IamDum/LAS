package com.sample.controller;

import com.sample.service.EmbeddingMatcherService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin(origins = "*")
public class SearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
    private static final float CUT_OFF = 0.62f;

    @Autowired
    private EmbeddingMatcherService embeddingMatcherService;

    public Map<String, String> itemImageMap = getImageMap();
    public SearchController() throws IOException {
    }

    @GetMapping(value = "/process")
    @CrossOrigin
    public List<EmbedResponse> recommend(@RequestParam String query) {
        LOGGER.info("Incmoming request for assistant");
        try {
            List<EmbedResponse> embedResponses = embeddingMatcherService.closestDiseaseMatch(query, "~", CUT_OFF);
            if (embedResponses.isEmpty()) {
                return null;
            }
            //
            for (EmbedResponse embedResponse : embedResponses) {
                String url = itemImageMap.get(embedResponse.word);
                embedResponse.setUrl(url);
            }

            return embedResponses;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Internal Error", e);
            throw new RuntimeException("Server internal error");
        }
    }

    private Map<String, String> getImageMap() {
        try {
            List<String> allItemsImage = FileUtils.readLines(new File("/home/namar/IdeaProjects/LAS/src/main/resources/items-url"), "UTF-8");
            Map<String, String> itemImageMap = new HashMap<>();
            for (String item : allItemsImage) {
                String[] split = item.split("=");
                itemImageMap.put(split[0], split[1]);
                String[] synonyms = split[0].split(",");
                for (String syn : synonyms) {
                    itemImageMap.put(syn, split[1]);
                    itemImageMap.put(syn.toLowerCase(), split[1]);
                }
                itemImageMap.put(split[0].toLowerCase(), split[1]);
            }
            return itemImageMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
    @RequestMapping(value = "/findallpages")
    private List<String> allPage() {
        return embeddingMatcherService.pageItems();

    }

    @RequestMapping(value = "/findallItems")
    private Set<String> allItems() {
        return embeddingMatcherService.getItems();
    }
}

