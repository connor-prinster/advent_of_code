package dayseven

import java.io.File
import java.util.Stack


enum class CliType {
    COMMAND, RESULT
}
interface Result {
    val name: String
    val parentDirectory: String
}
open class FileResult(
    override val name: String,
    override val parentDirectory: String,
    val fileSize: Int
): Result
data class DirectoryResult(
    override val name: String,
    override val parentDirectory: String,
): Result
data class Directory(
    val name: String,
    val content: ArrayList<Result>,
    val totalSize: Int = 0
)
data class CliText(
    val text: String,
    val type: CliType,
    val previousCommand: String
)

class DaySeven {
    private fun parseFile(filename: String): List<CliText> {
        val cliTexts: ArrayList<CliText> = arrayListOf()
        var lastCommand: String = ""
        File(filename)
            .useLines {
                it.toList().forEach { cliText ->
                    if (cliText.contains("$")) {
                        cliTexts.add(
                            CliText(
                                text = cliText,
                                type = CliType.COMMAND,
                                previousCommand = lastCommand
                            ))
                    } else {
                        cliTexts.add(
                            CliText(
                                text = cliText,
                                type = CliType.RESULT,
                                previousCommand = lastCommand
                            ))
                    }

                    lastCommand = cliText
                }
            }

        return cliTexts
    }

    private fun parsedToCommand(parsed: List<CliText>) {
        print(parsed)
        val directoryMap: HashMap<String, Directory> = hashMapOf()
        val dirStack: Stack<String> = Stack()
        var parentDirectory: String = "/"

        parsed.forEach { cliText ->
            if (cliText.text.contains("cd")) {
                val splitDir = cliText.text.split("cd ")
                if (splitDir[1] != "..")
                    directoryMap[splitDir[1]] = Directory(
                        name = splitDir[1],
                        content = arrayListOf()
                    )
            }
        }

        parsed.forEach { cliText ->
            if (cliText.text.contains("cd")) {
                val splitDir = cliText.text.split("cd ")
                val dirString = splitDir[1]
                parentDirectory = dirString
                if (dirString == "..") {
                    dirStack.pop()
                } else {
                    dirStack.add(dirString)
                }
            } else if (!cliText.text.contains("$ ls")) {
                val currentDir = dirStack.peek()
                val isDir = cliText.text.contains("dir")
                directoryMap[parentDirectory]?.content?.add(
                    if (isDir) {
                        DirectoryResult(
                            name = cliText.text,
                            parentDirectory = currentDir
                        )
                    } else {
                        val sizeAndName = cliText.text.split(" ")
                        FileResult(
                            name = sizeAndName[1],
                            parentDirectory = currentDir,
                            fileSize = sizeAndName[0].toInt()
                        )
                    }
                )

            }
        }

        print(directoryMap)
    }

    fun sumOfTotalSizeOfDirectories(filename: String) {
        parsedToCommand(parseFile(filename))
    }
}


fun main(args: Array<String>) {
    val filename = "advent_of_code_2022/src/main/kotlin/dayseven/test_input.txt"
    println("How many characters must be parsed for marker: ${DaySeven().sumOfTotalSizeOfDirectories(filename)}")
}