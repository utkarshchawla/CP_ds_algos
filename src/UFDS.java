
public class UFDS {
    private int[] parent;
    private int[] rank;
    private int[] size;
    private int numSets;

    public UFDS(int n) {
        this.parent = new int[n];
        this.rank = new int[n];
        this.size = new int[n];
        this.numSets = n;
        for (int i = 0; i < parent.length; i++) {
            size[i] = 1;
            parent[i] = i;
        }
    }

    private int findSet(int i) {
        return (parent[i] == i) ? i : (parent[i] = findSet(parent[i]));

    }

    private boolean isSameSet(int i, int j) {
        return findSet(i) == findSet(j);
    }

    public void unionSet(int i, int j) {
        if (isSameSet(i, j)) return;
        numSets--;
        int x = findSet(i);
        int y = findSet(j);
        if (rank[x] > rank[y]) {
            parent[y] = x;
            size[x] += size[y];
        } else {
            parent[x] = y;
            size[y] += size[x];
            if (rank[x] == rank[y]) rank[y]++;
        }
    }

    public int getNumSets() {
        return numSets;
    }

    public int getSize(int i) {
        return size[findSet(i)];
    }

    public static void main(String[] args) {
    }
}
