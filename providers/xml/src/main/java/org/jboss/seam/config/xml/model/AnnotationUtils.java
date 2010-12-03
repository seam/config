/*
 * JBoss, Home of Professional Opimport java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.seam.config.xml.util.XmlConfigurationException;
import org.jboss.seam.config.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
ublished by the Free Software Foundation; either version 2.1 of
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
package org.jboss.seam.config.xml.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.seam.config.xml.util.XmlConfigurationException;
import org.jboss.seam.config.xml.util.XmlObjectConverter;
import org.jboss.weld.extensions.reflection.AnnotationInstanceProvider;

/**
 * 
 * @author Stuart Douglas
 * 
 */
class AnnotationUtils
{
   final private static AnnotationInstanceProvider annotationInstanceProvider = new AnnotationInstanceProvider();

   @SuppressWarnings("unchecked")
   static Annotation createAnnotation(AnnotationXmlItem item)
   {
      Map<String, Object> typedVars = new HashMap<String, Object>();
      Class<?> anClass = item.getJavaClass();
      for (Entry<String, String> e : item.getAttributes().entrySet())
      {
         String mname = e.getKey();
         Method m;
         try
         {
            m = anClass.getDeclaredMethod(mname);
         }
         catch (Exception e1)
         {
            throw new XmlConfigurationException("Annotation " + item.getJavaClass().getName() + " does not have a member named " + mname + " ,error in XML", item.getDocument(), item.getLineno());
         }
         Class<?> returnType = m.getReturnType();
         typedVars.put(mname, XmlObjectConverter.convert(returnType, e.getValue()));
      }

      return annotationInstanceProvider.get((Class) item.getJavaClass(), typedVars);
   }
}
