import java.util.*;
import javafx.scene.paint.*;
import javafx.scene.*;

public class MyBadAI extends AI
{
   int numMoves = 0;

   public MyBadAI()
   {
      name = "BadAI";
   }
   /* Order of methods called by the AI */
   // 1. pickNextUnit
   // 2. Init
   // 3. ratePossibleToMoveForAdvancement
   // 4. removeDangerousMoves
   // 5. pickFinalMove


   //if you need to init any information. Such as clear out arraylists you make...
   //[harder] or, alternatively,  you could generate moves for every one of your units in this method and then assign them in pick final move.
   public void init(Unit u, ArrayList<Unit> unitsLeftToMove, boolean firstUnitInTurn)
   {

   }
   
   ArrayList<MapTile> spots = new ArrayList<MapTile>();
   ArrayList<MapTile> ourSpots = new ArrayList<MapTile>();

   public void ratePossiblePlacesToMoveForAdvancement( Unit theUnit,ArrayList<MapTile> possibleSpots)
   {
   
      boolean advantage = false;
   
      //get the enemy and friendly units
      ArrayList<Unit> enemyUnits = new ArrayList<Unit>();
      ArrayList<Unit> ourUnits = new ArrayList<Unit>();
      
      //gets all the tiles that are available for each type
      Map.Iterator allTileIterator = Map.getMap().generateIteratorAllTiles();
    
      //how many enemy, how many friendly
      while (allTileIterator.hasNext())
      {
         MapTile t = allTileIterator.next();
         if (t != null)
         {
            if (t.getUnit() != null)
            {
               if (t.getUnit().getOwner() != theUnit.getOwner())//enemy unit
               {
                  enemyUnits.add(t.getUnit());
               }
               else if(t.getUnit().getOwner() == theUnit.getOwner())//our unit
               {
                  ourUnits.add(t.getUnit());
               }
            }
         }
      }

      //see how many we have and they have
      System.out.println("Ours: "+ ourUnits.size());
      System.out.println("theirs : "+ enemyUnits.size());
      
      //make it knwon when we have an advantage
      if(ourUnits.size() >= enemyUnits.size())
         advantage = true;
      
      //clear spots
      spots.clear();
      ourSpots.clear();
   
      //Add tiles with enemy
      Map.Iterator theIterator = Map.getMap().generateIteratorAllTiles();
      while (theIterator.hasNext()) {
         MapTile tile = theIterator.next();
         if (tile.getUnit() != null && tile.getUnit().getOwner() != theUnit.getOwner()) 
         {
            spots.add(tile);
         }
         else if(tile.getUnit() != null && tile.getUnit().getOwner() == theUnit.getOwner())
         {
            ourSpots.add(tile);
         }
      }
      
      //test variable to see which node it is
      int currentIt = Map.initCounter(); 

      //set scores for each unit type
      
      if(numMoves < ourUnits.size())
      {
         //set good score to our boys
         for (int i = 0; i < ourSpots.size(); i++) 
         {
            //wont go back to visited spot
            ourSpots.get(i).setIt(currentIt);
            //base score is 100
            ourSpots.get(i).setGoodScore(100);
            
            if (ourSpots.get(i).getUnit().getType() == Unit.Type.RK)//RK
            {
               ourSpots.get(i).setGoodScore(ourSpots.get(i).getGoodScore() + 6);
            }
            if (ourSpots.get(i).getUnit().getType() == Unit.Type.ST)//ST
            {
               ourSpots.get(i).setGoodScore(ourSpots.get(i).getGoodScore() + 2);
            }
            if (ourSpots.get(i).getUnit().getType() == Unit.Type.MT)//MT
            {
               ourSpots.get(i).setGoodScore(ourSpots.get(i).getGoodScore() + 4);
            }
            if (ourSpots.get(i).getUnit().getType() == Unit.Type.ART)//Art
            {
               ourSpots.get(i).setGoodScore(ourSpots.get(i).getGoodScore() + 1);
            }
         }
      }
      else
      {
         for (int i = 0; i < spots.size(); i++) 
         {
            //wont go back to visited spot
            spots.get(i).setIt(currentIt);
            //base score is 100
            spots.get(i).setGoodScore(100);
            
            if (spots.get(i).getUnit().getType() == Unit.Type.RK)//RK PURPLE
            {
               spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 6);
               
               //prioritize the weak units
               if(spots.get(i).getUnit().getHP() < 8)
               {
                  spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 3);
               }
            }
            if (spots.get(i).getUnit().getType() == Unit.Type.ST)//ST GREY
            {
               spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 2);
               
               if(spots.get(i).getUnit().getHP() < 10)
               {
                  spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 3);
               }
            }
            if (spots.get(i).getUnit().getType() == Unit.Type.MT)//MT YELLOW
            {
               spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 4);
               
               if(spots.get(i).getUnit().getHP() < 20)
               {
                  spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 3);
               }
            }
            if (spots.get(i).getUnit().getType() == Unit.Type.ART)//Art BLUE
            {
               spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 1);
               
               if(spots.get(i).getUnit().getHP() < 8)
               {
                  spots.get(i).setGoodScore(spots.get(i).getGoodScore() + 3);
               }
            }
            
            //change good score if we  like the spot
            //the + allows that score to be more desirable
            
            //
            
            
         }
      }
      
      for (int i = 0; i < Map.SIZEX; i++) {
         for (int j = 0; j < Map.SIZEY; j++) {
            if (Map.getMap().getTile(i, j) != null)// null meaning tile does not exist AKA a blank space.
               Map.getMap().getTile(i, j).setVisited(false);
         }
      }
   
      //example of getting all tiles of move range of a unit -> tiles are in order starting from X/Y point and then nearest tiles next. No guarentees on which tiles will be called if there is a tie
      Map.Iterator it = Map.getMap().generateIteratorWithinMoveRange(theUnit.getX(),theUnit.getY(),theUnit.getSpeed(),theUnit);
      while(it.hasNext())
      {
         MapTile tile = it.next();
         //do something here.
         //set the score here
         int count = 0;
         
         while(spots.size() > 0)
         {
            count++;
            
            //taking available map tiles, and test it. and then remove it
            MapTile mt = spots.get(0);
            spots.remove(0);
            
            mt.setVisited(true);
            
            //gets neighbors using iterator
            MapTile.Iterator mit = mt.createIterator();
            
            while(mit.hasNext())
            {
               //temp value
               MapTile next = mit.next();
               
               if((!next.getVisited()) || (next.getGoodScore() - 1) > mt.getGoodScore())
               {
                  //empty space or taken space, cant move here
                  if(next.getUnit() == null || next.getUnit().getOwner() == theUnit.getOwner())
                  {
                     next.setVisited(true);
                     spots.add(next);
                     
                     if(next.getGoodScore() < mt.getGoodScore())
                     {
                        
                        //decreade the value of it by 1 it is further away
                        next.setGoodScore(mt.getGoodScore() -1);
                        
                        /*else if(!advantage)
                        {
                           //if we dont have advantage, run
                           next.setGoodScore(mt.getGoodScore() +1);
                        }*/
                     }
                  }
               }
            }
            
            if(count > 1000)
            {
               break;
            }
         }
         
         tile.getGoodScore();
         tile.setColor(Color.PURPLE);  //this was really so I can see what the AI is doing. You will probably find you want to display what the AI is doing too
      }
   
      
      numMoves++;
   
   
   
   
      if(true)
         return; //don't actually do any of these steps for the bad AI
   
      //example of getting all tiles in the map. Starts from top left corner
      it = Map.getMap().generateIteratorAllTiles();
      while(it.hasNext())
      {
         MapTile tile = it.next();
         //do something here.
      }


      //example of getting all tiles of attack range of a unit. Starts in top left corner of the square (if it was shaped as a square) and goes row major until bottom right
      it = Map.getMap().generateIteratorWithinAttackRange(theUnit.getX(),theUnit.getY(),theUnit.getRange(),theUnit);
      MapTile tile = null;
      while(it.hasNext())
      {
         tile = it.next();
         //do something here.
      }
      
      
      //tile.getUnit() returns null if no unit is present or the unit if a unit is present
      
      //examples of getting something from a unit
      if(tile.getUnit() != null)
      {
         tile.getUnit().getHP(); //get the current HP
         tile.getUnit().getType(); //get the type of unit
         tile.getUnit().getRange(); //attack range
         tile.getUnit().getSpeed(); //move range
         tile.getUnit().getOwner(); //integer indicating the owner. compare to "theUnit"'s owner to know if it is yours or the other
         tile.getUnit().getX(); //get tile position
         tile.getUnit().getY(); //get tile position
         tile.getUnit().getTile(); //get tile the unit is at
         
         tile.setGoodScore(100); // setting the good score to 100 -> default (cleared) value is -9000.
         tile.setBadScore(99); //setting the bad score to 99 -> default (cleared) value is -9000. These scores are interpreterd how you want to interpret them
         //possibleSpots.remove(0); remove a spot for the possible spots AL
         tile.getGoodScore();
         tile.getBadScore();
         
         MapTile.Iterator tileIterator = tile.createIterator(); //getting an iterator that gives all neighboring tiles
       
       
         MapTile t = Map.getMap().getTile(5,5); // getting a specific tile from the map  
         Map.getMap(); // getting the map
      }
   }
   
   //I suggest you use this to score bad moves OR remove bad possibleMoves. However, If no spots are left in possibleSpots, it will crash
   public void removeDangerousMoves(Unit theUnit,ArrayList<MapTile> possibleSpots)
   {
       //get the best spot
       /*int selected = 0;
       float max = 0;
       for (int i = 0; i < possibleSpots.size(); i++) {
         if (possibleSpots.get(i).getGoodScore() > max) {
            max = possibleSpots.get(i).getGoodScore();
            selected = i;
         }
      }
      
      //then it gets all the enemies if the unit was at that the new spot
      ArrayList<Unit> enemiesInRange = Map.getMap().getEnemyUnitsInRangeIfUnitUWasAtLocationXY(possibleSpots.get(selected).getX(),possibleSpots.get(selected).getY(),theUnit);      
      */
      
   }
   
   //generates the final move based on the previous methods (or, ignoring them if you prefer to write your code in this method only)
   public Move pickFinalMove(Unit theUnit,ArrayList<MapTile> possibleSpots)
   {
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
      ArrayList<Unit> enemiesInRange = Map.getMap().getEnemyUnitsInRangeIfUnitUWasAtLocationXY(possibleSpots.get(selected).getX(),possibleSpots.get(selected).getY(),theUnit);
      
      //then, if there is an enemy in range, it creates the move attack it
      Move m=null;
      if(enemiesInRange.size() > 0)
      {
         m = new Move(theUnit,new Vector2(possibleSpots.get(selected).getX(),possibleSpots.get(selected).getY()),true,enemiesInRange.get(0));
      }
      else
      {
         //otherwise just moves there
         m = new Move(theUnit,new Vector2(possibleSpots.get(selected).getX(),possibleSpots.get(selected).getY()),false,null);
      }
      return m;
   }
   
   //returns the index of the unit in unitsLeftToRun you want to run. Return 0 if don't care. The arraylist will probably be in different orders each time it is called.   
   public int pickNextUnit(ArrayList<Unit> unitsLeftToRun)
   {
      
      for (int j = 0; j < unitsLeftToRun.size(); j++)
      {
         if (unitsLeftToRun.get(j).getType() == Unit.Type.ST) 
         {
            //unitsLeftToRun.get(j);
            return j;
         } 
      }
         
      for (int j = 0; j < unitsLeftToRun.size(); j++)
      {  
         if (unitsLeftToRun.get(j).getType() == Unit.Type.MT) 
         {
            //unitsLeftToRun.get(j);
            return j;
         } 
      }
         
      for (int j = 0; j < unitsLeftToRun.size(); j++)
      {  
         if (unitsLeftToRun.get(j).getType() == Unit.Type.ART) 
         {
            //unitsLeftToRun.get(j);
            return j;
         }
      }
      
      for (int j = 0; j < unitsLeftToRun.size(); j++) //This for loop will make it pick specific types not just the first one
      {
         //we want to run the tanky bois first, then the long range ones
         if(unitsLeftToRun.get(j).getType() == Unit.Type.RK)
         {
            //unitsLeftToRun.get(j);
            return j;
         } 
      }
          //its a bad AI! just returns the first unit. Otherwise, pick the unit you want to run. Like, all of one type before all of another type...
      
      
      
      
      return 0; //its a bad AI! just returns the first unit. 
   }
}
