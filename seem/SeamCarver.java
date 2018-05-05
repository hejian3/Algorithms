import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import java.util.Arrays;

/**
 * Created by hejian on 2018/4/28.
 */
public class SeamCarver {

    private static final int ENGRY_BORDER = 1000;
    private Picture picture;
    private double[][]energyArr;
    private int [] horizontalSeam;
    private int [] verticalSeam;

    /**
     * create a seam carver object based on the given picture
     * @param picture
     */
    public SeamCarver(Picture picture) {
        if(picture == null) throw new IllegalArgumentException("a null argument");
        this.picture = new Picture(picture);
        setup();
    }

    private void setup(){
        energyArr = new double[picture.height()][picture.width()];
        setupEnergy();
        horizontalSeam = new int[picture.width()];
        verticalSeam   = new int[picture.height()];
        if(width() > 1 && height() > 1) {
            setupHorizontalSeam();
            setupVerticalSeam();
        }
    }

    private void setupEnergy(){
        for(int i = 0; i < picture.height(); i++){
            for(int j = 0; j < picture.width(); j++){
                energyArr[i][j] = getEnergy(j, i);
            }
        }
    }

    private void setupHorizontalSeam() {
        final double[][] distTo = new double[width()][height()];
        final int[][] edgeTo = new int[width()][height()];
        for(int i = 0; i< width();i++) {
            for (int j = 0; j < height(); j++) {
                if(i == 0) {
                    distTo[i][j] = ENGRY_BORDER;
                    edgeTo[i][j] = j;
                }else {
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for(int i = 0; i< width(); i++) {
            for (int j = 0; j < height(); j++) {

                if(i < width() -1 && distTo[i+1][j] > distTo[i][j] + energy(i+1,j)){
                    distTo[i+1][j] = distTo[i][j] + energy(i+1,j);
                    edgeTo[i+1][j] = j;
                }

                if(i < width() -1 && j > 0 && distTo[i+1][j-1] > distTo[i][j] + energy(i+1,j-1)){
                    distTo[i+1][j-1] = distTo[i][j] + energy(i+1,j-1);
                    edgeTo[i+1][j-1] = j;
                }

                if(i < width()-1 && j < height()-1 && distTo[i+1][j+1] > distTo[i][j] + energy(i+1,j+1)){
                    distTo[i+1][j+1] = distTo[i][j] + energy(i+1,j+1);
                    edgeTo[i+1][j+1] = j;
                }
            }
        }

        double result = Double.POSITIVE_INFINITY;
        int resultIndex = -1;
        for(int i = 0; i< height(); i++){
            if(result > distTo[width()-1][i]){
                resultIndex = i;
                result = distTo[width()-1][i];
            }
        }
        horizontalSeam[width()-1] = resultIndex;
        for (int i = width()-2; i >= 0; i--){
            horizontalSeam[i] = edgeTo[i+1][horizontalSeam[i+1]];
        }
    }

    private void setupVerticalSeam() {
        final double[][] distTo = new double[height()][width()];
        final int[][] edgeTo = new int[height()][width()];
        for(int i = 0; i< height();i++) {
            for (int j = 0; j < width(); j++) {
                if(i == 0) {
                    distTo[i][j] = ENGRY_BORDER;
                    edgeTo[i][j] = j;
                }else {
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for(int i = 0; i< height(); i++) {
            for (int j = 0; j < width(); j++) {

                if(i < height()-1 && distTo[i+1][j] > distTo[i][j] + energy(j,i+1)){
                    distTo[i+1][j] = distTo[i][j] + energy(j,i+1);
                    edgeTo[i+1][j] = j;
                }

                if(j > 0 && i < height()-1 && distTo[i+1][j-1] > distTo[i][j] + energy(j-1,i+1)){
                    distTo[i+1][j-1] = distTo[i][j] + energy(j-1,i+1);
                    edgeTo[i+1][j-1] = j;
                }

                if(i < height()-1 && j < width()-1 && distTo[i+1][j+1] > distTo[i][j] + energy(j+1,i+1)){
                    distTo[i+1][j+1] = distTo[i][j] + energy(j+1,i+1);
                    edgeTo[i+1][j+1] = j;
                }
            }
        }

        double result = Double.POSITIVE_INFINITY;
        int resultIndex = -1;
        for(int i = 0; i< width(); i++){
            if(result > distTo[height()-1][i]){
                result = distTo[height()-1][i];
                resultIndex = i ;
            }
        }

        verticalSeam[height()-1] = resultIndex;
        for (int i = height()-2; i >= 0; i--){
            verticalSeam[i] = edgeTo[i+1][verticalSeam[i+1]];
        }

        verticalSeam[height()-1] = verticalSeam[height()-2];
    }

    private boolean isBorder(int column,int row){
        return column == 0 || column == width() - 1 || row == 0 || row == height() - 1;
    }

    private double getEnergy(int column, int row){
        if(isBorder(column, row))return ENGRY_BORDER;
        Color cLeft = picture.get(column -1 ,row);
        Color cRight = picture.get(column+1, row);
        Color rTop = picture.get(column, row - 1);
        Color rBottom = picture.get(column,row+1);

        double result = Math.pow((cRight.getRed() - cLeft.getRed()),2) + Math.pow((cRight.getGreen() - cLeft.getGreen()),2)
                + Math.pow((cRight.getBlue() - cLeft.getBlue()),2) + Math.pow((rBottom.getRed() - rTop.getRed()),2)
                + Math.pow((rBottom.getGreen() - rTop.getGreen()),2) + Math.pow((rBottom.getBlue() - rTop.getBlue()),2);
        return Math.sqrt(result);
    }

    /**
     * current picture
     * @return
     */
    public Picture picture() {
        return new Picture(picture);
    }

    /**
     * width of current picture
     * @return
     */
    public int width() {
        return picture.width();
    }

    /**
     * height of current picture
     * @return
     */
    public int height() {
        return picture.height();
    }

    /**
     * energy of pixel at column x and row y
     * @param col
     * @param row
     * @return
     */
    public double energy(int col, int row) {
        if(col < 0 || col >= width() || row < 0 || row >= height())
            throw new java.lang.IllegalArgumentException("either x or y is outside its prescribed range");
        return energyArr[row][col];
    }

    /**
     * sequence of indices for horizontal seam
     * @return
     */
    public int[] findHorizontalSeam() {
        return Arrays.copyOf(horizontalSeam,horizontalSeam.length);
    }

    /**
     * sequence of indices for vertical seam
     * @return
     */
    public int[] findVerticalSeam() {
        return Arrays.copyOf(verticalSeam,verticalSeam.length);
    }

    /**
     * remove horizontal seam from current picture
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        if(seam == null) throw new IllegalArgumentException("a null argument");
        if(seam.length > width() || seam.length <=0)throw new IllegalArgumentException("outside its prescribed range ");
        if(height() <= 1)throw new IllegalArgumentException("outside its prescribed range ");
        for(int i =0; i < seam.length; i++){
            if(seam[i] < 0 || seam[i] >= height())throw new IllegalArgumentException("outside its prescribed range");
        }
        Picture p = new Picture(width(),height() -1);
        for(int i =0; i < seam.length ; i++){
            int index = 0;
            for(int j = 0; index < p.height(); j++){
                if(seam[i] == j){
                    continue;
                }
                p.set(i,index,picture.get(i,j));
                index++;
            }
        }
        this.picture = p;
        setup();
    }

    /**
     * remove vertical seam from current picture
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        if(seam == null) throw new IllegalArgumentException("a null argument");
        if(seam.length > height() || seam.length <=0)throw new IllegalArgumentException("outside its prescribed range ");
        if(width() <= 1)throw new IllegalArgumentException("outside its prescribed range ");
        for(int i =0; i < seam.length; i++){
            if(seam[i] < 0 || seam[i] >= width())throw new IllegalArgumentException("outside its prescribed range");
        }
        Picture p = new Picture(width() -1 , height());
        for (int i = 0; i < seam.length; i++){
            int index = 0;
            for(int j = 0; j < width(); j++){
                if(seam[i] == j){
                    continue;
                }
                p.set(index,i,picture.get(j,i));
                index++;
            }
        }
        this.picture = p;
        setup();
    }

    public static void main(String [] args) {
        Picture picture = new Picture("C:\\Users\\hejian\\Desktop\\seam-testing\\seam\\12x10.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        for (int i = 0; i < picture.width(); i++){
            for(int j = 0; j < picture.height(); j++){
                StdOut.println("engry("+i+","+j+")="+seamCarver.energy(i,j));
            }
        }

        if(seamCarver.horizontalSeam!=null) {
            StdOut.print("horizontalSeam={");
            for (int i = 0; i < seamCarver.horizontalSeam.length; i++) {
                StdOut.print(seamCarver.horizontalSeam[i] + ",");
            }
            StdOut.print("}");
        }
        if(seamCarver.verticalSeam!=null) {
            StdOut.print("verticalSeam={");
            for (int i = 0; i < seamCarver.verticalSeam.length; i++) {
                StdOut.print(seamCarver.verticalSeam[i] + ",");
            }
            StdOut.print("}");
        }
        SeamCarver seamCarver1 = new SeamCarver(picture);
        StdOut.println(seamCarver1.picture);
        seamCarver1.removeHorizontalSeam(seamCarver1.horizontalSeam);
        StdOut.println(seamCarver1.picture);
        SeamCarver seamCarver2 = new SeamCarver(picture);
        StdOut.println(seamCarver2.picture);
        seamCarver2.removeVerticalSeam(seamCarver2.verticalSeam);
        StdOut.println(seamCarver2.picture);
    }
}
