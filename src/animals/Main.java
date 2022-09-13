package animals;
import java.time.LocalTime;
import java.util.*;



public class Main {
    static ArrayList<String> listOfAnimals = new ArrayList<>();
    static HashMap<String, String> animals = new HashMap<>();
    final static LocalTime now = LocalTime.now();

    final static LocalTime night = LocalTime.of(3,0);

    final static LocalTime morning = LocalTime.of(5,0);
    final static LocalTime evening = LocalTime.of(18,0);

    final static List<String > positiveAnswers = List.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct",
            "indeed", "you bet", "exactly", "you said it");

    final static List<String> negativeAnswers = List.of("n", "no", "no way", "nah", "nope", "negative", "I don't think so", "yeah no");

    final static List<String> clarificationQuestions = List.of("I'm not sure I caught you: was it yes or no?",
                                                                "Funny, I still don't understand, is it yes or no?",
                                                                "Oh, it's too complicated for me: just tell me yes or no.",
                                                                "Could you please simply say yes or no?",
                                                                "Oh, no, don't try to confuse me: say yes or no.");

    final static List<String> phrasesToSayGoodbye = List.of("Bye", "Goodbye", "See you later", "Have a nice day");

    public static void main(String[] args) {
        greetUser();
    }
    private static void greetUser() {
        // depend on local time say greetings
        if (now.isAfter(LocalTime.MIDNIGHT) && now.isBefore(night)) {
            System.out.println("Hi, Night Owl");
        }else if (now.isAfter(night) && now.isBefore(morning)) {
            System.out.println("Hi, Early Bird");
        } else if (now.isAfter(morning) && now.isBefore(LocalTime.NOON)) {
            System.out.println("Good morning");
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(evening)) {
            System.out.println("Good afternoon");
        } else {
            System.out.println("Good evening");
        }
        enterAnimals();

    }

    private static void enterAnimals() {
        System.out.println("Enter the first animal:");
        inputNameOfAnimal(input());
        System.out.println("Enter the second animal:");
        inputNameOfAnimal(input());
        specifyFacts();
    }

    private static String input() {
        //create strings array and delete all articles before animal
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void inputNameOfAnimal(String nameOfAnimal) {
        //check first letter(vowel or not) in name of animal and add article by rules
        String animalWithArticle;
        if (nameOfAnimal.toLowerCase().matches("^[aeiyo].*\\b\\s?\\w*|^xeme\\s?\\w*")) {
            animalWithArticle = "an " + nameOfAnimal.toLowerCase();
        } else {
            animalWithArticle = "a " + nameOfAnimal.toLowerCase();
        }
        listOfAnimals.add(animalWithArticle);
        /*printQuestion(animalWithArticle);*/
    }

    private static void specifyFacts() {
        System.out.printf("Specify a fact that distinguishes %s from %s.%n", listOfAnimals.get(0), listOfAnimals.get(1));
        System.out.println("The sentence should be of the format: 'It can/has/is ...'.");
        addFact(input());
    }

    private static void addFact(String fact) {

            if (isCorrectFact(fact)) {
                String pattern = fact.replaceAll("\\bIt\\b\\s", "").
                        replaceAll("[!?.,:;]+", "");
                addFactToAnotherAnimal(pattern);

            } else {
                System.out.println("The examples of a statement:");
                System.out.println("- It can fly");
                System.out.println("- It has horn");
                System.out.println("- It is a mammal");
                specifyFacts();
            }
    }

    private static void addFactToAnotherAnimal(String pattern) {
        System.out.printf("Is it correct for %s?%n", listOfAnimals.get(1));
        String s = pattern.replaceFirst("\\bcan\\b", "can't")
                .replaceFirst("\\bhas\\b", "doesn't have")
                .replaceAll("\\bis\\b", "isn't");

        if ("No".equals(input())) {
            animals.put(listOfAnimals.get(1), s);
            animals.put(listOfAnimals.get(0), pattern);

        } else {
            animals.put(listOfAnimals.get(0), s);
            animals.put(listOfAnimals.get(1), pattern);
        }
        printResume(pattern);
    }

    private static boolean isCorrectFact(String fact) {
        return fact.matches("^It\\s(can|has|is)\\s.*");
    }

    private static void printResume(String pattern) {
        System.out.println("I have learned the following facts about animals:");
        System.out.printf("- %s %s.%n", listOfAnimals.get(0).replaceFirst("\\b(a|an)\\b", "The"), animals.get(listOfAnimals.get(0)));
        System.out.printf("- %s %s.%n", listOfAnimals.get(1).replaceFirst("\\b(a|an)\\b", "The"), animals.get(listOfAnimals.get(1)));
        System.out.println("I can distinguish these animals by asking the question:");
        System.out.printf("- %s?%n", pattern.replaceFirst("\\bcan\\b", "Can it")
                .replaceFirst("\\bhas\\b", "Does it have")
                .replaceFirst("\\bis\\b", "Is it"));

        sayGoodBye();
    }





    private static void printQuestion(String animalWithArticle) {
        System.out.printf("Is it %s?%n", animalWithArticle);
        while (!isRightConfirmation(input())) {
            askClarificationQuestion();
        }
        sayGoodBye();
    }

    private static boolean isRightConfirmation(String confirmation) {
        boolean check = false;
        for (String answer : positiveAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                System.out.println("You answered: Yes");
                check = true;
                break;
            }
        }
        for (String answer : negativeAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                System.out.println("You answered: No");
                check = true;
                break;
            }
        }

        return check;
    }

    private static void askClarificationQuestion() {
        System.out.println(chooseClarificationQuestion());
    }

    private static String chooseClarificationQuestion() {
        Random random = new Random();
        int indexOfQuestion = random.nextInt(clarificationQuestions.size());
        return clarificationQuestions.get(indexOfQuestion);
    }

    private static void sayGoodBye() {
        System.out.println(chooseHowSayGoodbye());
    }

    private static String chooseHowSayGoodbye() {
        Random random = new Random();
        int indexOfPhrase = random.nextInt(phrasesToSayGoodbye.size());
        return phrasesToSayGoodbye.get(indexOfPhrase);
    }


}


