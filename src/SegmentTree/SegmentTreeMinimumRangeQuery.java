package SegmentTree;


public class SegmentTreeMinimumRangeQuery {
    private int segmentTree[];
    private int lazy[];
    private int input[];

    private SegmentTreeMinimumRangeQuery(int[] input) {
        input = input;
        segmentTree = createSegmentTree();
        lazy = new int[segmentTree.length];

    }

    private int nextPowerOf2(int num) {
        if (num == 0) {
            return 1;
        }
        if (num > 0 && (num & (num - 1)) == 0) {
            return num;
        }
        while ((num & (num - 1)) > 0) {
            num = num & (num - 1);
        }
        return num << 1;
    }

    private int[] createSegmentTree() {
        //if input len is pow of 2 then size of
        //segment tree is 2*len - 1, otherwise
        //size of segment tree is next (pow of 2 for len)*2 - 1.
        int nextPowOfTwo = nextPowerOf2(input.length);
        int segmentTree[] = new int[nextPowOfTwo * 2 - 1];

        for (int i = 0; i < segmentTree.length; i++) {
            segmentTree[i] = Integer.MAX_VALUE;
        }
        constructMinSegmentTree(0, input.length - 1, 0);
        return segmentTree;
    }

    private void updateSegmentTree(int index, int delta) {
        input[index] += delta;
        updateSegmentTree(index, delta, 0, input.length - 1, 0);
    }

    private void updateSegmentTreeRange(int startRange, int endRange, int delta) {
        for (int i = startRange; i <= endRange; i++) {
            input[i] += delta;
        }
        updateSegmentTreeRange(startRange, endRange, delta, 0, input.length - 1, 0);
    }

    private int rangeMinimumQuery(int qlow, int qhigh) {
        return rangeMinimumQuery(0, input.length - 1, qlow, qhigh, 0);
    }

    private void updateSegmentTreeRangeLazy(int startRange, int endRange, int delta) {
        updateSegmentTreeRangeLazy(startRange, endRange, delta, 0, input.length - 1, 0);
    }

    private int rangeMinimumQueryLazy(int qlow, int qhigh) {
        return rangeMinimumQueryLazy(qlow, qhigh, 0, input.length - 1, 0);
    }

    private void constructMinSegmentTree(int low, int high, int pos) {
        if (low == high) {
            segmentTree[pos] = input[low];
            return;
        }
        int mid = (low + high) / 2;
        constructMinSegmentTree(low, mid, 2 * pos + 1);
        constructMinSegmentTree(mid + 1, high, 2 * pos + 2);
        segmentTree[pos] = Math.min(segmentTree[2 * pos + 1], segmentTree[2 * pos + 2]);
    }

    private void updateSegmentTree(int index, int delta, int low, int high, int pos) {

        //if index to be updated is less than low or higher than high just return.
        if (index < low || index > high) {
            return;
        }

        //if low and high become equal, then index will be also equal to them and update
        //that value in segment tree at pos
        if (low == high) {
            segmentTree[pos] += delta;
            return;
        }
        //otherwise keep going left and right to find index to be updated
        //and then update current tree position if min of left or right has
        //changed.
        int mid = (low + high) / 2;
        updateSegmentTree(index, delta, low, mid, 2 * pos + 1);
        updateSegmentTree(index, delta, mid + 1, high, 2 * pos + 2);
        segmentTree[pos] = Math.min(segmentTree[2 * pos + 1], segmentTree[2 * pos + 2]);
    }

    private void updateSegmentTreeRange(int startRange, int endRange, int delta, int low, int high, int pos) {
        if (low > high || startRange > high || endRange < low) {
            return;
        }

        if (low == high) {
            segmentTree[pos] += delta;
            return;
        }

        int middle = (low + high) / 2;
        updateSegmentTreeRange(startRange, endRange, delta, low, middle, 2 * pos + 1);
        updateSegmentTreeRange(startRange, endRange, delta, middle + 1, high, 2 * pos + 2);
        segmentTree[pos] = Math.min(segmentTree[2 * pos + 1], segmentTree[2 * pos + 2]);
    }

