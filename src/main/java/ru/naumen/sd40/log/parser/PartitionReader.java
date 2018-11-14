package ru.naumen.sd40.log.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Pattern;

class PartitionReader {
    private final Pattern PartitionStartPattern;
    private final Iterator<String> LogIterator;
    private String cachedLine = null;

    PartitionReader(Path logFilepath, Pattern partitionStartPattern) throws IOException {
        PartitionStartPattern = partitionStartPattern;
        LogIterator = Files.lines(logFilepath).iterator();
    }

    String GetNextPart() {
        StringBuilder sb = new StringBuilder();
        String line;

        if(LogIterator.hasNext()){
            boolean partFound = false;
            line = cachedLine == null ? LogIterator.next() : cachedLine;
            if (PartitionStartPattern.matcher(line).find()) {
                partFound = true;
                sb.append(line).append('\n');
                while (LogIterator.hasNext()){
                    line = LogIterator.next();
                    if(!PartitionStartPattern.matcher(line).find())
                        sb.append(line).append('\n');
                    else {
                        cachedLine = line;
                        break;
                    }
                }
            }
            if(partFound)
                return sb.toString();

            cachedLine = null;
            return line;
        }
        return null;
    }
}
