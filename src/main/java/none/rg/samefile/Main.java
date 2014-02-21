package none.rg.samefile;

import java.util.*;
import java.io.*;

public class Main {
    
    private List<String> dirs;
    
    private Set<String> visited;
    
    private Map<String, Stamp> files;
    
    Main(String... dirs) {
        this.dirs = new ArrayList<>(Arrays.asList(dirs));
        files = new HashMap<>();
        visited = new HashSet<>();
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
        
        Collection<String> same = findSame();
        printSame(same);
    }
    
    Collection<String> findSame() {
        Map<Stamp, String> map = new HashMap<>();
        for (Map.Entry<String, Stamp> e : files.entrySet()) {
            String name = e.getKey();
            Stamp stamp = e.getValue();
            String exists = map.get(stamp);
            if (exists == null) {
                map.put(stamp, name);
            } else {
                map.put(stamp, exists + "\n" + name);
            }
        }
        return map.values();
    }
    
    void printSame(Collection<String> same) {
        System.out.println();
        double total = 0;
        for (String entry : same) {
            String[] names = entry.split("\\n");
            if (names.length > 1) {
                double size = files.get(names[0]).getSize() / (1024.0 * 1024);
                total += (names.length - 1) * size;
                System.out.printf("%d files of %.2f Mb = %.2f Mb\n", names.length, size, names.length * size);
                System.out.println(entry);
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
            }
            addFile(f);
        }
    }
    
    void addFile(File f) {
        String name = getCanonicalPath(f);
        Stamp stamp = new Stamp();
        stamp.setSize(f.length());
        files.put(name, stamp);
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

