import os
import fnmatch
import re

# README
# this is going to break assertEquals that dont have a message, but a hardcoded string as first parameter

assertionsIncludingMessages = ur"assert(Equals|True|False|Null|NotNull)\(\s*\".*?\",\s*((.|\s)*?)\);"
replaceWith = ur"assert\1(\2);"

assertionsIncludingLinebreaks = ur"assert(Equals|True|False|Null|NotNull)\((.*?)[\n\t]*(.*?)\);"
replacedLinebreaks = ur"assert\1(\2\3);"


assertionsMissingSpacesAfterComma = ur"assert(Equals|True|False|Null|NotNull)\((.*?),(?! )(.*?)\);"
replacedMissingSpacesAfterComma = ur"assert\1(\2, \3);"


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

    content = re.sub(assertionsIncludingMessages, replaceWith, content)
    content = re.sub(assertionsIncludingLinebreaks, replacedLinebreaks, content)
    content = re.sub(assertionsMissingSpacesAfterComma, replacedMissingSpacesAfterComma, content)

    file = open(javaFilePath,"w")
    file.write(content.encode("utf-8"))
    file.close()

# ###############################################
def main():
    for file in allJavaUnitTestFiles():
        removeAssertionMessages(file)


# ###############################################
if __name__ == "__main__":
    main()