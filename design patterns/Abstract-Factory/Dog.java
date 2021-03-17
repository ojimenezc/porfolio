public class Dog implements Animalable{
    @Override
    public String getAnimal(){
        return "Dog";
    }

    @Override
    public String makeSound(){
        return "Guau";
    }
}