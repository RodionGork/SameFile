package none.rg.samefile;

import java.util.*;
import java.io.*;

public class Processor {

    private Map<String, Stamp> files;
    private Map<Stamp, List<String>> same;
    
    public Processor() {
        files = new HashMap<>();
    }
    
    public void addFile(String name, long size) {
        Stamp stamp = new Stamp();
        stamp.setSize(size);
        files.put(name, stamp);
    }
    
    public Map<Stamp, List<String>> findSame() {
        group();
        calcHashes();
        group();
        return filterSame();
    }
    
    private Map<Stamp, List<String>> filterSame() {
        Map<Stamp, List<String>> res = new HashMap<>();
        for (Map.Entry<Stamp, List<String>> entry : same.entrySet()) {
            if (entry.getValue().size() > 1) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        return res;
    }
    
    private void group() {
        same = new HashMap<>();
        for (Map.Entry<String, Stamp> e : files.entrySet()) {
            String name = e.getKey();
            Stamp stamp = e.getValue();
            List<String> list = same.get(stamp);
            if (list == null) {
                list = new ArrayList<>();
                same.put(stamp, list);
            }
            list.add(name);
        }
    }
    
    private void calcHashes() {
        for (List<String> list : same.values()) {
            if (list.size() < 2) {
                continue;
            }
            for (String name : list) {
                files.get(name).setHash(calcHash(name));
            }
        }
    }
    
    private int calcHash(String name) {
        try (InputStream input = new FileInputStream(name)) {
            return calcHash(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private int calcHash(InputStream input) throws IOException {
        byte[] buf = new byte[8192];
        int hash = 13;
        while (true) {
            int res = input.read(buf);
            if (res < 1) {
                break;
            }
            for (int i = 0; i < res; i++) {
                hash = hash * 17 + buf[i];
            }
        }
        return hash;
    }
    
}
