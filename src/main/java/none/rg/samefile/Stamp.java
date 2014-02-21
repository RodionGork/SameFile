package none.rg.samefile;

public class Stamp {
    
    private long size;
    private int hash;
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long n) {
        size = n;
    }
    
    public int getHash() {
        return hash;
    }
    
    public void setHash(int n) {
        hash = n;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stamp)) {
            return false;
        }
        Stamp stamp = (Stamp) o;
        return stamp.size == size && stamp.hash == hash;
    }
    
    @Override
    public int hashCode() {
        return (int) (size ^ (size >> 32)) ^ hash;
    }
    
}

