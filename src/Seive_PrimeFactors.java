import java.util.ArrayList;
import java.util.HashMap;

public class Seive_PrimeFactors {

    public static boolean[] seive = new boolean[1000000];

    private static ArrayList<Integer> seivegen() {
        ArrayList<Integer> primes = new ArrayList<>();
        seive[0] = seive[1] = true;
        for (long i = 2; i < seive.length; i++) {
            if (!seive[(int) i]) {
                for (long j = i * i; j < seive.length; j += i) {
                    seive[(int) j] = true;
                }
                primes.add((int) i);
            }
        }
        return primes;
    }

    private static HashMap<Integer, Integer> primefactor(int val, ArrayList<Integer> primes) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int idx = 0;
        while (primes.get(idx) * primes.get(idx) <= val) {
            while (val % primes.get(idx) == 0) {
                val /= primes.get(idx);
                if (map.containsKey(primes.get(idx))) {
                    map.put(primes.get(idx), map.get(primes.get(idx)) + 1);
                } else {
                    map.put(primes.get(idx), 1);
                }
            }
            idx++;
        }

        if (val != 1) {
            map.put(val, 1);
        }
        return map;
    }

}
