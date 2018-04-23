import urllib
import sys
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

diffLineNumberRe = "\@\@\s*-\d+,\d+\s*\+\d+,\d+\s*\@\@"


# ###############################################
def getGitHubDiffUrl(prNumber):
    return "https://patch-diff.githubusercontent.com/raw/Catrobat/Catroid/pull/" + str(prNumber) + ".diff"


# ###############################################
def getGitHubDiff(prNumber):
    w = urllib.urlopen(getGitHubDiffUrl(prNumber))
    response = w.read()
    w.close()
    return response


# ###############################################
def findBadAssertions(javaDiff):
    allRegex = assertionsIncludingMessages + assertionsIncludingComparators + fail + tryCatch
    for regex in allRegex:
        regexMatches = re.compile("\+\s*?" + regex).finditer(javaDiff)
        for match in regexMatches:
            matchedDiffStart = javaDiff[:match.start()].rfind("diff --git")
            fileName = javaDiff[matchedDiffStart:].splitlines()[0].split(" ")[-1].split("/")[-1]
            lineNumber = re.findall(diffLineNumberRe, javaDiff)[-1]
            if fileName.endswith("Test.java"):
                print fileName + " " + lineNumber + "\n" + match.group().lstrip("+").lstrip() + "\n"


# ###############################################
def main(prNumber):
    findBadAssertions(getGitHubDiff(prNumber))


# ###############################################
if __name__ == "__main__":
    if sys.argv.__len__() != 2:
        print """Usage:
        python hirschisJunitCodeStyleFuckUpLocatorPREdition.py [GITHUB PR NUMBER]"""
    else:
        main(sys.argv[1])