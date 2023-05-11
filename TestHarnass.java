//this is for the test version of the program (i.e., play lots of games against each other). 
//Use AFTER the AI is finished. Run the GUI version when developing so you see what happens. 
//I would't develop my AI using this way as I cannot see what happens. 
// change map or ai array to add a new map or a new AI

import java.util.*;

public class TestHarnass
{
   static Random gen = new Random();

  //play X games and get the average scores, swapping sides each time.
  public static VectorF2 play(AI one, AI two, int[][] map, GameController cg, int runs, int aiIndex1, int aiIndex2)
  {
      float avg1=0;
      float avg2=0;
      
      int[][] origmap = map;
      gen.setSeed(0);
      
      for(int i=0;i<runs;i++)
      {
         
         
         if(i%2 == 0)
         {
            map = Arrays.stream(origmap).map(int[]::clone).toArray(int[][]::new);
            
            for(int k=0;k<map.length;k++)
            {
               for(int j=0;j<map[k].length;j++)
               {
                  if(map[k][j] != 0 && map[k][j] != -1)
                  {
                     //has a unit
                     int placement = gen.nextInt(5);
                     //0 = same place
                     
                     //1 = up
                     if(placement == 1)
                     {
                        if(k != 0 && map[k-1][j] != -1 )
                        {
                           map[k-1][j] = map[k][j];
                           map[k][j] = 0;
                        }
                     }
                     
                     //2 = down
                     if(placement == 2)
                     {
                        if(k != map.length-1 && map[k+1][j] != -1)
                        {
                           map[k+1][j] = map[k][j];
                           map[k][j] = 0;
                        }
                     }
                     
                     //3 = left
                     if(placement == 3)
                     {
                        if(j != 0 && map[k][j-1] != -1)
                        {
                           map[k][j-1] = map[k][j];
                           map[k][j] = 0;
                        }
                     }
                     
                     
                     //4 = right
                     if(placement == 4)
                     {
                        if(j != map[0].length-1 && map[k][j+1] != -1)
                        {
                           map[k][j+1] = map[k][j];
                           map[k][j] = 0;
                        }
                     }
                  }
               }
            }
            /*for(int k=0;k<map.length;k++)
            {
               for(int j=0;j<map[0].length;j++)
               {
                  if(map[k][j] == -1)
                     System.out.print(map[k][j]+" ");
                  else
                     System.out.print(" "+map[k][j]+" ");
               }
               System.out.println();
            }
            System.out.println();*/
         }
         
         cg.Start(i%2 == 0 ? one : two,i%2 == 1 ? one : two,map);
         
         float score;
         
         //fancy way for playing until there is a score returned
         while( (score = cg.Update(null,100)) == -1); 
         
         if(score != -1)
         {
            if(cg.getWinningAI() == one)
            {

               avg1 += score;
               //System.out.println(""+score+",0");
            }
            else if(cg.getWinningAI() == two)
            {
               avg2 += score;
               //System.out.println("0,"+score);
            }
            
            
         }
      } 

      //return the score averages
      VectorF2 vec = new VectorF2(avg1/runs,avg2/runs);
      
      return vec;
  }

