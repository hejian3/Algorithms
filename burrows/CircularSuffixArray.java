import java.util.Arrays;

public class CircularSuffixArray {

    private final String s;

    private final Suffix [] suffixes;

    /**
     * circular suffix array of s
     * @param s
     */
    public CircularSuffixArray(String s){

        if (s == null) throw new IllegalArgumentException("null argument");

        this.s = s;

        suffixes  = new Suffix[length()];

        for(int i =0;i<length();i++){
            suffixes[i] = new Suffix(s,i);
        }
        Arrays.sort(suffixes);
    }

    private static class Suffix implements Comparable<Suffix> {
        private final String text;
        private final int index;

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        public int compareTo(Suffix that) {
            if (this == that) return 0;  // optimization
            for (int i = 0; i < text.length(); i++){
                char c1 = text.charAt((this.index+i)%text.length());
                char c2 = text.charAt((that.index+i)%text.length());
                if(c1 < c2){
                    return -1;
                }else if( c1 > c2){
                    return 1;
                }
            }
            return (int)Math.signum(that.index - this.index);
        }

        @Override
        public String toString() {
            return String.valueOf(text);
        }
    }

    /**
     * length of s
     * @return
     */
    public int length(){
        return s.length();
    }


    /**
     * returns index of ith sorted suffix
     * @param i
     * @return
     */
    public int index(int i){
        if (i < 0 || i >= suffixes.length) throw new IllegalArgumentException();
        return suffixes[i].index;
    }

    /**
     * unit testing (required)
     * @param args
     */
    public static void main(String[] args){
        new CircularSuffixArray(args[0]);
    }
}
