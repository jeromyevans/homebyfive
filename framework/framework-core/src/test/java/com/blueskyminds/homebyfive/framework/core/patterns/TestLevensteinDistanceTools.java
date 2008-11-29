package com.blueskyminds.homebyfive.framework.core.patterns;

import junit.framework.TestCase;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

import com.blueskyminds.homebyfive.framework.core.alias.Aliased;
import com.blueskyminds.homebyfive.framework.core.tools.Named;

/**
 * Date Started: 21/03/2008
 * <p/>
 * History:
 */
public class TestLevensteinDistanceTools extends TestCase {

    public void testMatch() {
        String[] suburbs = {
                "Aberdeen",
                "Woerden",
                "Verden",
                "Alberton",
                "Oberon",
                "Aberdare"

        };

        List<String> matches = LevensteinDistanceTools.match("Aberdeen", suburbs);
        assertEquals(1, matches.size());
        assertEquals("Aberdeen", matches.get(0));

        List<String> matches2 = LevensteinDistanceTools.match("Aberd", suburbs);
        assertEquals(2, matches2.size());
    }

    private static class Entity implements Named, Aliased {
        private String name;
        private List<String> aliases;

        private Entity(String name) {
            this.name = name;
            aliases =new LinkedList<String>();
        }
        public Entity addName(String name) {
            aliases.add(name);
            return this;
        }

        public String getName() {
            return name;
        }

        public String[] getAliases() {
            return (String[]) aliases.toArray();
        }

        public String[] getNames() {
            String[] values = new String[aliases.size()+1];
            values[0] = name;
            int i = 1;
            for (String alias : aliases) {
                values[i] = alias;
                i+=1;
            }
            return values;
        }
    }

    public void testAliases() {
        Set<Entity> entities = new HashSet<Entity>();

        entities.add(new Entity("New South Wales").addName("NSW"));
        entities.add(new Entity("Queensland").addName("QLD"));
        entities.add(new Entity("Northern Territory").addName("NT"));
        entities.add(new Entity("Western Australia").addName("WA"));
        entities.add(new Entity("South Australia").addName("SA"));
        entities.add(new Entity("Victoria").addName("VIC"));
        entities.add(new Entity("Tasmania").addName("TAS"));
        entities.add(new Entity("Australian Capital Territory").addName("ACT"));

        List<Entity> matches = LevensteinDistanceTools.matchName("WA", entities);
        assertEquals(1, matches.size());
    }
}
