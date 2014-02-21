package none.rg.samefile;

import java.util.*;
import java.io.*;

public class Main {
    
    private List<String> dirs;
    
    private Set<String> visited;
    
    private Processor proc;
    
    Main(String... dirs) {
        this.dirs = new ArrayList<>(Arrays.asList(dirs));
        visited = new HashSet<>();
        proc = new Processor();
    }
    
    void run() {
        for (String name : dirs) {
            File file = new File(name);
            if (!file.isDirectory()) {
                System.err.println("Error: '" + name + "' is not a directory!");
                continue;
            }
            parseDir(file);
        }
        
        printSame(proc.findSame());
    }
    
    void printSame(Map<Stamp, List<String>> same) {
        System.out.println();
        double total = 0;
        for (Map.Entry<Stamp, List<String>> entry : same.entrySet()) {
            Stamp stamp = entry.getKey();
            List<String> names = entry.getValue();
            if (names.size() > 1) {
                double size = stamp.getSize() / (1024.0 * 1024);
                total += (names.size() - 1) * size;
                System.out.printf("%d files of %.2f Mb = %.2f Mb\n", names.size(), size, names.size() * size);
                for (String s : names) {
                    System.out.println(s);
                }
                System.out.println();
            }
        }
        System.out.println("Total waste: " + (int) total);
    }
    
    void parseDir(File dir) {
        if (!checkAndMarkVisited(dir)) {
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                parseDir(f);
            } else {
                proc.addFile(getCanonicalPath(f), f.length());
            }
        }
    }
    
    boolean checkAndMarkVisited(File dir) {
        String path = getCanonicalPath(dir);
        if (!visited.add(path)) {
            return false;
        }
        return true;
    }
    
    String getCanonicalPath(File f) {
        try {
            return f.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String... args) {
        new Main(args).run();
    }

}

