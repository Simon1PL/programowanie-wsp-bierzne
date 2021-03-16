package olimpiadaInfLiceum;

public class OlimpiadaInfLiceum {
// ZADANIE Z PLIKU cuk.pdf

    public static void main(String[] args) {
        int N = 5;
        // int TAB[][] = new int[N][3];
        int TAB[][] = {
                {5, 1, 1},
                {0, 3, 4},
                {1, 4, 3},
                {4, 0, 0},
                {0, 0, 0}
        };

        TestyOlimpiadaInfLiceum testyOlimpiadaInfLiceum = new TestyOlimpiadaInfLiceum();
        TAB = TestyOlimpiadaInfLiceum.TAB1;
        N = TAB.length;


        int suma = 0;
        int rzedyZerowe = 0;

        int ileNiePrzekladamy = 0;
        int[] ileJestPulekZTym = new int[3];
        int[] nrPułkiZTymProduktem = new int[3];

        int maxymalnaIloscProduktów = (int)Math.pow(10, 9);
        int[] minimalneRóżniceDoMaxaWRzędzie = {maxymalnaIloscProduktów+1, maxymalnaIloscProduktów+1, maxymalnaIloscProduktów+1};
        int[] półkiMinimalnychRóżnic = new int[3];
        int[] drugieMinimalneRóżniceDoMaxaWRzędzie = {maxymalnaIloscProduktów+1, maxymalnaIloscProduktów+1, maxymalnaIloscProduktów+1};
        int[] ilosciPoszczególnychProduktów = new int[3];

        // PROGRAM
        for(int n = 0; n < N; n++) {
            suma += TAB[n][0] + TAB[n][1] + TAB[n][2];
            ilosciPoszczególnychProduktów[0] += TAB[n][0];
            ilosciPoszczególnychProduktów[1] += TAB[n][1];
            ilosciPoszczególnychProduktów[2] += TAB[n][2];

            // RZEDY ZEROWE
            if (TAB[n][0] == 0 && TAB[n][1] == 0 && TAB[n][2] == 0) {
                rzedyZerowe ++;
            }
            // 1. produktów '0' jest najwięcej w rzędzie n:
            if (TAB[n][0] >= TAB[n][1] && TAB[n][0] >= TAB[n][2]) {
                ileJestPulekZTym[0] += 1;
                nrPułkiZTymProduktem[0] = n;

                int maxWRzedzie = TAB[n][0];
                TAB[n][0] = maxWRzedzie - TAB[n][0]; // różnice miedzy maxem w rzędzie a iloscia danego produktu(czyli ile im brakowalo do maxa)
                TAB[n][1] = maxWRzedzie - TAB[n][1];
                TAB[n][2] = maxWRzedzie - TAB[n][2];
                ileNiePrzekladamy += maxWRzedzie;

                if (TAB[n][1] < drugieMinimalneRóżniceDoMaxaWRzędzie[1]) { // czy jest jednym z 2 najmniejszych różnic
                    if (TAB[n][1] < minimalneRóżniceDoMaxaWRzędzie[1]) { // czy jest najmniejsza różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[1] = minimalneRóżniceDoMaxaWRzędzie[1];
                        minimalneRóżniceDoMaxaWRzędzie[1] = TAB[n][1];
                        półkiMinimalnychRóżnic[1] = n;
                    }
                    else { // czy jest drugą najmniejszą różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[1] = TAB[n][1];
                    }
                }

                if (TAB[n][2] < drugieMinimalneRóżniceDoMaxaWRzędzie[2]) { // czy jest jednym z 2 najmniejszych różnic
                    if (TAB[n][2] < minimalneRóżniceDoMaxaWRzędzie[2]) { // czy jest najmniejsza różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[2] = minimalneRóżniceDoMaxaWRzędzie[2];
                        minimalneRóżniceDoMaxaWRzędzie[2] = TAB[n][2];
                        półkiMinimalnychRóżnic[2] = n;
                    }
                    else { // czy jest drugą najmniejszą różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[2] = TAB[n][2];
                    }
                }
            }

            // 2. produktów '1' jest najwięcej w rzędzie n:
            else if (TAB[n][1] >= TAB[n][2]) {
                ileJestPulekZTym[1] += 1;
                nrPułkiZTymProduktem[1] = n;

                int maxWRzedzie = TAB[n][1];
                TAB[n][0] = maxWRzedzie - TAB[n][0]; // różnice miedzy maxem w rzędzie a iloscia danego produktu(czyli ile im brakowalo do maxa)
                TAB[n][1] = maxWRzedzie - TAB[n][1];
                TAB[n][2] = maxWRzedzie - TAB[n][2];
                ileNiePrzekladamy += maxWRzedzie;

                if (TAB[n][0] < drugieMinimalneRóżniceDoMaxaWRzędzie[0]) { // czy jest jednym z 2 najmniejszych różnic
                    if (TAB[n][0] < minimalneRóżniceDoMaxaWRzędzie[0]) { // czy jest najmniejsza różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[0] = minimalneRóżniceDoMaxaWRzędzie[1];
                        minimalneRóżniceDoMaxaWRzędzie[0] = TAB[n][0];
                        półkiMinimalnychRóżnic[0] = n;
                    }
                    else { // czy jest drugą najmniejszą różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[0] = TAB[n][0];
                    }
                }

                if (TAB[n][2] < drugieMinimalneRóżniceDoMaxaWRzędzie[2]) { // czy jest jednym z 2 najmniejszych różnic
                    if (TAB[n][2] < minimalneRóżniceDoMaxaWRzędzie[2]) { // czy jest najmniejsza różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[2] = minimalneRóżniceDoMaxaWRzędzie[2];
                        minimalneRóżniceDoMaxaWRzędzie[2] = TAB[n][2];
                        półkiMinimalnychRóżnic[2] = n;
                    }
                    else { // czy jest drugą najmniejszą różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[2] = TAB[n][2];
                    }
                }
            }

            // 2. produktów '2' jest najwięcej w rzędzie n:
            else {
                ileJestPulekZTym[2] += 1;
                nrPułkiZTymProduktem[2] = n;

                int maxWRzedzie = TAB[n][2];
                TAB[n][0] = maxWRzedzie - TAB[n][0]; // różnice miedzy maxem w rzędzie a iloscia danego produktu(czyli ile im brakowalo do maxa)
                TAB[n][1] = maxWRzedzie - TAB[n][1];
                TAB[n][2] = maxWRzedzie - TAB[n][2];
                ileNiePrzekladamy += maxWRzedzie;

                if (TAB[n][0] < drugieMinimalneRóżniceDoMaxaWRzędzie[0]) { // czy jest jednym z 2 najmniejszych różnic
                    if (TAB[n][0] < minimalneRóżniceDoMaxaWRzędzie[0]) { // czy jest najmniejsza różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[0] = minimalneRóżniceDoMaxaWRzędzie[1];
                        minimalneRóżniceDoMaxaWRzędzie[0] = TAB[n][0];
                        półkiMinimalnychRóżnic[0] = n;
                    }
                    else { // czy jest drugą najmniejszą różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[0] = TAB[n][0];
                    }
                }

                if (TAB[n][1] < drugieMinimalneRóżniceDoMaxaWRzędzie[1]) { // czy jest jednym z 2 najmniejszych różnic
                    if (TAB[n][1] < minimalneRóżniceDoMaxaWRzędzie[1]) { // czy jest najmniejsza różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[1] = minimalneRóżniceDoMaxaWRzędzie[1];
                        minimalneRóżniceDoMaxaWRzędzie[1] = TAB[n][1];
                        półkiMinimalnychRóżnic[1] = n;
                    }
                    else { // czy jest drugą najmniejszą różnica
                        drugieMinimalneRóżniceDoMaxaWRzędzie[1] = TAB[n][1];
                    }
                }
            }
        }

        if (ilosciPoszczególnychProduktów[0] == 0) ileJestPulekZTym[0] = 2;
        if (ilosciPoszczególnychProduktów[1] == 0) ileJestPulekZTym[1] = 2;
        if (ilosciPoszczególnychProduktów[2] == 0) ileJestPulekZTym[2] = 2;

        // wynik jesli kazdy produkt ma swoja pułke
        if (ileJestPulekZTym[0] > 0 && ileJestPulekZTym[1] > 0 && ileJestPulekZTym[2] > 0) {
            System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
        }

        // wynik jesli 2 produkty nie maja swojej pułki
        // A. produkty '0' i '1' nie maja swojej pułki
        else if (ileJestPulekZTym[0] == 0 && ileJestPulekZTym[1] == 0) {
            if (rzedyZerowe >= 2) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
                return;
            }
            if (rzedyZerowe == 1) {
                int ileTrzebaWięcejPrzełożyć = Math.min(minimalneRóżniceDoMaxaWRzędzie[0], minimalneRóżniceDoMaxaWRzędzie[1]);
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
                return;
            }
            if (półkiMinimalnychRóżnic[0] != półkiMinimalnychRóżnic[1]) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + minimalneRóżniceDoMaxaWRzędzie[0] + minimalneRóżniceDoMaxaWRzędzie[1]));
            }
            else {
                int ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[0] + drugieMinimalneRóżniceDoMaxaWRzędzie[1];
                if (minimalneRóżniceDoMaxaWRzędzie[1] + drugieMinimalneRóżniceDoMaxaWRzędzie[0] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[1] + drugieMinimalneRóżniceDoMaxaWRzędzie[0];
                }
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
            }
        }
        // B. produkty '0' i '2' nie maja swojej pułki
        else if (ileJestPulekZTym[0] == 0 && ileJestPulekZTym[2] == 0) {
            if (rzedyZerowe >= 2) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
                return;
            }
            if (rzedyZerowe == 1) {
                int ileTrzebaWięcejPrzełożyć = Math.min(minimalneRóżniceDoMaxaWRzędzie[0], minimalneRóżniceDoMaxaWRzędzie[2]);
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
                return;
            }
            if (półkiMinimalnychRóżnic[0] != półkiMinimalnychRóżnic[2]) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + minimalneRóżniceDoMaxaWRzędzie[0] + minimalneRóżniceDoMaxaWRzędzie[2]));
            }
            else {
                int ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[0] + drugieMinimalneRóżniceDoMaxaWRzędzie[2];
                if (minimalneRóżniceDoMaxaWRzędzie[2] + drugieMinimalneRóżniceDoMaxaWRzędzie[0] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[2] + drugieMinimalneRóżniceDoMaxaWRzędzie[0];
                }
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
            }
        }
        // C. produkty '1' i '2' nie maja swojej pułki
        else if (ileJestPulekZTym[2] == 0 && ileJestPulekZTym[1] == 0) {
            if (rzedyZerowe >= 2) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
                return;
            }
            if (rzedyZerowe == 1) {
                int ileTrzebaWięcejPrzełożyć = Math.min(minimalneRóżniceDoMaxaWRzędzie[2], minimalneRóżniceDoMaxaWRzędzie[1]);
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
                return;
            }
            if (półkiMinimalnychRóżnic[2] != półkiMinimalnychRóżnic[1]) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + minimalneRóżniceDoMaxaWRzędzie[2] + minimalneRóżniceDoMaxaWRzędzie[1]));
            }
            else {
                int ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[2] + drugieMinimalneRóżniceDoMaxaWRzędzie[1];
                if (minimalneRóżniceDoMaxaWRzędzie[1] + drugieMinimalneRóżniceDoMaxaWRzędzie[2] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[1] + drugieMinimalneRóżniceDoMaxaWRzędzie[2];
                }
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
            }
        }

        // wynik jesli 1 produkt nie maja swojej pułki
        // jest to produkt '0'
        else if (ileJestPulekZTym[0] == 0) {
            if (rzedyZerowe >= 1) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
                return;
            }
            int ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[0];
            if (ileJestPulekZTym[1] == 1 && półkiMinimalnychRóżnic[0] == nrPułkiZTymProduktem[1]) {
                ileTrzebaWięcejPrzełożyć = drugieMinimalneRóżniceDoMaxaWRzędzie[0];
                if (minimalneRóżniceDoMaxaWRzędzie[0] + minimalneRóżniceDoMaxaWRzędzie[1] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[0] + minimalneRóżniceDoMaxaWRzędzie[1];
                }
            }
            else if (ileJestPulekZTym[2] == 1 && półkiMinimalnychRóżnic[0] == nrPułkiZTymProduktem[2]) {
                ileTrzebaWięcejPrzełożyć = drugieMinimalneRóżniceDoMaxaWRzędzie[0];
                if (minimalneRóżniceDoMaxaWRzędzie[0] + minimalneRóżniceDoMaxaWRzędzie[2] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[0] + minimalneRóżniceDoMaxaWRzędzie[2];
                }
            }
            System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
        }
        // jest to produkt '1'
        else if (ileJestPulekZTym[1] == 0) {
            if (rzedyZerowe >= 1) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
                return;
            }
            int ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[1];
            if (ileJestPulekZTym[0] == 1 && półkiMinimalnychRóżnic[1] == nrPułkiZTymProduktem[0]) {
                ileTrzebaWięcejPrzełożyć = drugieMinimalneRóżniceDoMaxaWRzędzie[1];
                if (minimalneRóżniceDoMaxaWRzędzie[1] + minimalneRóżniceDoMaxaWRzędzie[0] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[1] + minimalneRóżniceDoMaxaWRzędzie[0];
                }
            }
            else if (ileJestPulekZTym[2] == 1 && półkiMinimalnychRóżnic[1] == nrPułkiZTymProduktem[2]) {
                ileTrzebaWięcejPrzełożyć = drugieMinimalneRóżniceDoMaxaWRzędzie[1];
                if (minimalneRóżniceDoMaxaWRzędzie[1] + minimalneRóżniceDoMaxaWRzędzie[2] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[1] + minimalneRóżniceDoMaxaWRzędzie[2];
                }
            }
            System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
        }
        // jest to produkt '2'
        else if (ileJestPulekZTym[2] == 0) {
            if (rzedyZerowe >= 1) {
                System.out.println("WYNIK: " + (suma - ileNiePrzekladamy));
                return;
            }
            int ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[2];
            if (ileJestPulekZTym[1] == 1 && półkiMinimalnychRóżnic[2] == nrPułkiZTymProduktem[1]) {
                ileTrzebaWięcejPrzełożyć = drugieMinimalneRóżniceDoMaxaWRzędzie[2];
                if (minimalneRóżniceDoMaxaWRzędzie[2] + minimalneRóżniceDoMaxaWRzędzie[1] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[2] + minimalneRóżniceDoMaxaWRzędzie[1];
                }
            }
            else if (ileJestPulekZTym[0] == 1 && półkiMinimalnychRóżnic[2] == nrPułkiZTymProduktem[0]) {
                ileTrzebaWięcejPrzełożyć = drugieMinimalneRóżniceDoMaxaWRzędzie[2];
                if (minimalneRóżniceDoMaxaWRzędzie[2] + minimalneRóżniceDoMaxaWRzędzie[0] < ileTrzebaWięcejPrzełożyć) {
                    ileTrzebaWięcejPrzełożyć = minimalneRóżniceDoMaxaWRzędzie[2] + minimalneRóżniceDoMaxaWRzędzie[0];
                }
            }
            System.out.println("WYNIK: " + (suma - ileNiePrzekladamy + ileTrzebaWięcejPrzełożyć));
        }
    }
}
