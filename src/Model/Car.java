package Model;

public class Car {
    private RoadTraffic roadTraffic;
    public Car() {
    }
    public void main() {

    }
    public void vor() {
        this.roadTraffic.ago();
    }

    public void nimm() {
        this.roadTraffic.take();
    }

    public void gib() {
        this.roadTraffic.put();
    }

    public void linksUm() {
        this.roadTraffic.leftAround();
    }

    public boolean vornFrei() {
        return this.roadTraffic.isFree();
    }

    public boolean kofferraumLeer() {
        return this.roadTraffic.trunkEmpty();
    }

    public boolean reifenDa() {
        return this.roadTraffic.tiresAtCarPos();
    }

    public RoadTraffic getRoadTraffic() {
        return roadTraffic;
    }

    public void setRoadTraffic(RoadTraffic roadTraffic) {
        this.roadTraffic = roadTraffic;
    }
}
