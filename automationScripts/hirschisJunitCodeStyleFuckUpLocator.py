import os
import fnmatch
import re

assertionsIncludingMessages = ["assertEquals\(\".*?\",\s*.*?,\s*.*?\);",
                 "assertTrue\(\".*?\",\s*.*?\);",
                 "assertFalse\(\".*?\",\s*.*?\);",
                 "assertNull\(\".*?\",\s*.*?\);",
                 "assertNotNull\(\".*?\",\s*.*?\);"]

assertionsIncludingComparators = ["assertTrue\((\".*?\",\s*)?.*?(equals|==|!=|>|<).*?\);",
                                  "assertFalse\((\".*?\",\s*)?.*?(equals|==|!=|>|<).*?\);"]

fail = ["fail\(\".*?\"\);"]

tryCatch = ["try\s*\{",
            "\}\s*catch\s*\("]

# ###############################################
def allJavaUnitTestFiles():
    java_files = []
    for root, dirnames, filenames in os.walk('../catroid/src/androidTest/java/org/catrobat/catroid/test'):
        for filename in fnmatch.filter(filenames, '*.java'):
            java_files.append(os.path.join(root, filename))
    return java_files


# ###############################################
def reportBadAssertions(javaFilePath):
    file = open(javaFilePath, "r")
    content = file.read()

    allRegex = assertionsIncludingMessages + assertionsIncludingComparators + fail + tryCatch
    for regex in allRegex:
        regexMatches = re.compile(regex).finditer(content)
        for match in regexMatches:
            className = javaFilePath[javaFilePath.rfind("/")+1:]
            lineNumber = str(content[:match.start()].count("\n") + 1)
            print className + " " + lineNumber + ": " + match.group()
    file.close()


# ###############################################
def main():
    for file in allJavaUnitTestFiles():
        reportBadAssertions(file)


# ###############################################
if __name__ == "__main__":
    main()