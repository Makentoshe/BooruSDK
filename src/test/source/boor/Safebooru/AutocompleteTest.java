/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package test.source.boor.Safebooru;

import source.interfaces.Autocomplete;
import org.junit.Test;
import source.boor.Safebooru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AutocompleteTest {

    private Autocomplete autocomplete = Safebooru.get();

    @Test
    public void getAutocompleteSearchRequest(){
        assertEquals(
                "https://safebooru.org/index.php?page=tags&s=list&tags=sas*&sort=desc&order_by=index_count",
                autocomplete.getAutocompleteSearchRequest("sas")
        );
    }

    @Test
    public void getAutocompleteResultTest() throws Exception {
        String term = "tou";
        String[] result = autocomplete.getAutocompleteVariations(term);
        assertNotNull(result);
    }

    @Test
    public void getAutocompleteResultWhenTermIsSmall() throws Exception {
        String term = "t";
        String[] result = autocomplete.getAutocompleteVariations(term);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

}
