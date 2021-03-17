public class Duck implements Animalable{
    @Override
    public String getAnimal(){
        return "Duck";
    }

    @Override
    public String makeSound(){
        return "Cuak";
    }
}