package Model;


public class Testprogramm {
    public static void main(String[] args) {
        RoadTraffic s1 = new RoadTraffic();
        Car amg = new Car();
        amg.setRoadTraffic(s1);
        s1.setLight(2,2);
        s1.setLight(1,3);
        s1.addTires(0,1,3);
        while (true) {
            for (int i = 0; i < s1.getActualRows(); i++) {
                for (int j = 0; j < s1.getActualColumns(); j++) {
                    if (s1.getCarRow()==i && s1.getCarColumns() == j) {
                        printAuto(s1);
                    }
                    else if (s1.isLightOnPos(i, j)) {
                        System.out.print("X");
                    }
                    else if (s1.getTiresAtPos(i, j) > 0) {
                        System.out.print(s1.getTiresAtPos(i, j));
                    }
                    else {
                    System.out.print("0");}
                }
                System.out.println();
            }
            int eingabe = IO.readInt("Vor = 0, linksUmdrehen = 1, Reifen aufnehmen = 2, Reifen ablegen = 3");

            switch (eingabe) {
                case 0: amg.vor();
                break;
                case 1: amg.linksUm();
                break;
                case 2: amg.nimm();
                break;
                case 3: amg.gib();
                break;
                default:
                    break;
            }
        }
    }
    static void printAuto(RoadTraffic s1) {
        switch (s1.getCarDirection()) {
            case 0: IO.print("^");
            break;
            case 1: IO.print(">");
            break;
            case 2: IO.print("v");
            break;
            case 3: IO.print("<");
            break;
            default:
            break;
        }
    }
}
