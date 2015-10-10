/*
Searcher 0.0.1 BETA

args[0] = A path to a directory containing some files.
args[1] = The original text or pattern to use for searching in the files
args[2] = A new text string which will replace the original text or pattern
args[3] = Path to a file for outputting a list of which files were modified.

All parameters are mandatory

Example of usage:
groovy Search.groovy /home/carlos/testSearcher XXXX house modifiedFiles.txt

Logging:
All the output goes to standard output so it is very easy to redirect it to a file
just execute command like on the example and add ">> anyFile.log" at the end

 */

import groovy.io.FileType
import groovy.time.TimeCategory
import groovy.time.TimeDuration


def start = new Date()
def format = "yyyyMMdd-HH:mm:ss.SSS"
println "Starting searcher script at "+ start.format(format)

def ant = new AntBuilder()
ant.project.getBuildListeners().firstElement().setMessageOutputLevel(3)
def modifiedFiles = new File(args[3])
def dir = new File(args[0])
dir.eachFileRecurse (FileType.FILES) { file ->

    if(file.getText().findAll(args[1]) && !file.isDirectory()) {
        //will create backup file on same directory as the original file
        ant.copy(file: file, tofile: new File(file.getAbsolutePath() + ".backup").write(file.getText()))
        ant.replace(file: file, token: args[1], value: args[2], excludes: ".*.backup", summary: "true")
        modifiedFiles.append(file.getAbsolutePath() + "\n")
    }
}
def end = new Date()
println "Searcher script ended at "+ end.format(format)
TimeDuration duration = TimeCategory.minus( end, start )
println "Script took "+ duration + " to complete "

