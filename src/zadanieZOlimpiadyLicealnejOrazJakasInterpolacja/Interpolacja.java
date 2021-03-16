package olimpiadaInfLiceum;

public class Interpolacja {

    static void rekurencja(int n, double[] x, double[] y, double[][] tab, int k) {
        for (int i = 0; i < n-k; i++) {
            tab[k][i] = (tab[k-1][i+1]-tab[k-1][i])/(x[i+k]-x[i]);
        }
        if (k<n) {
            rekurencja(n, x, y, tab, k+1);
        }
    }

    static void newton(int n, double[] x, double[] y, double XXX) {
        double[][] tab = new double[n][n];

        // uzupelnienie rzędów zerowych
        for (int i = 0; i < n; i++) {
            tab[0][i]=y[i];
        }
        rekurencja(n, x, y, tab, 1);

        // Współczynniki
        System.out.print("Wspolczynniki z funkcji: ");
        for (int i = 0; i < n; i++) {
            System.out.print(tab[i][0] + ", ");
        }
        System.out.println();

        double result = 0;
        for (int i = 0; i < n; i++) {
            double tmp = tab[i][0];
            for (int k = 0; k < i; k++){
                tmp *= (XXX-x[k]);
            }
            result += tmp;
        }
        System.out.println("Wynik z funkcji: " + result);

        // System.out.println(tab[0][0] + tab[1][0]*(50-x[0]) + tab[2][0]*(50-x[0])*(50-x[1]) + tab[3][0]*(50-x[0])*(50-x[1])*(50-x[2]));
    }

    public static void main(String[] args) {
        double[] y = {3, 4, 5, 6, 7, 8};
        double[] x = new double[y.length];

        for (int i = 0; i < x.length; i++) {
            x[i] = Math.pow(y[i],3);
        }

        newton(x.length, x, y, 50);

        double F01 = (y[1]-y[0])/(x[1]-x[0]);
        double F12 = (y[2]-y[1])/(x[2]-x[1]);
        double F23 = (y[3]-y[2])/(x[3]-x[2]);
        double F012 = (F12-F01)/(x[2]-x[0]);
        double F123 = (F23-F12)/(x[3]-x[1]);
        double F0123 = (F123-F012)/(x[3]-x[0]);
        double res = y[0] + F01*(50-x[0]) + F012*(50-x[0])*(50-x[1]) + F0123*(50-x[0])*(50-x[1])*(50-x[2]);

        System.out.print("\nWspolczynniki ręcznie: ");
        System.out.print(y[0] + ", " + F01 + ", " + F012 + ", " + F0123 + ", ");
        System.out.println();
        System.out.println("Wynik na piechote(ręcznie) liczony: " + res);
        System.out.println("\nWYNIK IDEALNY: " + Math.pow(50, 1.0/3));
        System.out.println("\nWYNIK IDEALNY: " + F0123 + " " + (3+F0123));

        System.out.println("\nNIE DZIALA: " + Wn(x.length-1, x, y, 50));

//        System.out.println("\nEEE MLEKO: ");
//        System.out.println("ALA MA KOTA: " + ((y[0]/(x[0]-x[1]))+(y[1]/(x[1]-x[0]))));
//        System.out.println((3.0/((27-64)*(27-125)*(27-216))) + " " + (4.0/((64-27)*(64-125)*(64-216))) + " " + (5.0/((125-27)*(125-64)*(125-216))) + " " + (6.0/((216-27)*(216-64)*(216-125))));
//        System.out.println(((3.0/((27-64)*(27-125)*(27-216)))+(4.0/((64-27)*(64-125)*(64-216))))+(5.0/((125-27)*(125-64)*(125-216)))+(6.0/((216-27)*(216-125)*(216-64))));
    }

    static double Bk(int k, double[] x, double[] y) {
        double result = 0;
        for (int i = 0; i <= k; i++) {
            double tmp = 1;
            for (int j = 0; j <= k; j++) {
                if (i != j) {
                    tmp *= x[i] - x[j];
                }
            }
            result += y[i]/tmp;
        }
        return result;
    }

    static double Pk(int k, double[] x, double[] y, double XXX) {
        double result = 1;
        for (int i = 0; i < k; i++) {
            result *= (XXX-x[i]);
        }
        return result;
    }

    static double Wn(int n, double[] x, double[] y, double XXX) {
        double result = y[0];
        for (int k = 1; k <= n; k++) {
            result += Bk(k, x, y) * Pk(k, x, y, XXX);
        }
        return result;
    }
}