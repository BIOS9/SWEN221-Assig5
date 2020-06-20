package tests;

//This program is copyright VUW.
//You are granted permission to use it to construct your answer to a SWEN221 assignment.
//You may not distribute it in any other way without permission.

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import game.CatanGame;
import model.Game;
import model.Location;
import model.Resource;
import model.ResourceCounter;
import model.Settlement;
import model.Tile;

public class Part2Tests {


 //-----------
	@Test
	void testSetResources1() {

		CatanGame game = new CatanGame(2);

		ArrayList<Resource> pool = new ArrayList<>();
		pool.add(Resource.WOOD);
		pool.add(Resource.DESERT);
		pool.add(Resource.SHEEP);
		pool.add(Resource.BRICK);
		pool.add(Resource.STONE);
		pool.add(Resource.WHEAT);
		pool.add(Resource.WOOD);

		game.setResources(pool);

		assertEquals("WO  DE  "
		         + "SH  BR  ST  "
				   + "WH  WO",
				game.toResourceString());
	}

	@Test
	void testSetResources2() {

		CatanGame game = new CatanGame(3);

		List<Resource> pool = new ArrayList<>();
		pool.add(Resource.WOOD);
		pool.add(Resource.DESERT);
		pool.add(Resource.WHEAT);
		pool.add(Resource.SHEEP);
		pool.add(Resource.WOOD);
		pool.add(Resource.SHEEP);
		pool.add(Resource.BRICK);
		pool.add(Resource.WOOD);
		pool.add(Resource.BRICK);
		pool.add(Resource.BRICK);
		pool.add(Resource.STONE);
		pool.add(Resource.STONE);
		pool.add(Resource.SHEEP);
		pool.add(Resource.STONE);
		pool.add(Resource.WHEAT);
		pool.add(Resource.WOOD);
		pool.add(Resource.WHEAT);
		pool.add(Resource.SHEEP);
		pool.add(Resource.WHEAT);

		game.setResources(pool);

		assertEquals("WO  DE  WH  "
		         + "SH  WO  SH  BR  "
			   + "WO  BR  BR  ST  ST  "
		         + "SH  ST  WH  WO  "
				    +"WH  SH  WH",
				game.toResourceString());
	}

