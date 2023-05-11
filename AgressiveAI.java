import java.util.*;
import javafx.scene.paint.*;
import javafx.scene.*;

public class AgressiveAI extends AI {
   public AgressiveAI() {
      name = "AgressiveAI";
   }

   //this method is called first if you need to init any information. Such as clear out arraylists...
   //and [much harder] you could generate moves for every one of your units in this method and then assign them in pick final move.
   //it really depends if you want to do it unit by unit or keep context. Unit by unit is much easier, but I wanted to provide the interface to be able to do it globally if you wanted to.
   public void init(Unit u, ArrayList<Unit> unitsLeftToMove, boolean firstUnitInTurn)
   {
      //get side/player and get the arraylist in order of how we want to move
   }


   ArrayList<MapTile> spots = new ArrayList<MapTile>();

   public void ratePossiblePlacesToMoveForAdvancement(Unit theUnit, ArrayList<MapTile> possibleSpots)
   {

      //get the enemy units
      ArrayList<Unit> enemyUnits = new ArrayList<Unit>();
      Map.Iterator allTileIterator = Map.getMap().generateIteratorAllTiles();
      while (allTileIterator.hasNext())
      {
         MapTile t = allTileIterator.next();
         if (t != null)
         {
            if (t.getUnit() != null)
            {
               if (t.getUnit().getOwner() != theUnit.getOwner())
               {
                  enemyUnits.add(t.getUnit());
               }
            }
         }
      }


      //algorithm to calculate scores. 

      spots.clear();

      //first add all tiles with an enemy to my list
      Map.Iterator theIterator = Map.getMap().generateIteratorAllTiles();
      while (theIterator.hasNext()) {
         MapTile tile = theIterator.next();
         if (tile.getUnit() != null && tile.getUnit().getOwner() != theUnit.getOwner()) {
            spots.add(tile);
         }
      }


      //demonstrating custom traversal of the graph, spirals outwards from the center points.
      int currentIt = Map.initCounter(); //this gets the next traversal number. The number is used it indicate if this particular traversal has encountered a given node.

      //score the enemy tiles as 100 or 102 (depending on type). Higher score, in this case, means a more prefereable tile.
      for (int i = 0; i < spots.size(); i++) {
         spots.get(i).setIt(currentIt); //set that this is a starting tile and will not revisit it later (well, unless its value is to small)
         spots.get(i).setGoodScore(100);
         if (spots.get(i).getUnit().getType() == Unit.Type.RK) //demonstrating the use of get type
         {
            spots.get(i).setGoodScore(103);
         }
      }


      //custom traversal. Its not really necessary, but here is how it works.

      //Step 1, set all nodes not visited
      for (int i = 0; i < Map.SIZEX; i++) {
         for (int j = 0; j < Map.SIZEY; j++) {
            if (Map.getMap().getTile(i, j) != null)// null meaning tile does not exist AKA a blank space.
               Map.getMap().getTile(i, j).setVisited(false);
         }
      }

      int count = 0;
      //Step 2:
      //starting from each enemy unit, traverse entire map, assinging scores based on distance from an enemy unit - manual traversal instead of using the iterator as I want to start from each enemy at the same time and work my way outward
      //It goes through each enemy, adding the tiles that are near to them (as long as they were not added already), each time it sets the score as the neightbour tile as -1 of the current tile
      while (spots.size() > 0) {
         count++;

         //get and remove first item in the list
         MapTile mt = spots.get(0);
         /*
         will leave it commented out for now
         for(int j =0; j<spots.size(); j++) //This for loop will make it pick specific types not just the first one
         {
            if(spots.contains(Unit.Type.RK) == true)
            {
               mt = spots.get(j);
            }
            else if(spots.contains(Unit.Type.MT) == true)
            {
               mt = spots.get(j);
            }
            else if(spots.contains(Unit.Type.ST) == true)
            {
               mt = spots.get(j);
            }
            else if(spots.contains(Unit.Type.ART) == true)
            {
               mt = spots.get(j);
            }
         }*/
         spots.remove(0);

         //set you visited it so you cannot visit it a second time
         mt.setVisited(true);
         MapTile.Iterator it = mt.createIterator(); //each tile has the ability to get its neighbors through the use of an iterator

         while (it.hasNext()) //check & getting for the next neighboring tile of mt
         {
            MapTile next = it.next();

            if ((!next.getVisited()) || next.getGoodScore() - 1 > mt.getGoodScore()) //cannot visit a node twice unless the next node is larger than this node, we use currentIt to indicate whether it has visited that tile previously
            {
               if (next.getUnit() == null || next.getUnit().getOwner() == theUnit.getOwner()) // if the tile is null OR if I can move through my own unit. Cannot move through enemy units...
               {
                  next.setVisited(true);
                  spots.add(next);
                  if (next.getGoodScore() < mt.getGoodScore())
                     next.setGoodScore(mt.getGoodScore() - 1); //every tile away from an enemy, i decrease the score by 1.
               }
            }
         }

         if (count > 1000)
            break; //don't let it continue forever. Safety against an inf loop.
      }
   }

   //can remove files from possibleSpots or assign bad scores
   public void removeDangerousMoves(Unit theUnit, ArrayList<MapTile> possibleSpots)
   {

   }

   //generates the final move based on the previous methods (or, ignoring them if you prefer to write your code in this method only)
   public Move pickFinalMove(Unit theUnit, ArrayList<MapTile> possibleSpots) {
      //picking highest score tile. Note this does not use the bad score at all
      int selected = 0;
      float max = 0;


      for (int i = 0; i < possibleSpots.size(); i++) {
         if (possibleSpots.get(i).getGoodScore() > max) {
            max = possibleSpots.get(i).getGoodScore();
            selected = i;
         }
      }

      //then it gets all the enemies if the unit was at that the new spot
      ArrayList<Unit> enemiesInRange = Map.getMap().getEnemyUnitsInRangeIfUnitUWasAtLocationXY(possibleSpots.get(selected).getX(), possibleSpots.get(selected).getY(), theUnit);

      //then, if there is an enemy in range, it creates the move attack it
      Move m = null;
      if (enemiesInRange.size() > 0) {
         m = new Move(theUnit, new Vector2(possibleSpots.get(selected).getX(), possibleSpots.get(selected).getY()), true, enemiesInRange.get(0));
      } else {
         //otherwise just moves there
         m = new Move(theUnit, new Vector2(possibleSpots.get(selected).getX(), possibleSpots.get(selected).getY()), false, null);
      }
      return m;
   }

   public int pickNextUnit(ArrayList<Unit> unitsLeftToRun)
   {
      for (int j = 0; j < unitsLeftToRun.size(); j++) //This for loop will make it pick specific types not just the first one
      {
         if(unitsLeftToRun.contains(Unit.Type.RK) == true)
         {
            unitsLeftToRun.get(j);
            return j;
         } else if (unitsLeftToRun.contains(Unit.Type.MT) == true) {
            unitsLeftToRun.get(j);
            return j;
         } else if (unitsLeftToRun.contains(Unit.Type.ST) == true) {
            unitsLeftToRun.get(j);
            return j;
         } else if (unitsLeftToRun.contains(Unit.Type.ART) == true) {
            unitsLeftToRun.get(j);
            return j;
         }
          //its a bad AI! just returns the first unit. Otherwise, pick the unit you want to run. Like, all of one type before all of another type...
      }
      return 0;
   }
}

