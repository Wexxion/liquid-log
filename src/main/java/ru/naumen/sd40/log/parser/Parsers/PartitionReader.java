package ru.naumen.sd40.log.parser.Parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Pattern;

public class PartitionReader {
    private final Pattern PartitionStartPattern;
    private final Iterator<String> LogIterator;
    private String cachedLine = null;

    public PartitionReader(Path logFilepath, Pattern partitionStartPattern) throws IOException {
        PartitionStartPattern = partitionStartPattern;
        LogIterator = Files.lines(logFilepath).iterator();
    }

    public String GetNextPart() {
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
