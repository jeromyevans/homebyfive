package com.blueskyminds.homebyfive.business.address;

import junit.framework.TestCase;
import com.blueskyminds.homebyfive.business.address.AddressPathParser;

import java.util.Map;
import java.util.List;
import java.util.Arrays;

/**
 * Date Started: 20/05/2008
 */
public class TestAddressPathParser extends TestCase {

    public void testEdit1() {

        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/edit", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("edit", params.get("method"));
        assertEquals("suburb", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}", params.get("namespace"));
    }

    public void testEdit2() {

        Map<String, String> params = AddressPathParser.process("/au/edit", null);
        assertEquals("au", params.get("country"));
        assertEquals(null, params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals(null, params.get("suburb"));
        assertEquals("edit", params.get("method"));
        assertEquals("country", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}", params.get("namespace"));
    }

    public void testEdit3() {

        Map<String, String> params = AddressPathParser.process("/au/nsw/edit", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals(null, params.get("suburb"));
        assertEquals("edit", params.get("method"));
        assertEquals("state", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}", params.get("namespace"));
    }

    public void testProperty1() {

        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("property", params.get("action"));
        assertEquals(null, params.get("method"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}", params.get("namespace"));
    }

    public void testPath2() {
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("property", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}", params.get("namespace"));
    }

    public void testPath3() {
        Map<String, String> params = AddressPathParser.process("au/nsw/neutral+bay/spruson+street/22", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("property", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}", params.get("namespace"));
    }

    public void testPath4() {
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("property", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}", params.get("namespace"));
    }

    public void testPath5() {
        Map<String, String> params = AddressPathParser.process("/au/nsw/2089", null);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals("2089", params.get("postCode"));
        assertEquals(null, params.get("suburb"));
        assertEquals(null, params.get("street"));
        assertEquals(null, params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("postcode", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{postCode}", params.get("namespace"));
    }

    private List<String> buildActionList(String... actions) {
        return Arrays.asList(actions);
    }

    public void testAction1() {
        List<String> actions = buildActionList("events");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1/events", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("events", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}", params.get("namespace"));
    }

    public void testAction2() {
        List<String> actions = buildActionList("properties");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/properties", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals(null, params.get("street"));
        assertEquals(null, params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("properties", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}", params.get("namespace"));
    }

    public void testAction3() {
        List<String> actions = buildActionList("suburbs", "properties");
        Map<String, String> params = AddressPathParser.process("/au/nsw/2089/suburbs", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals("2089", params.get("postCode"));
        assertEquals(null, params.get("suburb"));
        assertEquals(null, params.get("street"));
        assertEquals(null, params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("suburbs", params.get("action"));
        assertEquals(null, params.get("extension"));

        assertEquals("/{country}/{state}/{postCode}", params.get("namespace"));
    }

    public void testAction3again() {
       List<String> actions = buildActionList("states", "properties");
       Map<String, String> params = AddressPathParser.process("/au/states", actions);
       assertEquals("au", params.get("country"));
       assertEquals(null, params.get("state"));
       assertEquals(null, params.get("postCode"));
       assertEquals(null, params.get("suburb"));
       assertEquals(null, params.get("street"));
       assertEquals(null, params.get("streetNo"));
       assertEquals(null, params.get("unitNo"));
       assertEquals("states", params.get("action"));
       assertEquals(null, params.get("extension"));

       assertEquals("/{country}", params.get("namespace"));
   }


    public void testAction4() {
        List<String> actions = buildActionList("suburbs", "properties", "events");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1/info/events", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("events", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}/info", params.get("namespace"));
    }

    public void testAction5() {
        List<String> actions = buildActionList("suburbs", "properties", "events");
        Map<String, String> params = AddressPathParser.process("/services/authenticate", actions);
        assertNull(params);
    }

    public void testAction6() {
        List<String> actions = buildActionList("suburbs", "properties", "events");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1/info/events.json", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("events", params.get("action"));
        assertEquals("json", params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}/info", params.get("namespace"));
    }

    public void testAction7() {
        List<String> actions = buildActionList("suburbs", "properties", "events");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1/info/events.json?a=b", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("events", params.get("action"));
        assertEquals("json", params.get("extension"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}/info", params.get("namespace"));
    }

    public void testAction8() {
        List<String> actions = buildActionList("suburbs", "properties", "events");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1/info/events/21.json", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("events/21", params.get("action"));
        assertEquals("json", params.get("extension"));
//        assertEquals("21", params.get("id"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}/info", params.get("namespace"));
    }

    public void testAction9() {
        List<String> actions = buildActionList("suburbs", "properties", "events");
        Map<String, String> params = AddressPathParser.process("/au/nsw/neutral+bay/spruson+street/22/1/info/events/21/edit.json", actions);
        assertEquals("au", params.get("country"));
        assertEquals("nsw", params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals("neutral+bay", params.get("suburb"));
        assertEquals("spruson+street", params.get("street"));
        assertEquals("22", params.get("streetNo"));
        assertEquals("1", params.get("unitNo"));
        assertEquals("events/21", params.get("action"));
        assertEquals("edit", params.get("method"));
        assertEquals("json", params.get("extension"));
//        assertEquals("21", params.get("id"));
        assertEquals("/{country}/{state}/{suburb}/{street}/{streetNo}/{unitNo}/info", params.get("namespace"));
    }

    public void testPath10() {
        Map<String, String> params = AddressPathParser.process("/au", null);
        assertEquals("au", params.get("country"));
        assertEquals(null, params.get("state"));
        assertEquals(null, params.get("postCode"));
        assertEquals(null, params.get("suburb"));
        assertEquals(null, params.get("street"));
        assertEquals(null, params.get("streetNo"));
        assertEquals(null, params.get("unitNo"));
        assertEquals("country", params.get("action"));
        assertEquals(null, params.get("extension"));
        assertEquals("/{country}", params.get("namespace"));
    }
}
