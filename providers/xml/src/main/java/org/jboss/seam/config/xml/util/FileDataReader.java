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
package org.jboss.seam.config.xml.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Utility class that reads a file or URL into a String
 * 
 * @author Stuart Douglas <stuart@baileyroberts.com.au>
 * 
 */
public class FileDataReader
{

   public static String readUrl(URL u) throws IOException
   {
      InputStream s = u.openStream();
      String res = readFile(s);
      s.close();
      return res;
   }

   public static String readFile(InputStream file) throws IOException
   {
      InputStreamReader reader = new InputStreamReader(file);
      StringBuilder fileData = new StringBuilder();
      char[] buf = new char[1024];
      int numRead = 0;

      while ((numRead = reader.read(buf)) != -1)
      {
         String readData = String.valueOf(buf, 0, numRead);
         fileData.append(readData);
      }
      return fileData.toString();
   }
}
