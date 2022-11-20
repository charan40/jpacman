package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is a test class for MapParser.
 */
@ExtendWith(MockitoExtension.class)
public class MapParserTest {
    @Mock
    private BoardFactory boardFactory;
    @Mock
    private LevelFactory levelFactory;
    @Mock
    private Blinky blinky;
    /**
     * Test for the parseMap method (good map).
     */
    @Test
    public void testParseMapGood() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(boardFactory);
        assertNotNull(levelFactory);
        Mockito.when(levelFactory.createGhost()).thenReturn(blinky);
        MapParser mapParser = new MapParser(levelFactory, boardFactory);
        ArrayList<String> map = new ArrayList<>();
        map.add("############");
        map.add("#P        G#");
        map.add("############");
        mapParser.parseMap(map);
        Mockito.verify(levelFactory, Mockito.times(1)).createGhost();


        int countWall = 0;
        int countGround = 0;
        String groundCharacter = " .PG";
        for (String i : map) {
            for (char j : i.toCharArray()) {
                if (j == '#') {
                    countWall = countWall + 1;
                }
                else if (groundCharacter.contains(String.valueOf(j))) {
                    countGround++;
                }
            }
        }
        Mockito.verify(boardFactory, Mockito.times(countGround)).createGround();
        Mockito.verify(boardFactory, Mockito.times(countWall)).createWall();
    }
    /**
     * Test for the parseMap method (bad map).
     */
    @Test
    public void testParseMapWrong1() {
        Exception thrown =
            Assertions.assertThrows(PacmanConfigurationException.class, () -> {
                MockitoAnnotations.initMocks(this);
                assertNotNull(boardFactory);
                assertNotNull(levelFactory);
                MapParser mapParser = new MapParser(levelFactory, boardFactory);
                ArrayList<String> map = new ArrayList<>();
                map.add("#############");
                map.add("#P        G#");
                map.add("############");
                mapParser.parseMap(map);
            });
        Assertions.assertEquals("Input text lines are not of equal width.", thrown.getMessage());
    }


}
