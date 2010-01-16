Seam XML Extensions Readme

To use:

Place the seam-xml*.jar on the classpath,

The extension will look for a file called seam-beans.xml in the root of all archives in the deployment. There is an extension mechanism 
that allows you to register other xml sources that will get documented in a future release (have a look at TestXmlProvider and the 
org.jboss.seam.xml.XmlExtension file if you want more info).

The xml format:

The best way to understand the format is with some examples:

<Beans xmlns="urn:seam:core"
          xmlns:test="urn:java:org.jboss.seam.xml.test.beans">
    <test:Bean1/>
</Beans>

This registers a single bean, org.jboss.seam.xml.test.beans.Bean1


<Beans xmlns="urn:jbraze:core"
          xmlns:test="urn:java:org.jboss.seam.xml.test.beans">
   
    <test:Bean1>
        <test:bean2>
            <Inject/>
        </test:bean2>
    </test:Bean1>
    
    <test:Bean2>
        <test:produceBean3>
            <Produces/>
        </test:produceBean3>
    </test:Bean2>
          
</Beans>

This registers two beans, Bean1 has injection target, a field named bean2. Bean2 has a producer field named produceBean3.


<Beans xmlns="urn:jbraze:core"
          xmlns:test="urn:java:org.jboss.seam.xml.test.beans">
   
    <test:OtherQualifier>
        <Qualifier/>
    </test:OtherQualifier>
     
    <test:QualifiedBean1>
        <test:OtherQualifier value1="AA" value2="1">A</test:OtherQualifier>
    </test:QualifiedBean1>
    
    <test:QualifiedBean2>
        <test:OtherQualifier value1="BB" value2="2" value="B" />
    </test:QualifiedBean2>
    
    <test:QualifierTestBean>
        <test:bean1>
            <test:OtherQualifier value1="AA" value2="1" value="A" />
            <Inject/>
        </test:bean1>
        <test:bean2>
            <test:OtherQualifier value1="BB" value2="2">B</test:OtherQualifier>
            <Inject/>
        </test:bean2>
    </test:QualifierTestBean>
          
</Beans>

This registers a qualifier, and then registers two beans with the given values for the qualifiers attributes. 
QualifierTestBean has two injection points, also with qualifiers. For more examples look at the
src/test/resources folder. 
