package service;


import com.sample.Application;
import com.sample.controller.EmbedResponse;
import com.sample.service.EmbeddingMatcherService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EmbeddingMatcherServiceTest {

    @Autowired
    private EmbeddingMatcherService embeddingMatcherService;


    @Test
    public void testpillowrelateditems() {
        String pillow = "तकिया";
        List<EmbedResponse> result = embeddingMatcherService.closestDiseaseMatch(pillow, "~", 0.62f);
        boolean found = false;
        for (int i = 0; i < result.size(); i++) {
            EmbedResponse responseToCheck = result.get(i);
            String itemName = responseToCheck.getWord();
            if (itemName.equals("pillow")) {
                found = true;
            }
        }
        Assert.assertThat(found, Matchers.is(true));
    }

    @Test
    public void testrelateditems() {
        String pillow = "aroplane";
        List<EmbedResponse> result = embeddingMatcherService.closestDiseaseMatch(pillow, "~", 0.62f);
        boolean found = false;
        for (int i = 0; i < result.size(); i++) {
            EmbedResponse responseToCheck = result.get(i);
            String itemName = responseToCheck.getWord();
            if (itemName.equals("pillow")) {
                found = true;
            }
        }
        Assert.assertThat(found, Matchers.is(false));
    }

    @Test
    public void testallitems() {

        Set<String> result = embeddingMatcherService.getItems();

        int size = result.size();
        Assert.assertThat(size, Matchers.is(60));
    }

    @Test
    public void testpillow() {
        Set<String> result = embeddingMatcherService.getItems();

        boolean found = false;

        for (String item : result) {
            if (item.equalsIgnoreCase("pillow")) {
                found = true;
            }
        }
        Assert.assertThat(found, Matchers.is(true));
    }
}


