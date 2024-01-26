package ch.heig.dai.lab.udp;

public class App {
    private static Instrument parse(String instrument) {
        return switch (instrument.toLowerCase()) {
            case "piano"   -> Instrument.PIANO;
            case "trumpet" -> Instrument.TRUMPET;
            case "flute"   -> Instrument.FLUTE;
            case "violin"  -> Instrument.VIOLIN;
            case "drum"    -> Instrument.DRUM;
            default        -> throw new IllegalStateException("Unexpected value: " + instrument);
        };
    }
    public static void main(String[] args) throws IllegalArgumentException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java -jar udp.jar <piano|trumpet|flute|violin|drum>");
        }
        Musician musician = new Musician(parse(args[0]));
        try {
            musician.run();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
