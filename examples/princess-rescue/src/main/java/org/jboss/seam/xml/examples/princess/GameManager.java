package org.jboss.seam.xml.examples.princess;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named
public class GameManager implements Serializable
{

   Instance<GameRoom> allRooms;

   @Inject
   GameMessage gameMessage;

   GameRoom startRoom;

   GameRoom currentRoom;

   String emptyRoomShootMessage;

   String startMessage;

   boolean gameOver = true;

   public String newGame()
   {
      for (GameRoom i : allRooms)
      {
         i.reset();
      }
      gameMessage.add(startMessage);
      runRoom(startRoom);
      gameOver = false;
      return "play";
   }

   public void runRoom(GameRoom room)
   {
      currentRoom = room;
      if (currentRoom.isMonsterKilled())
      {
         gameMessage.add(room.getKilledRoomMessage());
      }
      else
      {
         gameMessage.add(room.getMessage());
      }
      if (room.getRoomType() == RoomType.GAMEOVER)
      {
         gameOver = true;
      }
      for (GameRoom g : getAdjacentRooms())
      {
         gameMessage.add(g.getAdjacentMessage());
      }
   }

   public void runShoot(GameRoom room)
   {
      if (room.getRoomType() == RoomType.MONSTER)
      {
         gameMessage.add(room.getShootMessage());
         if (room.getShootEffect() == ShootEffect.KILL)
         {
            room.setMonsterKilled(true);
         }
         else if (room.getShootEffect() == ShootEffect.ANNOY)
         {
            gameOver = true;
         }
      }
      else
      {
         gameMessage.add(emptyRoomShootMessage);
      }
   }

   private Set<GameRoom> getAdjacentRooms()
   {
      HashSet<GameRoom> ret = new HashSet<GameRoom>();
      if (currentRoom.getNorth() != null)
      {
         ret.add(currentRoom.getNorth());
      }
      if (currentRoom.getSouth() != null)
      {
         ret.add(currentRoom.getSouth());
      }
      if (currentRoom.getEast() != null)
      {
         ret.add(currentRoom.getEast());
      }
      if (currentRoom.getWest() != null)
      {
         ret.add(currentRoom.getWest());
      }
      return ret;
   }

   public void shootNorth()
   {
      if (currentRoom.getNorth() != null)
      {
         runShoot(currentRoom.getNorth());
      }
      else
      {
         gameMessage.add("You cannot shoot that way");
      }
   }

   public void shootSouth()
   {
      if (currentRoom.getSouth() != null)
      {
         runShoot(currentRoom.getSouth());
      }
      else
      {
         gameMessage.add("You cannot shoot that way");
      }
   }

   public void shootEast()
   {
      if (currentRoom.getEast() != null)
      {
         runShoot(currentRoom.getEast());
      }
      else
      {
         gameMessage.add("You cannot shoot that way");
      }
   }

   public void shootWest()
   {
      if (currentRoom.getWest() != null)
      {
         runShoot(currentRoom.getWest());
      }
      else
      {
         gameMessage.add("You cannot shoot that way");
      }
   }

   public void moveNorth()
   {
      if (currentRoom.getNorth() != null)
      {
         runRoom(currentRoom.getNorth());
      }
      else
      {
         gameMessage.add("You cannot move that way");
      }
   }

   public void moveSouth()
   {
      if (currentRoom.getSouth() != null)
      {
         runRoom(currentRoom.getSouth());
      }
      else
      {
         gameMessage.add("You cannot move that way");
      }
   }

   public void moveEast()
   {
      if (currentRoom.getEast() != null)
      {
         runRoom(currentRoom.getEast());
      }
      else
      {
         gameMessage.add("You cannot move that way");
      }
   }

   public void moveWest()
   {
      if (currentRoom.getWest() != null)
      {
         runRoom(currentRoom.getWest());
      }
      else
      {
         gameMessage.add("You cannot move that way");
      }
   }

   public GameRoom getCurrentRoom()
   {
      return currentRoom;
   }

   public void setCurrentRoom(GameRoom currentRoom)
   {
      this.currentRoom = currentRoom;
   }

   public boolean isGameOver()
   {
      return gameOver;
   }
}