  public static void main(String[] args)
  {
      GameController cg = new GameController();
      
      //loop over all AIs and maps and play them.
      for(int i=0;i<3;i++)
      {
         for(int j=0;j<3;j++)
         {
            if(j != i)
               for(int k=0;k<maps.length;k++)
               {
                  System.out.println(ais[i].getName() +"," +ais[j].getName()+","+k+","+play(ais[i], ais[j], maps[k],cg,numberOfGames,i,j));           
               }
         }
      }

   }
   
   
   static int numberOfGames = 10;
   public static int[][][] maps = {
   
      //maps may be larger or smaller. Do not assume its 11x10
      /* here 2 is st, 4 is mt, 6 is rk and the |0 or |1 is the player*/
      
      //small valley to go through. balanced units
      {
      {0,  0,  0,  0,  0,  -1, -1,  0,  0,  0,  0,  0},
      {0,  0,6|1,  0,  0,  -1, -1,  0,  0,6|0,  0,  0},
      {0,  0,  0,  0,2|1,  -1, -1,2|0,  0,  0,  0,  0},
      {0,  0,  0,2|1,  0,  -1, -1,  0,2|0,  0,  0,  0},
      {8|1,0,4|1,  0,  0,   0,  0,  0,  0,4|0,  0,8|0},
      {8|1,0,4|1,  0,  0,   0,  0,  0,  0,4|0,  0,8|0},
      {0,  0,  0,2|1,  0,  -1, -1,  0,2|0,  0,  0,  0},
      {0,  0,  0,  0,2|1,  -1, -1,2|0,  0,  0,  0,  0},
      {0,  0,6|1,  0,  0,  -1, -1,  0,  0,6|0,  0,  0},
      {0,  0,  0,  0,  0,  -1, -1,  0,  0,  0,  0,  0},
      }
      ,
   
   
      //balanced units and fairly open map
      {
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      {    0,  0,4|1,  0,  0,4|0,  0,  0},
      {    0,2|1,  0, -1, -1,  0,2|0,  0},
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      {    0,2|1,  0, -1, -1,  0,2|0,  0},
      {    0,  0,4|1,  0,  0,4|0,  0,  0},
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      }
      
      ,
   
      //fun with ranged units
      {
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      {    0,  0,4|1, -1, -1,4|0,  0,  0},
      {    0,2|1,  0, -1, -1,  0,2|0,  0},
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      {    0,2|1,  0, -1, -1,  0,2|0,  0},
      {    0,  0,4|1, -1, -1,4|0,  0,  0},
      {  6|1,  0,  0,  0,  0,  0,  0,6|0},
      }
      

      ,
      
      {
      {  6|1,  0,  0,  0,  0,  0,  0,4|0},
      {  6|1,  0,  0,  0, 0,4|0,  0,  0},
      {  6|1,  0,  0,  0, 0,  0,2|0,  0},
      {  6|1,  0,  0,  0,  0,  0,  0,2|0},
      {  6|1,  0,  0,  0,  0,  0,  0,2|0},
      {  6|1,  0,  0,  0, 0,  0,2|0,  0},
      {  6|1,  0,  0,  0, 0,4|0,  0,  0},
      {  6|1,  0,  0,  0,  0,  0,  0,4|0},
      }
      
      ,
      
      //st wars
      {
      {0,  0,2|1,2|1,  0,  -1, -1,  0,2|0,2|0,  0,  0},
      {0,  0,2|1,2|1,  0,  -1, -1,  0,2|0,2|0,  0,  0},
      {0,  0,2|1,  0,2|1,  -1, -1,2|0,  0,2|0,  0,  0},
      {0,  0,  0,  0,  0,  -1, -1,  0,  0,  0,  0,  0},
      {0,  0,  0,  0,  0,   0,  0,  0,  0,  0,  0,  0},
      {0,  0,  0,  0,  0,   0,  0,  0,  0,  0,  0,  0},
      {0,  0,  0,  0,  0,  -1, -1,  0,  0,  0,  0,  0},
      {0,  0,2|1,  0,2|1,  -1, -1,2|0,  0,2|0,  0,  0},
      {0,  0,2|1,2|1,  0,  -1, -1,  0,2|0,2|0,  0,  0},
      {0,  0,2|1,2|1,  0,  -1, -1,  0,2|0,2|0,  0,  0},
      }
      ,
       //st and mt wars
      {
      {0,  0,2|1,4|1,  0,  -1, -1,  0,4|0,2|0,  0,  0},
      {0,  0,2|1,4|1,  0,  -1, -1,  0,4|0,2|0,  0,  0},
      {0,  0,2|1,  0,4|1,  -1, -1,4|0,  0,2|0,  0,  0},
      {0,  0,  0,  0,  0,  -1, -1,  0,  0,  0,  0,  0},
      {0,  0,  0,  0,  0,   0,  0,  0,  0,  0,  0,  0},
      {0,  0,  0,  0,  0,   0,  0,  0,  0,  0,  0,  0},
      {0,  0,  0,  0,  0,  -1, -1,  0,  0,  0,  0,  0},
      {0,  0,2|1,  0,4|1,  -1, -1,4|0,  0,2|0,  0,  0},
      {0,  0,2|1,4|1,  0,  -1, -1,  0,4|0,2|0,  0,  0},
      {0,  0,2|1,4|1,  0,  -1, -1,  0,4|0,2|0,  0,  0},
      }
   
   };
   
   //array of the different AIs
   static AI [] ais = { new AgressiveAI(), new MyBadAI(), new FleeAI() };
}
