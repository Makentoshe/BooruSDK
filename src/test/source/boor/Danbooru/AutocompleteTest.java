/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package test.source.boor.Danbooru;

import source.interfaces.Autocomplete;
import org.junit.Test;
import source.boor.Danbooru;

import static org.junit.Assert.*;

public class AutocompleteTest {

    private Autocomplete autocomplete = Danbooru.get();

    @Test
    public void getAutocompleteSearchRequest(){
        assertEquals(
                "https://danbooru.donmai.us/tags/autocomplete.json?search%5Bname_matches%5D=sas",
                autocomplete.getAutocompleteSearchRequest("sas")
        );
    }

    @Test
    public void getAutocompleteResultTest() throws Exception {
        String term = "tou";
        String[] result = autocomplete.getAutocompleteVariations(term);
        assertNotNull(result);
    }

}
