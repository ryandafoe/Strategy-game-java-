import java.util.*;
import javafx.scene.paint.*;
import javafx.scene.*;

public class FleeAI extends AI
{
   public FleeAI()
   {
      name = "BadAI";
   }

   public void init(Unit u, ArrayList<Unit> unitsLeftToMove, boolean firstUnitInTurn)
   {

   }

   public void ratePossiblePlacesToMoveForAdvancement( Unit theUnit,ArrayList<MapTile> possibleSpots)
   {
      //this should be an efficent DP algorithm.... buts its not efficient... Opps! (that is a joke, it's intentional)
      
      //this example does not use the iterator pattern - it is another way to code up an AI.
      
      int [][] scores = new int[Map.SIZEX][Map.SIZEY];
      
      //set all positions to the max value.
      for(int i=0;i<Map.SIZEX;i++)
      {
         for(int j=0;j<Map.SIZEY;j++)
         {
            scores[i][j] = Integer.MAX_VALUE;
         }
      }
      
      //idea is this: set the positions of enemy units to 1. Then, while a tile was changed last round, iterate over all the tiles and pick the smallest + 1 value (if not max).
      
      //set positions of enemies to 1s
      for(int i=0;i<Map.SIZEX;i++)
      {
         for(int j=0;j<Map.SIZEY;j++)
         {
            if(Map.getMap().getTile(i,j) != null) //check if the tile is blank or not
            {
               if(Map.getMap().getTile(i,j).getUnit() != null) //if there is a unit
               {
                  if(Map.getMap().getTile(i,j).getUnit().getOwner() != theUnit.getOwner()) //if the unit is not on my team... (theUnit is my unit)
                  {
                     scores[i][j] = 1; //set score of that spot it 1
                  }              
               }
            }
         }
      }         
      
      
      boolean change = true;
      while(change)
      {
         change = false;
         for(int i=0;i<Map.SIZEX;i++)
         {
            for(int j=0;j<Map.SIZEY;j++)
            {
               if(Map.getMap().getTile(i,j) != null)
               {
                  if(i-1 >= 0 && scores[i-1][j] != Integer.MAX_VALUE && scores[i-1][j] + 1 < scores[i][j])
                  {
                     scores[i][j] = scores[i-1][j] + 1;
                     change = true;
                  }
                  if(i+1 < Map.SIZEX  && scores[i+1][j] != Integer.MAX_VALUE && scores[i+1][j] + 1 < scores[i][j])
                  {
                     scores[i][j] = scores[i+1][j] + 1;
                     change = true;
                  }
                  if(j-1 >= 0 && scores[i][j-1] != Integer.MAX_VALUE && scores[i][j-1] + 1 < scores[i][j])
                  {
                     scores[i][j] = scores[i][j-1] + 1;
                     change = true;
                  }
                  if(j+1 < Map.SIZEY  && scores[i][j+1] != Integer.MAX_VALUE && scores[i][j+1] + 1 < scores[i][j])
                  {
                     scores[i][j] = scores[i][j+1] + 1;
                     change = true;
                  }
               }
            }
         }     
      
      
            /*for(int j=0;j<Map.SIZEY;j++)
            {
               for(int i=0;i<Map.SIZEX;i++)
               {
                  if(scores[i][j] != Integer.MAX_VALUE)
                     System.out.print(scores[i][j]+" ");
                  else
                     System.out.print("  ");
               }
            System.out.println();
            }
         System.out.println();
         System.out.println();*/
      }
      
      for(int i=0;i<Map.SIZEX;i++)
      {
         for(int j=0;j<Map.SIZEY;j++)
         {
            if(Map.getMap().getTile(i,j) != null) //check if the tile is blank or not
            {
               if(Map.getMap().getTile(i,j).getUnit() == null) //if there is a unit
               {
                  Map.getMap().getTile(i,j).setGoodScore(scores[i][j]);   
               }
            }
         }
      }         
      
      

   }
   
   //I suggest you use this to score bad moves OR remove bad possibleMoves. However, If no spots are left in possibleSpots, it will crash
   public void removeDangerousMoves(Unit theUnit,ArrayList<MapTile> possibleSpots)
   {
   
   }
   
   //generates the final move based on the previous methods (or, ignoring them if you prefer to write your code in this method only)
   public Move pickFinalMove(Unit theUnit,ArrayList<MapTile> possibleSpots)
   {
      //picking highest score tile. Note this does not use the bad score at all
      int selected = 0;
      float max=0;

      for(int i=0;i<possibleSpots.size();i++)
      {
         if(possibleSpots.get(i).getGoodScore() > max)
         {
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
      return 0; //its a bad AI! just returns the first unit. 
   }
}