    private int rangeMinimumQuery(int low, int high, int qlow, int qhigh, int pos) {
        if (qlow <= low && qhigh >= high) {
            return segmentTree[pos];
        }
        if (qlow > high || qhigh < low) {
            return Integer.MAX_VALUE;
        }
        int mid = (low + high) / 2;
        return Math.min(rangeMinimumQuery(low, mid, qlow, qhigh, 2 * pos + 1),
                rangeMinimumQuery(mid + 1, high, qlow, qhigh, 2 * pos + 2));
    }

    private void updateSegmentTreeRangeLazy(int startRange, int endRange, int delta, int low, int high, int pos) {
        if (low > high) {
            return;
        }

        //make sure all propagation is done at pos. If not update tree
        //at pos and mark its children for lazy propagation.
        if (lazy[pos] != 0) {
            segmentTree[pos] += lazy[pos];
            if (low != high) { //not a leaf node
                lazy[2 * pos + 1] += lazy[pos];
                lazy[2 * pos + 2] += lazy[pos];
            }
            lazy[pos] = 0;
        }

        //no overlap condition
        if (startRange > high || endRange < low) {
            return;
        }

        //total overlap condition
        if (startRange <= low && endRange >= high) {
            segmentTree[pos] += delta;
            if (low != high) {
                lazy[2 * pos + 1] += delta;
                lazy[2 * pos + 2] += delta;
            }
            return;
        }

        //otherwise partial overlap so look both left and right.
        int mid = (low + high) / 2;
        updateSegmentTreeRangeLazy(startRange, endRange, delta, low, mid, 2 * pos + 1);
        updateSegmentTreeRangeLazy(startRange, endRange, delta, mid + 1, high, 2 * pos + 2);
        segmentTree[pos] = Math.min(segmentTree[2 * pos + 1], segmentTree[2 * pos + 2]);
    }

    private int rangeMinimumQueryLazy(int qlow, int qhigh, int low, int high, int pos) {

        if (low > high) {
            return Integer.MAX_VALUE;
        }

        //make sure all propagation is done at pos. If not update tree
        //at pos and mark its children for lazy propagation.
        if (lazy[pos] != 0) {
            segmentTree[pos] += lazy[pos];
            if (low != high) { //not a leaf node
                lazy[2 * pos + 1] += lazy[pos];
                lazy[2 * pos + 2] += lazy[pos];
            }
            lazy[pos] = 0;
        }

        //no overlap
        if (qlow > high || qhigh < low) {
            return Integer.MAX_VALUE;
        }

        //total overlap
        if (qlow <= low && qhigh >= high) {
            return segmentTree[pos];
        }

        //partial overlap
        int mid = (low + high) / 2;
        return Math.min(rangeMinimumQueryLazy(qlow, qhigh, low, mid, 2 * pos + 1),
                rangeMinimumQueryLazy(qlow, qhigh, mid + 1, high, 2 * pos + 2));

    }

    public static void main(String args[]) {
        int input[] = {0, 3, 4, 2, 1, 6, -1};
        SegmentTreeMinimumRangeQuery st = new SegmentTreeMinimumRangeQuery(input);

        //non lazy propagation example
        assert 0 == st.rangeMinimumQuery(0, 3);
        assert 1 == st.rangeMinimumQuery(1, 5);
        assert -1 == st.rangeMinimumQuery(1, 6);
        st.updateSegmentTree(2, 1);
        assert 2 == st.rangeMinimumQuery(1, 3);
        st.updateSegmentTreeRange(3, 5, -2);
        assert -1 == st.rangeMinimumQuery(5, 6);
        assert 0 == st.rangeMinimumQuery(0, 3);

        //lazy propagation example
        int input1[] = {-1, 2, 4, 1, 7, 1, 3, 2};
        st = new SegmentTreeMinimumRangeQuery(input1);
        st.updateSegmentTreeRangeLazy(0, 3, 1);
        st.updateSegmentTreeRangeLazy(0, 0, 2);
        assert 1 == st.rangeMinimumQueryLazy(3, 5);
    }
}