	@Test
	void testSetResourceCounters1() {
		CatanGame game = new CatanGame(2);
		List<Resource> resourcePool = new ArrayList<>();
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.WHEAT);
		resourcePool.add(Resource.SHEEP);
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.WHEAT);
		resourcePool.add(Resource.WHEAT);
		List<ResourceCounter> counterPool = ResourceCounter.generateResourceCounters();
		game.setResources(resourcePool);
		game.setResourceCounters(counterPool);
		//Note, this is the numeric value of the resources in the counterPool.
        assertEquals("[5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11]",
            	counterPool.stream().map(r->r.getNumber()).collect(Collectors.toList()).toString());
        //They are placed by the clockwise stream on the hexagonal map
        //but the desert is skipped (no resource on the desert) and he get a resource number of 0.
		assertEquals("5   0   " //zero is the desert!
		         + "8   10  2   "
				   + "3   6",
				game.toResourceCounterString());
	}
	@Test
	void testSetResourceCounters2() {
		CatanGame game = new CatanGame(2);
		List<Resource> resourcePool = new ArrayList<>();
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT); //more deserts
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.WHEAT);
		resourcePool.add(Resource.WHEAT);
		List<ResourceCounter> counterPool = ResourceCounter.generateResourceCounters();
		game.setResources(resourcePool);
		game.setResourceCounters(counterPool);
		assertEquals("5   0   "
		          +"0   0   2   "
				    +"3   6",
				game.toResourceCounterString());
	}

	@Test
	void testSetResourceCounters3() {
		CatanGame game = new CatanGame(3);
		List<Resource> resourcePool = new ArrayList<>();
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.WHEAT);
		resourcePool.add(Resource.SHEEP);
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.SHEEP);
		resourcePool.add(Resource.BRICK);
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.BRICK);
		resourcePool.add(Resource.BRICK);
		resourcePool.add(Resource.STONE);
		resourcePool.add(Resource.STONE);
		resourcePool.add(Resource.SHEEP);
		resourcePool.add(Resource.STONE);
		resourcePool.add(Resource.WHEAT);
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.WHEAT);
		resourcePool.add(Resource.SHEEP);
		resourcePool.add(Resource.WHEAT);
		List<ResourceCounter> counterPool = ResourceCounter.generateResourceCounters();

		game.setResources(resourcePool);
		game.setResourceCounters(counterPool);

		assertEquals("5   0   2   "
		         + "8   10  9   6   "
			   + "4   3   11  4   3   "
		         + "11  6   5   8   "
				    +"12  9   10",
				game.toResourceCounterString());
	}

	//-----
	@Test
	public void toStringTest1() {
		Game<Tile> game = new CatanGame(2);
		assertEquals(CatanGame.toString(game.stream(), Object::toString),
			 "0   1   "
	     + "2   3   4   "
           + "5   6");
		assertEquals(CatanGame.toString(game.clockwiseStream(), Object::toString),
				 "0   1   4   6   5   2   3");
		assertEquals(CatanGame.toString(Stream.concat(game.clockwiseStream(), game.clockwiseStream()), Object::toString),
				 "0   1   4   6   5   2   3   0   1   4   6   5   2   3");
		assertEquals(CatanGame.toString(game.stream(),n->""),"");
	}

	@Test
	public void testDistributeResources() {
		CatanGame game = new CatanGame(3);
		List<Resource> resourcePool = new ArrayList<>();
		resourcePool.add(Resource.WOOD);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		resourcePool.add(Resource.DESERT);
		List<ResourceCounter> counterPool = ResourceCounter.generateResourceCounters();
		game.setResources(resourcePool);
		game.setResourceCounters(counterPool);
		assertTrue(game.getPlayers().size() > 0);
		game.stream().forEach(n -> game.addSettlement(n,
			Location.NORTH, new Settlement(game.getPlayers().get(0))));;
		game.distributeResources(5);
		assertEquals(
			game.getPlayers().get(0).getResourceAmount(Resource.WHEAT),
			(Integer)0);
		assertEquals(
			game.getPlayers().get(0).getResourceAmount(Resource.WOOD),
			(Integer)3);//3 since we have a Settlement at north of every tile, thus
		//the top level tile share 3 Settlements.
		assertEquals(game.stream().findFirst().get().getValue().getSettlements().size(),3);
		game.stream().findFirst().get().getValue().getSettlements().get(0).upgrade();
		game.distributeResources(5);
		assertEquals(
			game.getPlayers().get(0).getResourceAmount(Resource.WOOD),
			(Integer)7);//3(from before)+4 since a Settlement is now a city
		game.distributeResources(9);
		assertEquals(
			game.getPlayers().get(0).getResourceAmount(Resource.WOOD),
			(Integer)7);//no change!
	}
	 @Test
	 public void testSharedSettlement() {
         CatanGame game = new CatanGame(3);
         List<Resource> resourcePool = new ArrayList<>();
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         resourcePool.add(Resource.DESERT);
         List<ResourceCounter> counterPool = ResourceCounter.generateResourceCounters();
         game.setResources(resourcePool);
         game.setResourceCounters(counterPool);
         assertTrue(game.getPlayers().size() > 0);
         game.stream().forEach(n -> game.addSettlement(n,
        	Location.NORTH, new Settlement(game.getPlayers().get(0))));
         assertEquals(
        	CatanGame.toString(game.stream(), n -> ""+n.getSettlements().size()),
     	          "3   3   3   "
              + "3   3   3   3   "
            + "2   3   3   3   2   "
              + "2   3   3   2   "
                 +"1   1   1");
         }
}
