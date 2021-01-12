package olimpiadaInfLiceum;

public class TestyOlimpiadaInfLiceum {
// ZADANIE Z PLIKU cuk.pdf - TESTY

    public static int[][] TAB1 = {
            {1, 1, 2},
            {2, 1, 1},
            {1, 1, 2}
    };

    public static int[][] TAB2 = new int[5][3];

    public static int[][] TAB3 = new int[1000][3];

    public static int[][] TAB4 = new int[300000][3];

    TestyOlimpiadaInfLiceum() {
        for (int i = 0; i < TAB2.length; i++) {
            for (int j = 0; j < TAB2[0].length; j++) {
                TAB2[i][j] = 5;
            }
        }

        for (int i = 0; i < TAB3.length; i++) {
            if (i%2==0) {
                TAB3[i][0] = 10;
            }
            else {
                TAB3[i][2] = 10;
            }
        }

        for (int i = 0; i < TAB4.length; i++) {
            TAB4[i][0] = 3;
            TAB4[i][1] = 2;
            TAB4[i][2] = 1;
        }
    }




}
