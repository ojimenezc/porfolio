public class ColorFactory implements AbstractFactory{

    @Override
    public Colorable create(String color) {
        if ("Brown".equalsIgnoreCase(color)) {
            return new Brown();
        } else if ("Yellow".equalsIgnoreCase(color)) {
            return new Yellow();
        }

        return null;
    }
}