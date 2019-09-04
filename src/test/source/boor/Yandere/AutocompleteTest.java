/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package test.source.boor.Yandere;

import source.interfaces.Autocomplete;
import org.junit.Test;
import source.boor.Yandere;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AutocompleteTest {

    private Autocomplete autocomplete = Yandere.get();

    @Test
    public void getAutocompleteSearchRequestTest() {
        assertEquals(
                "https://yande.re/tag.json?order=count&name=tou*&limit=10"
                , autocomplete.getAutocompleteSearchRequest("tou")
        );
    }

    @Test
    public void getAutocompleteResultTest() throws Exception {
        String term = "tou";
        String[] result = autocomplete.getAutocompleteVariations(term);
        assertNotNull(result);
    }

}
