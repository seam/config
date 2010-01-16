/*
 * Distributed under the LGPL License
 * 
 */
package org.jboss.seam.xml.util;

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
