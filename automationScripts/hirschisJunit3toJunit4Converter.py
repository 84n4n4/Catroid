import os
import fnmatch
import re

######
"""public class CollisionDetectionPolygonCreationTest extends InstrumentationTestCase """
# to
"""@RunWith(AndroidJUnit4.class)
    public class CollisionDetectionPolygonCreationTest """
classNameRegex = ur"public class (.*?)Test extends .+? "
classNameReplace = ur"@RunWith(AndroidJUnit4.class)\npublic class \1Test "

######
"""@Override
	protected void setUp()"""
# to
"""@Before
    public void setUp()"""
setUpRegex = ur"@Override\s*?(protected|public) void setUp\(\)"
setUpReplace = ur"@Before\n\tpublic void setUp()"

######
"""super.setUp();"""
# to
""""""
setUpSuperRegex = ur"\s*super.setUp\(\);"
setUpSuperReplace = ur""

######
""""@Override
    protected void tearDown()"""
# to
"""@After
    public void tearDown()"""
tearDownRegex = ur"@Override\n\t(protected|public) void tearDown\(\)"
tearDownReplace = ur"@After\n\tpublic void tearDown()"

######
"""super.tearDown();"""
# to
""""""
tearDownSuperRegex = ur"\s*super.tearDown\(\);"
tearDownSuperReplace = ur""

######
"""public void test"""
# to
"""@Test
    public void test"""
testMethodRegex = ur"(?<!@Test\n\t)(?<!@UiThreadTest\n\t)(?<!//\t)public void test(.*)\(\) "
testMethodReplace = ur"@Test\n\tpublic void test\1() "

######
"""getInstrumentation()"""
# to
"""InstrumentationRegistry"""
instrumentationRegex = ur"(?<!\InstrumentationRegistry.)(this\.)?getInstrumentation\(\)"
instrumentationReplace = ur"InstrumentationRegistry"

getContextRegex = ur"(?<!InstrumentationRegistry\.)(?<!getTar)(this\.)?getContext\(\)"
getContextReplace = ur"InstrumentationRegistry.getContext()"

######
"""import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;"""
# has to be added to imports
importsRegex = ur"(package .*?;)"
importsReplace = ur"""\1

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
"""


######
# manually add
"""	@Rule
	public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();"""
# and
"""@UiThreadTest"""
#if necessary


# ###############################################
def allJavaUnitTestFiles():
    java_files = []
    for root, dirnames, filenames in os.walk('../catroid/src/androidTest/java/org/catrobat/catroid/test'):
        for filename in fnmatch.filter(filenames, '*.java'):
            java_files.append(os.path.join(root, filename))
    return java_files


# ###############################################
def convert(javaFilePath):
    file = open(javaFilePath, "r")
    content = unicode(file.read(), "utf-8")
    file.close()
    content = re.sub(classNameRegex, classNameReplace, content)
    content = re.sub(setUpRegex, setUpReplace, content)
    content = re.sub(setUpSuperRegex, setUpSuperReplace, content)
    content = re.sub(tearDownRegex, tearDownReplace, content)
    content = re.sub(tearDownSuperRegex, tearDownSuperReplace, content)
    content = re.sub(testMethodRegex, testMethodReplace, content)
    content = re.sub(instrumentationRegex, instrumentationReplace, content)
    content = re.sub(importsRegex, importsReplace, content)
    content = re.sub(getContextRegex, getContextReplace, content)

    file = open(javaFilePath ,"w")
    file.write(content.encode("utf-8"))
    file.close()

# ###############################################
def main():
    for file in allJavaUnitTestFiles():
        convert(file)


# ###############################################
if __name__ == "__main__":
    main()