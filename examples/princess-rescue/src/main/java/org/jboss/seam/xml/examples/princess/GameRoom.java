/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.xml.examples.princess;

import java.io.Serializable;

/**
 * represents a room in the game. All configuration is done via XML
 * 
 * @author stuart
 * 
 */
public class GameRoom implements Serializable
{

   public GameRoom()
   {

   }

   /**
    * message that is displayed when the player enters the room.
    */
   String message;
   /**
    * message that is displayed when the player is adjacent to the room
    */
   String adjacentMessage;
   /**
    * What happens if an arrow is fired into the room
    */
   ShootEffect shootEffect = ShootEffect.NOTHING;
   /**
    * Message that is displayed when the arrow hits something, even if it is
    * just annoyed
    */
   String shootMessage;
   /**
    * Message that is display when a player enters a room that has something
    * they just shot.
    */
   String killedRoomMessage;

   /**
    * if the monster in the room has been killed.
    */
   boolean monsterKilled = false;

   /**
    * what happens when the player enters the room. There is no difference
    * between dying and winning, only a different message is displayed
    */
   RoomType roomType;

   GameRoom north, south, east, west;

   public void reset()
   {
      monsterKilled = false;
   }

   public String getMessage()
   {
      return message;
   }

   public void setMessage(String message)
   {
      this.message = message;
   }

   public String getAdjacentMessage()
   {
      return adjacentMessage;
   }

   public void setAdjacentMessage(String adjacentMessage)
   {
      this.adjacentMessage = adjacentMessage;
   }

   public ShootEffect getShootEffect()
   {
      return shootEffect;
   }

   public void setShootEffect(ShootEffect shootEffect)
   {
      this.shootEffect = shootEffect;
   }

   public String getShootMessage()
   {
      return shootMessage;
   }

   public void setShootMessage(String shootMessage)
   {
      this.shootMessage = shootMessage;
   }

   public String getKilledRoomMessage()
   {
      return killedRoomMessage;
   }

   public void setKilledRoomMessage(String killedRoomMessage)
   {
      this.killedRoomMessage = killedRoomMessage;
   }

   public RoomType getRoomType()
   {
      return roomType;
   }

   public void setRoomType(RoomType roomType)
   {
      this.roomType = roomType;
   }

   public GameRoom getNorth()
   {
      return north;
   }

   public void setNorth(GameRoom north)
   {
      this.north = north;
   }

   public GameRoom getSouth()
   {
      return south;
   }

   public void setSouth(GameRoom south)
   {
      this.south = south;
   }

   public GameRoom getEast()
   {
      return east;
   }

   public void setEast(GameRoom east)
   {
      this.east = east;
   }

   public GameRoom getWest()
   {
      return west;
   }

   public void setWest(GameRoom west)
   {
      this.west = west;
   }

   public boolean isMonsterKilled()
   {
      return monsterKilled;
   }

   public void setMonsterKilled(boolean monsterKilled)
   {
      this.monsterKilled = monsterKilled;
   }

}
