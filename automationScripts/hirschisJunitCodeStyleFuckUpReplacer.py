import os
import fnmatch
import re

assertionsIncludingMessages = ur"assert(Equals|True|False|Null|NotNull)\(\".*?\", (\s*.*?)\);"
replaceWith = ur"assert\1(\2);"


# ###############################################
def allJavaUnitTestFiles():
    java_files = []
    for root, dirnames, filenames in os.walk('../catroid/src/androidTest/java/org/catrobat/catroid/test'):
        for filename in fnmatch.filter(filenames, '*.java'):
            java_files.append(os.path.join(root, filename))
    return java_files


# ###############################################
def removeAssertionMessages(javaFilePath):
    file = open(javaFilePath, "r")
    content = unicode(file.read(), "utf-8")
    file.close()

    replacement = re.sub(assertionsIncludingMessages, replaceWith, content)
    
    file = open(javaFilePath,"w")
    file.write(replacement.encode("utf-8"))
    file.close()

# ###############################################
def main():
    for file in allJavaUnitTestFiles():
        removeAssertionMessages(file)


# ###############################################
if __name__ == "__main__":
    main()